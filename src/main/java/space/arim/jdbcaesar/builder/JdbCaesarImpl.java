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
import java.util.List;
import java.util.Objects;

import space.arim.jdbcaesar.ConnectionSource;
import space.arim.jdbcaesar.JdbCaesar;
import space.arim.jdbcaesar.adapter.DataTypeAdapter;
import space.arim.jdbcaesar.error.ExceptionHandler;
import space.arim.jdbcaesar.query.InitialSingleQueryBuilder;
import space.arim.jdbcaesar.transact.InitialTransactionBuilder;
import space.arim.jdbcaesar.transact.IsolationLevel;

class JdbCaesarImpl implements JdbCaesar, QueryExecutor<InitialSingleQueryBuilder> {

	private final ConnectionSource connectionSource;
	private final ExceptionHandler exceptionHandler;
	final DataTypeAdapter[] adapters;
	final int fetchSize;
	final IsolationLevel isolation;
	final boolean readOnly;
	final int nullType;
	final boolean rewrapExceptions;
	
	JdbCaesarImpl(ConnectionSource connectionSource, ExceptionHandler exceptionHandler, List<DataTypeAdapter> adapters,
			int fetchSize, IsolationLevel isolation, boolean readOnly, int nullType, boolean rewrapExceptions) {
		this.connectionSource = Objects.requireNonNull(connectionSource, "connectionSource");
		this.exceptionHandler = Objects.requireNonNull(exceptionHandler, "exceptionHandler");
		this.adapters = adapters.toArray(new DataTypeAdapter[] {});
		this.fetchSize = fetchSize;
		this.isolation = isolation;
		this.readOnly = readOnly;
		this.nullType = nullType;
		this.rewrapExceptions = rewrapExceptions;
	}

	@Override
	public ConnectionSource getConnectionSource() {
		return connectionSource;
	}

	@Override
	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}
	
	@Override
	public InitialSingleQueryBuilder query(String statement) {
		return new InitialSingleQueryBuilderImpl(adapters, this, statement, fetchSize, isolation, readOnly);
	}
	
	@Override
	public InitialTransactionBuilder transaction() {
		return new InitialTransactionBuilderImpl(this, isolation, readOnly);
	}
	
	@Override
	public void execute(ConnectionAcceptor acceptor) {
		InitialSingleQueryBuilderImpl isqbi = (InitialSingleQueryBuilderImpl) acceptor.initialBuilder;
		int isolationLevel = isqbi.settings.isolation.getLevel();
		boolean readOnly = isqbi.settings.readOnly;

		try (Connection conn = connectionSource.getConnection()) {
			conn.setTransactionIsolation(isolationLevel);
			conn.setReadOnly(readOnly);
			try {
				acceptor.acceptConnection(conn);
				conn.commit();
			} catch (SQLException ex) {
				conn.rollback();
				throw ex;
			}
		} catch (SQLException ex) {
			if (rewrapExceptions) {
				ex = acceptor.rewrapExceptionWithDetails(ex);
			}
			exceptionHandler.handleException(ex);
			acceptor.onError();
		}
	}

	@Override
	public int nullType() {
		return nullType;
	}
	
}
