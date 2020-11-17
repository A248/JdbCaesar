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

import java.sql.Statement;
import java.util.Map;

/**
 * A builder of batch updates. <br>
 * <br>
 * Note that instances of this interface are not implemented "live" on a PreparedStatement,
 * but must retain their own list of parameters. Where memory usage is absolutely critical,
 * raw JDBC should be preferred.
 * 
 * @author A248
 *
 */
public interface BatchUpdate {

	/**
	 * Adds the specified batch parameter arguments to this batch update
	 * 
	 * @param arguments the batch parameters
	 * @return this batch update
	 * @throws IllegalArgumentException if the length of the arguments do not match
	 *                                  that in previous calls to this method
	 */
	BatchUpdate addParams(Object... arguments);

	/**
	 * Adds the specified batch parameter arguments to this batch update <br>
	 * <br>
	 * Named parameters take the same form described in
	 * {@link QueryBuilder#params(Map)}
	 * 
	 * @param arguments the map of batch parameters
	 * @return this batch update
	 * @throws IllegalArgumentException if the named parameters do not match those
	 *                                  in the statement (best practical effort)
	 */
	BatchUpdate addParams(Map<String, Object> arguments);
	
	/**
	 * Gets a query result which executes this batch update via
	 * {@link Statement#executeBatch()}. <br>
	 * <br>
	 * If no batch arguments have been added to this batch update,
	 * {@code Statement.executeBatch} is <i>not</i> called and an empty update count
	 * array will be returned. This is to help avoid corner cases where some JDBC drivers
	 * do not permit empty batch updates.
	 * 
	 * @return a query result yielding the update count array
	 */
	QueryResult<int[]> readyBatch();

	/**
	 * Gets a query result which executes this batch update via
	 * {@link Statement#executeLargeBatch()}. <br>
	 * <br>
	 * If no batch arguments have been added to this batch update,
	 * {@code Statement.executeLargeBatch} is <i>not</i> called and an empty update count
	 * array will be returned. This is to help avoid corner cases where some JDBC drivers
	 * do not permit empty batch updates.
	 * 
	 * @return a query result yielding the large update count array
	 */
	QueryResult<long[]> readyLargeBatch();
	
}
