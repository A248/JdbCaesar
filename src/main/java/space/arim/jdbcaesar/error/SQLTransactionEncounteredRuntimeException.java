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
 * Exception wrapping a {@link RuntimeException} thrown from within a transaction body
 * 
 * @author A248
 *
 * @deprecated This is no longer how unchecked exceptions inside transactions are handled. Rather,
 * they are propagated to the caller.
 */
@Deprecated
public class SQLTransactionEncounteredRuntimeException extends SQLException {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = -1538948655415392465L;
	
	/**
	 * Creates the exception
	 * 
	 * @param cause the runtime exception cause
	 */
	public SQLTransactionEncounteredRuntimeException(RuntimeException cause) {
		super(cause);
	}
	
	/**
	 * Creates the exception
	 * 
	 * @param message the exception message
	 * @param cause the runtime exception cause
	 */
	public SQLTransactionEncounteredRuntimeException(String message, RuntimeException cause) {
		super(message, cause);
	}

	/**
	 * Gets the runtime exception which caused this SQL exception
	 * 
	 */
	@Override
	public synchronized RuntimeException getCause() {
		return (RuntimeException) super.getCause();
	}
	
}
