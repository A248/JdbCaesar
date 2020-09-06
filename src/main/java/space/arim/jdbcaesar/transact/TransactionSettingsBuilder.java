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
 * Base builder of transaction settings
 * 
 * @author A248
 *
 * @param <B> the builder type
 */
public interface TransactionSettingsBuilder<B extends TransactionSettingsBuilder<B>> {

	/**
	 * Sets the isolation level of the transaction to the specified one (optional operation).
	 * If unspecified, uses the global transaction isolation. <br>
	 * <br>
	 * This is the equivalent of {@link Connection#setTransactionIsolation(int)}
	 * 
	 * @param isolation the transaction isolation level
	 * @return this builder
	 */
	B isolation(IsolationLevel isolation);
	
	/**
	 * Sets whether this transaction is read only (optional operation). If unspecified,
	 * uses the global read only setting. <br>
	 * <br>
	 * This is the equivalent of {@link Connection#setReadOnly(boolean)}
	 * 
	 * @param readOnly whether the transaction is read only
	 * @return this builder
	 */
	B readOnly(boolean readOnly);
	
}
