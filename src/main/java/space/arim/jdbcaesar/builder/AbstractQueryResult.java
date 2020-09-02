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
import java.sql.PreparedStatement;
import java.sql.SQLException;

import space.arim.jdbcaesar.error.SubstituteProvider;
import space.arim.jdbcaesar.query.QueryResult;

abstract class AbstractQueryResult<R> extends ConnectionAcceptor implements QueryResult<R> {

	private final SubstituteProvider<R> onError;
	
	private R result;
	
	AbstractQueryResult(InitialQueryBuilderImpl initialBuilder, SubstituteProvider<R> onError) {
		super(initialBuilder);
		this.onError = onError;
	}
	
	@Override
	void acceptConnection(Connection conn) throws SQLException {
		R result;
		int fetchSize = initialBuilder.fetchSize;

		try (PreparedStatement prepStmt = prepareStatement(conn)) {
			prepStmt.setFetchSize(fetchSize);
			SqlUtils.setArguments(prepStmt, initialBuilder.params, initialBuilder.executor.nullType());
			result = getResult(prepStmt);
		}
		this.result = result;
	}
	
	// Overriden where generated keys and result set attributes are concerned
	PreparedStatement prepareStatement(Connection conn) throws SQLException {
		return conn.prepareStatement(initialBuilder.statement);
	}
	
	abstract R getResult(PreparedStatement prepStmt) throws SQLException;
	
	@Override
	void onError() {
		result = onError.getSubstituteValue();
	}
	
	@Override
	public R execute() {
		initialBuilder.executor.execute(this);
		return result;
	}
	
}
