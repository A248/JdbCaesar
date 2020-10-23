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
package space.arim.jdbcaesar;

import java.sql.SQLException;

import space.arim.jdbcaesar.error.SubstituteProvider;
import space.arim.jdbcaesar.error.UncheckedSQLException;

/**
 * Some query or transaction which may be executed to return a result
 * 
 * @author A248
 *
 * @param <R> the type of the result
 */
public interface ExecutableSQL<R> {

	/**
	 * Alternative method to execute which uses a substitute provider should a
	 * {@link SQLException} occur while executing. <br>
	 * <br>
	 * If all proceeds normally, this method is equivalent to {@link #execute()}.
	 * Otherwise, if a {@code SQLException} is encountered, first the global error
	 * handler is called, then the {@code onError} substitute provider is invoked
	 * and its result returned. <br>
	 * <br>
	 * <i>This method can be used for single queries and transactions, but is highly
	 * discouraged for queries within a transaction.</i> Instad, it is best to defer
	 * error handling to the transaction itself.
	 * 
	 * @param onError the substitute result provider should an error occur
	 * @return the result
	 * @throws IllegalArgumentException optional, if this implementation does not
	 *                                  support this method
	 */
	R executeOrGet(SubstituteProvider<R> onError);
	
	/**
	 * Executes and gets the result
	 * 
	 * @return the result
	 * @throws UncheckedSQLException if query or queries threw a {@code SQLException}
	 */
	R execute();
	
}
