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
import java.sql.ResultSet;
import java.sql.SQLException;

import space.arim.jdbcaesar.error.SubstituteProvider;

abstract class AbstractResultedQueryResult<R> extends AbstractQueryResult<R> {

	AbstractResultedQueryResult(InitialQueryBuilderImpl<?> initialBuilder, SubstituteProvider<R> onError) {
		super(initialBuilder, onError);
	}
	
	@Override
	R getResult(PreparedStatement prepStmt) throws SQLException {
		R result;
		try (ResultSet resultSet = prepStmt.executeQuery()) {
			result = getResult(resultSet);
		}
		return result;
	}
	
	abstract R getResult(ResultSet resultSet) throws SQLException;
	
	@Override
	PreparedStatement prepareStatement(Connection conn) throws SQLException {
		InitialQueryBuilderImpl<?> initialBuilder = this.initialBuilder;
		return conn.prepareStatement(initialBuilder.statement,
				initialBuilder.type.getType(), initialBuilder.concurrency.getConcurrency());
	}

}
