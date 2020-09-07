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
package space.arim.jdbcaesar.transact;

import java.sql.SQLException;

/**
 * Unchecked exception which may be thrown from inside of transactions (per {@link Transactor#transact(TransactionQuerySource)}
 * to rollback the entire transaction. <br>
 * <br>
 * This exception is used for control flow processing. It is not intended to be caught except
 * by JdbCaesar. To enhance performance, it has no stacktrace.
 * 
 * @author A248
 *
 * @deprecated This is no longer the way transactions are rolled back. Instead, {@link TransactionBody}
 * should be used in order to access a {@link TransactionController}
 */
@Deprecated
public class RollMeBackException extends RuntimeException {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1065364924992480011L;
	
	/**
	 * Creates the exception
	 * 
	 */
	public RollMeBackException() {
		
	}
	
	/**
	 * Creates the exception with the given cause
	 * 
	 * @param cause the SQL exception cause
	 */
	public RollMeBackException(SQLException cause) {
		super(cause);
	}
	
	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

}
