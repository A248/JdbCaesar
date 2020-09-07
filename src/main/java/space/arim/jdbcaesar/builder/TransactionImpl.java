/* 
 * JdbCaesar
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * JdbCaesar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JdbCaesar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JdbCaesar. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU Lesser General Public License.
 */
package space.arim.jdbcaesar.builder;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import space.arim.jdbcaesar.error.SubstituteProvider;
import space.arim.jdbcaesar.transact.InitialTransactedQueryBuilder;
import space.arim.jdbcaesar.transact.Transaction;
import space.arim.jdbcaesar.transact.TransactionBody;
import space.arim.jdbcaesar.transact.TransactionController;
import space.arim.jdbcaesar.transact.TransactionQuerySource;

class TransactionImpl<T> implements Transaction<T> {
	
	private final TransactionBuilderImpl<T> transactionBuilder;
	private final SubstituteProvider<T> onError;
	
	TransactionImpl(TransactionBuilderImpl<T> transactionBuilder, SubstituteProvider<T> onError) {
		this.transactionBuilder = transactionBuilder;
		this.onError = onError;
	}
	
	@Override
	public T execute() {
		InitialTransactionBuilderImpl initialBuilder = transactionBuilder.initialBuilder;
		JdbCaesarImpl jdbCaesar = initialBuilder.jdbCaesar;
		int isolationLevel = initialBuilder.settings.isolation.getLevel();
		boolean readOnly = initialBuilder.settings.readOnly;
		TransactionBody<T> body = transactionBuilder.body;

		T result;
		try (Connection connection = jdbCaesar.getConnectionSource().getConnection()) {
			connection.setAutoCommit(false);
			connection.setTransactionIsolation(isolationLevel);
			connection.setReadOnly(readOnly);
			try {
				TransactionQuerySourceImpl sourceAndController = new TransactionQuerySourceImpl(connection, jdbCaesar);
				result = body.transact(sourceAndController, sourceAndController);
				connection.commit();

			} catch (@SuppressWarnings("deprecation") space.arim.jdbcaesar.transact.RollMeBackException
					| InternalRollbackException ex) {
				connection.rollback();
				result = onError.getSubstituteValue();

			} catch (SQLException | RuntimeException ex) {
				try {
					connection.rollback();
				} catch (SQLException suppressed) { ex.addSuppressed(suppressed); }
				throw ex;
			}
		} catch (SQLException ex) {
			jdbCaesar.getExceptionHandler().handleException(ex);
			result = onError.getSubstituteValue();
		}
		return result;
	}

}

class TransactionQuerySourceImpl implements TransactionQuerySource, TransactionController, QueryExecutor<InitialTransactedQueryBuilder> {
	
	private final Connection connection;
	private final JdbCaesarImpl jdbCaesar;
	
	TransactionQuerySourceImpl(Connection connection, JdbCaesarImpl jdbCaesar) {
		this.connection = connection;
		this.jdbCaesar = jdbCaesar;
	}
	
	@Override
	public InitialTransactedQueryBuilder query(String statement) {
		return new InitialTransactedQueryBuilderImpl(jdbCaesar.adapters, this, statement, jdbCaesar.fetchSize);
	}

	@Override
	public void execute(ConnectionAcceptor acceptor) {
		try {
			acceptor.acceptConnection(connection);
		} catch (SQLException ex) {
			if (jdbCaesar.rewrapExceptions) {
				ex = acceptor.rewrapExceptionWithDetails(ex);
			}
			jdbCaesar.getExceptionHandler().handleException(ex);
			throw InternalRollbackException.INSTANCE;
		}
	}

	@Override
	public int nullType() {
		return jdbCaesar.nullType;
	}
	
	@Override
	public Savepoint savepoint() throws SQLException {
		return connection.setSavepoint();
	}
	
	@Override
	public Savepoint savepoint(String name) throws SQLException {
		return connection.setSavepoint(name);
	}
	
	@Override
	public void release(Savepoint savepoint) throws SQLException {
		connection.releaseSavepoint(savepoint);
	}

	@Override
	public void rollbackTo(Savepoint savepoint) throws SQLException {
		connection.rollback(savepoint);
	}

	@Override
	public void rollback() throws SQLException {
		connection.rollback();
	}

}
