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

import space.arim.jdbcaesar.error.SubstituteProvider;

/**
 * Intermediate builder of transactions
 * 
 * @author A248
 *
 * @param <T> the result type of the whole transaction
 */
public interface TransactionBuilder<T> {

	/**
	 * Sets the substitute provider and returns an executable {@link Transaction}. Should a {@link SQLException}
	 * occur while executing this query, the transaction is rolled back and the error substitute provider is invoked.
	 * 
	 * @param onError the substitute provider to be invoked in case of error
	 * @return an executable transaction
	 */
	Transaction<T> onError(SubstituteProvider<T> onError);
	
	/**
	 * Old version of {@code #onError(SubstituteProvider)}, but with a misleading name.
	 * 
	 * @param onError the substitute provider to be invoked in case of error
	 * @return an executable transaction
	 * @deprecated Prefer {@link #onError(SubstituteProvider)}
	 */
	@Deprecated
	default Transaction<T> onRollback(SubstituteProvider<T> onError) {
		return onError(onError);
	}
	
}
