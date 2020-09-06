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
import space.arim.jdbcaesar.mapper.UpdateGenKeysMapper;

class UpdateGenKeysResultImpl<R> extends AbstractQueryResult<R> {

	private final UpdateGenKeysMapper<R> mapper;
	
	UpdateGenKeysResultImpl(InitialQueryBuilderImpl<?> initialBuilder, UpdateGenKeysMapper<R> mapper, SubstituteProvider<R> onError) {
		super(initialBuilder, onError);
		this.mapper = mapper;
	}
	
	@Override
	PreparedStatement prepareStatement(Connection conn) throws SQLException {
		return conn.prepareStatement(initialBuilder.statement, Statement.RETURN_GENERATED_KEYS);
	}

	@Override
	R getResult(PreparedStatement prepStmt) throws SQLException {
		R result;
		prepStmt.execute();
		int updateCount = prepStmt.getUpdateCount();
		if (updateCount == -1 && !mapper.allowNonUpdateCount()) {
			throw new SQLNoUpdateCountException();
		}
		result = mapper.mapValueFrom(updateCount, prepStmt.getGeneratedKeys());
		return result;
	}
	
}
