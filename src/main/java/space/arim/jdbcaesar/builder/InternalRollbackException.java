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
package space.arim.jdbcaesar.builder;

/**
 * Internal exception used to forcibly rollback the entire transaction should a {@link java.sql.SQLException}
 * occur
 * 
 * @author A248
 *
 */
class InternalRollbackException extends RuntimeException {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = -6641936634527959500L;
	
	static final InternalRollbackException INSTANCE = new InternalRollbackException();
	
	/**
	 * Creates the exception singleton, using {@link Throwable#Throwable(String, Throwable, boolean, boolean)}
	 * (and subclasses' chained constructors) to disable suppression and make the stacktrace non writable.
	 * 
	 */
	private InternalRollbackException() {
		super(null, null, false, false);
	}
	
}
