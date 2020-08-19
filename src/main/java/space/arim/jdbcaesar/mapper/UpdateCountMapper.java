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
package space.arim.jdbcaesar.mapper;

import space.arim.jdbcaesar.error.SQLNoUpdateCountException;

/**
 * A mapper which maps an update count to a single value
 * 
 * @author A248
 *
 * @param <T> the result type
 */
@FunctionalInterface
public interface UpdateCountMapper<T> {

	/**
	 * Maps a single value from the specified update count. <br>
	 * <br>
	 * By default, if the query did not produce an update coumt, a {@link SQLNoUpdateCountException} will be thrown
	 * to cancel the query or transaction and this mapper will not be invoked. <br>
	 * However, if {@link #allowNonUpdateCount()} = {@code true}, and the query did not produce an update count,
	 * then {@code updateCount} will be {@code -1}.
	 * 
	 * @param updateCount the update count or possibly {@code -1} if {@link #allowNonUpdateCount()} is {@code true}
	 * @return the single result
	 */
	T mapValueFrom(int updateCount);
	
	/**
	 * Whether this mapper accepts no update count. By default, this is false.
	 * 
	 * @return true if this mapper accepts no update count, false otherwise
	 */
	default boolean allowNonUpdateCount() {
		return false;
	}
	
	/**
	 * Gets an identity update count mapper, equivalent to <pre>{@code (updateCount) -> updateCount}</pre>
	 * 
	 * @return an identity update count mapper
	 */
	static UpdateCountMapper<Integer> identity() {
		return (updateCount) -> updateCount;
	}
	
}
