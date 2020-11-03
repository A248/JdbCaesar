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
package space.arim.jdbcaesar.internal.transact;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import space.arim.jdbcaesar.error.UncheckedSQLException;
import space.arim.jdbcaesar.internal.QueryExecutor;
import space.arim.jdbcaesar.internal.query.ConnectionAcceptor;
import space.arim.jdbcaesar.transact.TransactionQueryBuilder;
import space.arim.jdbcaesar.transact.TransactionController;
import space.arim.jdbcaesar.transact.TransactionQuerySource;

class TransactionQuerySourceImpl
		implements TransactionQuerySource, TransactionController, QueryExecutor<TransactionQueryBuilder> {
	
	private final TransactionImpl<?> transaction;
	private final Connection connection;
	
	TransactionQuerySourceImpl(TransactionImpl<?> transaction, Connection connection) {
		this.transaction = transaction;
		this.connection = connection;
	}
	
	@Override
	public TransactionQueryBuilder query(String statement) {
		return new TransactionQueryBuilderImpl(this, statement, transaction.getProperties());
	}

	@Override
	public <R> R execute(ConnectionAcceptor<R> acceptor) {
		try {
			return acceptor.acceptConnection(connection);
		} catch (SQLException ex) {
			throw new UncheckedSQLException(acceptor.getExceptionDetails(), ex);
		} finally {
			transaction.setNeedsCommit(true);
		}
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
		transaction.setNeedsCommit(false);
	}

	@Override
	public void rollback() throws SQLException {
		connection.rollback();
		transaction.setNeedsCommit(false);
	}

}
