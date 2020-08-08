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

/**
 * Initial builder for specifying transaction properties
 * 
 * @author A248
 *
 */
public interface InitialTransactionBuilder {

	/**
	 * Sets the isolation level of this transaction to the specified one (optional operation).
	 * The default isolation used is {@link IsolationLevel#REPEATABLE_READ}.
	 * 
	 * @param isolation the transaction isolation level
	 * @return this builder
	 */
	InitialTransactionBuilder isolation(IsolationLevel isolation);
	
	/**
	 * Sets whether this transaction is read only (optional operation). By default this is {@code false}.
	 * 
	 * @param readOnly whether the transaction is read only
	 * @return this builder
	 */
	InitialTransactionBuilder readOnly(boolean readOnly);
	
	/**
	 * Sets the body of this transaction to the specified {@link Transactor}
	 * 
	 * @param <T> the result type of the whole transaction
	 * @param transactor the main body of the transaction
	 * @return a transaction builder
	 */
	<T> TransactionBuilder<T> transactor(Transactor<T> transactor);
	
}
