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
package space.arim.jdbcaesar.internal.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import space.arim.jdbcaesar.error.SubstituteProvider;
import space.arim.jdbcaesar.error.UncheckedSQLException;
import space.arim.jdbcaesar.query.QueryResult;

abstract class AbstractQueryResult<R> extends ConnectionAcceptor<R> implements QueryResult<R> {
	
	AbstractQueryResult(QueryBuilderImpl<?> initialBuilder) {
		super(initialBuilder);
	}
	
	@Override
	public R acceptConnection(Connection connection) throws SQLException {
		QueryBuilderImpl<?> initialBuilder = getInitialBuilder();

		try (PreparedStatement prepStmt = prepareStatement(connection)) {
			initialBuilder.setParameters(prepStmt);
			return getResult(prepStmt);
		}
	}
	
	PreparedStatement prepareStatement(Connection connection) throws SQLException {
		return connection.prepareStatement(getInitialBuilder().getStatement());
	}
	
	abstract R getResult(PreparedStatement prepStmt) throws SQLException;
	
	@Override
	public R execute() {
		return getInitialBuilder().getExecutor().execute(this);
	}
	
	@Override
	public R executeOrGet(SubstituteProvider<R> onError) {
		try {
			return execute();
		} catch (UncheckedSQLException ex) {
			getInitialBuilder().getProperties().getExceptionHandler().handleException(ex.getCause());
			return onError.getSubstituteValue();
		}
	}
	
}
