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
package space.arim.jdbcaesar.query;

import java.sql.SQLException;

import space.arim.jdbcaesar.error.SubstituteProvider;

/**
 * Builder of single query results
 * 
 * @author A248
 *
 * @param <R> the result type
 */
public interface SingleResultBuilder<R> extends QueryResultBuilder<R> {

	/**
	 * Sets the substitute provider should a {@link SQLException} occur while executing
	 * this query, and returns an executable {@link SingleResult}. <br>
	 * <br>
	 * Within a transaction, this method is useless, as {@code SQLException}s rollback
	 * the entire transaction and cause the transaction to return <i>its</i> substitute result.
	 * 
	 */
	@Override
	SingleResult<R> onError(SubstituteProvider<R> onError);
	
	/**
	 * Executes the query using the default substitute provider. If a {@link SQLException} occurs,
	 * {@code null} is returned. Otherwise, the mapped result is returned. <br>
	 * <br>
	 * This method is otherwise equivalent to <pre>{@code this.onError(() -> null).execute()}</pre>
	 * 
	 * @return the mapped result or {@code null}
	 */
	@Override
	R execute();
	
}
