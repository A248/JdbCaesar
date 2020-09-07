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
public interface InitialTransactionBuilder extends TransactionSettingsBuilder<InitialTransactionBuilder> {
	
	/**
	 * Sets the body of this transaction to the specified {@link Transactor}
	 * 
	 * @param <T> the result type of the whole transaction
	 * @param transactor the main body of the transaction
	 * @return a transaction builder
	 * @deprecated Use {@link #body(TransactionBody)}, with its improved approach to rolling back
	 */
	@Deprecated
	<T> TransactionBuilder<T> transactor(Transactor<T> transactor);
	
	/**
	 * Sets the body of this transaction to the specified {@link TransactionBody}
	 * 
	 * @param <T> the result type of the whole transaction
	 * @param body the main body of the transaction
	 * @return a transaction builder
	 */
	<T> TransactionBuilder<T> body(TransactionBody<T> body);
	
}
