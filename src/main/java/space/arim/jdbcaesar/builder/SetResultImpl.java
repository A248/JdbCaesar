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
import java.util.HashSet;
import java.util.Set;

import space.arim.jdbcaesar.error.SubstituteProvider;
import space.arim.jdbcaesar.mapper.ResultElementMapper;
import space.arim.jdbcaesar.query.SetResult;

class SetResultImpl<E> extends AbstractResultedQueryResult<Set<E>> implements SetResult<E> {

	private final ResultElementMapper<E> mapper;
	
	SetResultImpl(InitialQueryBuilderImpl initialBuilder, ResultElementMapper<E> mapper, SubstituteProvider<Set<E>> onError) {
		super(initialBuilder, onError);
		this.mapper = mapper;
	}

	@Override
	Set<E> getResult(ResultSet resultSet) throws SQLException {
		Set<E> result = new HashSet<>();
		while (resultSet.next()) {
			result.add(mapper.mapElementFrom(resultSet));
		}
		return result;
	}
	
}
