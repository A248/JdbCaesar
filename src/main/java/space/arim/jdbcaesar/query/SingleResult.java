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
package space.arim.jdbcaesar.query;

import space.arim.jdbcaesar.mapper.ResultSingleMapper;

/**
 * A singular query result
 * 
 * @author A248
 *
 * @param <T> the singular result type
 */
public interface SingleResult<T> extends QueryResult<T> {

	/**
	 * Executes the query and returns its result. <br>
	 * <br>
	 * If there were no results, {@code null} is returned. If there was a {@code SQLException},
	 * the result of the error handler will be returned. Otherwise, the {@link ResultSingleMapper}
	 * is used.
	 * 
	 */
	@Override
	T execute();
	
}
