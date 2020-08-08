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

import java.sql.Connection;

/**
 * Enumeration of transaction isolation levels defined by the SQL standard. Each
 * isolation level corresponds to a constant in {@link Connection}.
 * 
 * @author A248
 *
 */
public enum IsolationLevel {

	/**
	 * Corresponds to {@link Connection#TRANSACTION_READ_UNCOMMITTED}
	 * 
	 */
	READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
	
	/**
	 * Corresponds to {@link Connection#TRANSACTION_READ_COMMITTED}
	 * 
	 */
	READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
	
	/**
	 * Corresponds to {@link Connection#TRANSACTION_REPEATABLE_READ}
	 * 
	 */
	REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
	
	/**
	 * Corresponds to {@link Connection#TRANSACTION_SERIALIZABLE}
	 * 
	 */
	SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);
	
	private final int level;
	
	private IsolationLevel(int level) {
		this.level = level;
	}
	
	/**
	 * Gets the transaction isolation integer represented by this {@code IsolationLevel}
	 * 
	 * @return the transaction isolation integer
	 */
	public int getLevel() {
		return level;
	}
	
}
