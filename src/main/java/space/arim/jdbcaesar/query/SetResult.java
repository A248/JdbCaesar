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

import java.sql.SQLException;
import java.util.Set;

/**
 * An executable set query result
 * 
 * @author A248
 *
 * @param <E> the element type
 */
public interface SetResult<E> extends QueryResult<Set<E>> {

	/**
	 * Executes the query and returns its result. <br>
	 * <br>
	 * If there were no results, an empty set is returned. If there was a {@link SQLException}, the result
	 * of the error substitute provider will be returned.
	 * 
	 */
	@Override
	Set<E> execute();
	
}
