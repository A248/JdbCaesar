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

import space.arim.jdbcaesar.ExecutableSQL;

/**
 * An exception handler of {@link SQLException}s
 * 
 * @author A248
 *
 */
@FunctionalInterface
public interface ExceptionHandler {

	/**
	 * The default implementation rethrows the {@code SQLException} wrapped in {@code UncheckedSQLException}.
	 * Note that this behaviour therefore makes {@link ExecutableSQL#executeOrGet(SubstituteProvider)}
	 * equivalent to {@link ExecutableSQL#execute()}
	 * 
	 */
	ExceptionHandler DEFAULT = new ExceptionHandler() {
		@Override
		public void handleException(SQLException ex) {
			throw new UncheckedSQLException(ex);
		}
	};
	
	/**
	 * Handles the exception. This often includes logging the exception
	 * 
	 * @param ex the SQL exception to handle
	 */
	void handleException(SQLException ex);
	
}
