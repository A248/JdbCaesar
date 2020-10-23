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
import java.util.Objects;

/**
 * Unchecked wrapper for {@link SQLException}s
 * 
 * @author A248
 *
 */
public class UncheckedSQLException extends RuntimeException {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 4088813370123105349L;
	
	/**
	 * Creates from an exception cause
	 * 
	 * @param cause the cause
	 * @throws NullPointerException if {@code cause} is null
	 */
	public UncheckedSQLException(SQLException cause) {
		super(Objects.requireNonNull(cause, "cause"));
	}
	
	/**
	 * Creates from a message and a cause
	 * 
	 * @param message the message
	 * @param cause the cause
	 * @throws NullPointerException if {@code cause} is null
	 */
	public UncheckedSQLException(String message, SQLException cause) {
		super(message, Objects.requireNonNull(cause, "cause"));
	}
	
	@Override
	public synchronized SQLException getCause() {
		return (SQLException) super.getCause();
	}

}
