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
 * General interface for results almost complete but requiring some error substitute provider should
 * a {@link SQLException} occur while executing.
 * 
 * @author A248
 *
 * @param <R> the result type
 */
public interface QueryResultBuilder<R> {

	/**
	 * Sets the substitute provider should a {@link SQLException} occur while executing
	 * this query, and returns an executable {@link QueryResult}. <br>
	 * <br>
	 * Within a transaction, this method is useless, as {@code SQLException}s rollback
	 * the entire transaction and cause the transaction to return <i>its</i> substitute result.
	 * 
	 * @param onError the substitute result provider should an error occur
	 * @return a {@link QueryResult}
	 */
	QueryResult<R> onError(SubstituteProvider<R> onError);
	
	/**
	 * Executes the query using the default substitute provider. If a {@link SQLException} occurs,
	 * the substitute provider is invoked and its result returned. Otherwise, the mapped result
	 * is returned. <br>
	 * <br>
	 * For most results, the default substitute provider simply returns {@code null}. For collection
	 * results ({@link ListResultBuilder} and {@link SetResultBuilder}) the default substitute provider
	 * returns an empty immutable collection.
	 * 
	 * @return the mapped result or the substitute result
	 */
	R execute();
	
}
