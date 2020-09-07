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

/**
 * An exception handler of {@link SQLException}s
 * 
 * @author A248
 *
 */
@FunctionalInterface
public interface ExceptionHandler {

	/**
	 * Handles the exception. This may include logging the exception or an exception message. <br>
	 * <br>
	 * Some implementations may choose to fail fast by propagating an unchecked exception wrapper.
	 * However, users taking this route should be aware that while it is supported, it may be unclear
	 * to the caller of {@code execute()} methods which exception will be thrown
	 * 
	 * @param exception the SQL exception to handle
	 */
	void handleException(SQLException exception);
	
}
