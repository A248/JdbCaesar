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
package space.arim.jdbcaesar.error;

import java.sql.SQLException;

import space.arim.jdbcaesar.mapper.UpdateCountMapper;

/**
 * A {@code SQLException} thrown to produce fail fast behaviour when an {@link UpdateCountMapper}
 * expected an update count but the query did not produce an update count.
 * 
 * @author A248
 *
 */
public class SQLNoUpdateCountException extends SQLException {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = -7233580034199827356L;
	
	/**
	 * Creates the exception
	 * 
	 */
	public SQLNoUpdateCountException() {

	}
	
	/**
	 * Creates the exception
	 * 
	 * @param cause the exception cause
	 */
	public SQLNoUpdateCountException(SQLException cause) {
		super(cause);
	}

}
