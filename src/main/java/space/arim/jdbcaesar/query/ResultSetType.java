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
 * Enumeration result set types. Each corresponds to a result set type constant in {@link ResultSet}
 * 
 * @author A248
 *
 */
public enum ResultSetType {

	/**
	 * Corresponds to {@link ResultSet#TYPE_FORWARD_ONLY}
	 * 
	 */
	FORWARD_ONLY(ResultSet.TYPE_FORWARD_ONLY),
	
	/**
	 * Corresponds to {@link ResultSet#TYPE_SCROLL_INSENSITIVE}
	 * 
	 */
	SCROLL_INSENSITIVE(ResultSet.TYPE_SCROLL_INSENSITIVE),
	
	/**
	 * Corresponds to {@link ResultSet#TYPE_SCROLL_SENSITIVE}
	 * 
	 */
	SCROLL_SENSITIVE(ResultSet.TYPE_SCROLL_SENSITIVE);
	
	private int type;
	
	private ResultSetType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
}
