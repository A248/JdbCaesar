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
import java.sql.ResultSet;
import java.sql.SQLException;

import space.arim.jdbcaesar.mapper.TotalResultMapper;

class TotalResultImpl<R> extends AbstractQueryResult<R> {

	private final TotalResultMapper<R> mapper;
	
	TotalResultImpl(QueryBuilderImpl<?> initialBuilder, TotalResultMapper<R> mapper) {
		super(initialBuilder);
		this.mapper = mapper;
	}
	
	@Override
	R getResult(PreparedStatement prepStmt) throws SQLException {
		prepStmt.setFetchSize(getInitialBuilder().getFetchSize());
		R result;
		try (ResultSet resultSet = prepStmt.executeQuery()) {
			result = mapper.mapFrom(resultSet);
		}
		return result;
	}
	
	@Override
	PreparedStatement prepareStatement(Connection conn) throws SQLException {
		QueryBuilderImpl<?> initialBuilder = getInitialBuilder();
		int type = initialBuilder.getResultSetType().getType();
		int concurrency = initialBuilder.getResultSetConcurrency().getConcurrency();
		return conn.prepareStatement(initialBuilder.getStatement(), type, concurrency);
	}

}
