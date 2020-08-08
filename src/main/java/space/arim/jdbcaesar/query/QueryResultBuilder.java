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

import space.arim.jdbcaesar.error.SubstituteProvider;

/**
 * General interface for results almost complete but requiring an error substitute provider.
 * 
 * @author A248
 *
 * @param <T> the result type
 */
public interface QueryResultBuilder<T> {

	/**
	 * Sets the substitute provider of this result builder to the specified one
	 * and returns an executable result.
	 * 
	 * @param onError the substitute result provider should an error occur
	 * @return a {@link QueryResult}
	 */
	QueryResult<T> onError(SubstituteProvider<T> onError);
	
}
