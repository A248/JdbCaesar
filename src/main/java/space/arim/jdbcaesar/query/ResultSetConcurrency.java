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

import java.sql.ResultSet;

/**
 * Enumeration of result set concurrency types. Each corresponds to a result set concurrency constant
 * in {@link ResultSet}
 * 
 * @author A248
 *
 */
public enum ResultSetConcurrency {

	/**
	 * Corresponds to {@link ResultSet#CONCUR_UPDATABLE}
	 * 
	 */
	UPDATABLE(ResultSet.CONCUR_UPDATABLE),
	
	/**
	 * Corresponds to {@link ResultSet#CONCUR_READ_ONLY}
	 * 
	 */
	READ_ONLY(ResultSet.CONCUR_READ_ONLY);
	
	private int concurrency;
	
	private ResultSetConcurrency(int concurrency) {
		this.concurrency = concurrency;
	}
	
	/**
	 * Gets the result set concurrency integer represented by this {@code ResultSetConcurrency}
	 * 
	 * @return the concurrency integer
	 */
	public int getConcurrency() {
		return concurrency;
	}
	
}
