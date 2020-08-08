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
import space.arim.jdbcaesar.mapper.ResultSingleMapper;
import space.arim.jdbcaesar.query.SingleResult;

class SingleResultImpl<T> extends AbstractResultedQueryResult<T> implements SingleResult<T> {
	
	private final ResultSingleMapper<T> mapper;
	
	SingleResultImpl(InitialQueryBuilderImpl initialBuilder, ResultSingleMapper<T> mapper, SubstituteProvider<T> onError) {
		super(initialBuilder, onError);
		this.mapper = mapper;
	}

	@Override
	T getResult(ResultSet resultSet) throws SQLException {
		T result;
		if (resultSet.next()) {
			result = mapper.mapValueFrom(resultSet);
		} else {
			result = null;
		}
		return result;
	}
	
}
