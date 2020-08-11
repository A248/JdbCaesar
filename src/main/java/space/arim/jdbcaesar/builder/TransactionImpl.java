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

import space.arim.jdbcaesar.error.SubstituteProvider;
import space.arim.jdbcaesar.query.InitialQueryBuilder;
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
		JdbCaesarImpl jdbCaesar = transactionBuilder.jdbCaesar;
		int isolationLevel = transactionBuilder.isolation;
		boolean readOnly = transactionBuilder.readOnly;
		Transactor<T> transactor = transactionBuilder.transactor;
		T result;
		try (Connection connection = jdbCaesar.getDatabaseSource().getConnection()) {
			connection.setTransactionIsolation(isolationLevel);
			connection.setReadOnly(readOnly);
			try {
				TransactionQueryExecutor executor = new TransactionQueryExecutor(connection, jdbCaesar);
				TransactionQuerySource source = new TransactionQuerySourceImpl(executor);
				result = transactor.transact(source);
				connection.commit();

			} catch (RollMeBackException ex) {
				connection.rollback();
				result = onRollback.getSubstituteValue();
			}
		} catch (SQLException ex) {
			jdbCaesar.getExceptionHandler().handleException(ex);
			result = onRollback.getSubstituteValue();
		}
		return result;
	}

}

class TransactionQuerySourceImpl implements TransactionQuerySource {

	private final TransactionQueryExecutor executor;
	
	TransactionQuerySourceImpl(TransactionQueryExecutor executor) {
		this.executor = executor;
	}
	
	@Override
	public InitialQueryBuilder query(String statement) {
		return new InitialQueryBuilderImpl(executor.jdbCaesar.adapters, executor, statement, executor.jdbCaesar.fetchSize);
	}
	
}

class TransactionQueryExecutor implements QueryExecutor {
	
	private final Connection connection;
	final JdbCaesarImpl jdbCaesar;
	
	TransactionQueryExecutor(Connection connection, JdbCaesarImpl jdbCaesar) {
		this.connection = connection;
		this.jdbCaesar = jdbCaesar;
	}

	@Override
	public void execute(ConnectionAcceptor acceptor) {
		try {
			acceptor.acceptConnection(connection);
		} catch (SQLException ex) {
			jdbCaesar.getExceptionHandler().handleException(ex);
			throw new RollMeBackException(ex);
		}
	}

	@Override
	public int nullType() {
		return jdbCaesar.nullType;
	}
}
