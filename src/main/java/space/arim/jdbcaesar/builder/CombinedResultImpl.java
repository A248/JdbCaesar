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

import java.sql.ResultSet;
import java.sql.SQLException;

import space.arim.jdbcaesar.error.SubstituteProvider;
import space.arim.jdbcaesar.mapper.CombinedResultMapper;

class CombinedResultImpl<R> extends AbstractResultedQueryResult<R> {
	
	private final CombinedResultMapper<R> mapper;
	
	CombinedResultImpl(InitialQueryBuilderImpl initialBuilder, CombinedResultMapper<R> mapper, SubstituteProvider<R> onError) {
		super(initialBuilder, onError);
		this.mapper = mapper;
	}

	@Override
	R getResult(ResultSet resultSet) throws SQLException {
		return mapper.mapFrom(resultSet);
	}
	
}
