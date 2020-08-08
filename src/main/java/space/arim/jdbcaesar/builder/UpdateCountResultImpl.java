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
import java.sql.Statement;

import space.arim.jdbcaesar.error.SQLNoUpdateCountException;
import space.arim.jdbcaesar.error.SubstituteProvider;
import space.arim.jdbcaesar.mapper.UpdateCountMapper;
import space.arim.jdbcaesar.query.SingleResult;

class UpdateCountResultImpl<T> extends AbstractQueryResult<T> implements SingleResult<T> {

	private final UpdateCountMapper<T> mapper;
	
	UpdateCountResultImpl(InitialQueryBuilderImpl initialBuilder, UpdateCountMapper<T> mapper, SubstituteProvider<T> onError) {
		super(initialBuilder, onError);
		this.mapper = mapper;
	}

	@Override
	PreparedStatement prepareStatement(Connection conn) throws SQLException {
		return conn.prepareStatement(initialBuilder.statement, Statement.NO_GENERATED_KEYS);
	}
	
	@Override
	T getResult(PreparedStatement prepStmt) throws SQLException {
		T result;
		prepStmt.execute();
		int updateCount = prepStmt.getUpdateCount();
		if (updateCount == -1 && !mapper.allowNonUpdateCount()) {
			throw new SQLNoUpdateCountException();
		}
		result = mapper.mapValueFrom(updateCount);
		return result;
	}

}
