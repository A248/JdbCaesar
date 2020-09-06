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

import space.arim.jdbcaesar.error.SQLTransactionEncounteredRuntimeException;
import space.arim.jdbcaesar.error.SubstituteProvider;
import space.arim.jdbcaesar.transact.InitialTransactedQueryBuilder;
import space.arim.jdbcaesar.transact.RollMeBackException;
import space.arim.jdbcaesar.transact.Transaction;
import space.arim.jdbcaesar.transact.TransactionQuerySource;
import space.arim.jdbcaesar.transact.Transactor;

class TransactionImpl<T> implements Transaction<T> {
	
	private final TransactionBuilderImpl<T> transactionBuilder;
	private final SubstituteProvider<T> onRollback;
	
	TransactionImpl(TransactionBuilderImpl<T> transactionBuilder, SubstituteProvider<T> onRollback) {
		this.transactionBuilder = transactionBuilder;
		this.onRollback = onRollback;
	}
	
	@Override
	public T execute() {
		InitialTransactionBuilderImpl initialBuilder = transactionBuilder.initialBuilder;
		JdbCaesarImpl jdbCaesar = initialBuilder.jdbCaesar;
		int isolationLevel = initialBuilder.settings.isolation.getLevel();
		boolean readOnly = initialBuilder.settings.readOnly;
		Transactor<T> transactor = transactionBuilder.transactor;

		T result;
		try (Connection connection = jdbCaesar.getConnectionSource().getConnection()) {
			connection.setTransactionIsolation(isolationLevel);
			connection.setReadOnly(readOnly);
			try {
				TransactionQuerySource source = new TransactionQuerySourceImpl(connection, jdbCaesar);
				result = transactor.transact(source);
				connection.commit();

			} catch (RollMeBackException ex) {
				connection.rollback();
				result = onRollback.getSubstituteValue();

			} catch (RuntimeException ex) {
				connection.rollback();
				jdbCaesar.getExceptionHandler().handleException(new SQLTransactionEncounteredRuntimeException(ex));
				result = onRollback.getSubstituteValue();
			}
		} catch (SQLException ex) {
			jdbCaesar.getExceptionHandler().handleException(ex);
			result = onRollback.getSubstituteValue();
		}
		return result;
	}

}

class TransactionQuerySourceImpl implements TransactionQuerySource, QueryExecutor<InitialTransactedQueryBuilder> {
	
	private final Connection connection;
	final JdbCaesarImpl jdbCaesar;
	
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
			throw new RollMeBackException(ex);
		}
	}

	@Override
	public int nullType() {
		return jdbCaesar.nullType;
	}
}
