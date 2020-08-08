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

import space.arim.jdbcaesar.mapper.ResultElementMapper;
import space.arim.jdbcaesar.mapper.ResultSingleMapper;
import space.arim.jdbcaesar.mapper.UpdateCountMapper;
import space.arim.jdbcaesar.mapper.UpdateGenKeysMapper;

/**
 * Initial builder from which more specific queries may be created.
 * 
 * @author A248
 *
 */
public interface InitialQueryBuilder {

	/**
	 * Sets the parameters of this initial builder to the specified ones (optional operation). <br>
	 * This is the equivalent of {@link java.sql.PreparedStatement#setObject(int, Object)} and
	 * variants thereof.
	 * 
	 * @param params the parameters
	 * @return this builder
	 */
	InitialQueryBuilder params(Object...params);
	
	/**
	 * Sets the fetch size of this initial builder to the specified one (optional operation). <br>
	 * This is the equivalent of {@link java.sql.Statement#setFetchSize(int)}. If the global fetch
	 * size setting is set, it will be overriden for this query.
	 * 
	 * @param fetchSize the fetch size
	 * @return this builder
	 */
	InitialQueryBuilder fetchSize(int fetchSize);
	
	/**
	 * Maps to a single result from a result set. The mapper will be fed the first row available, and the mapped
	 * result returned. If the result set is empty, the mapper is ignored and the result is {@code null}.
	 * 
	 * @param <T> the type of the result
	 * @param mapper the single result mapper
	 * @return a single result builder
	 */
	<T> SingleResultBuilder<T> singleResult(ResultSingleMapper<T> mapper);
	
	/**
	 * Maps to a list result from a result set. The result will be a list populated by the list mapper.
	 * It will be empty if the SQL result set is empty.
	 * 
	 * @param <T> the type of the result
	 * @param mapper the list result mapper
	 * @return a list result builder
	 */
	<T> ListResultBuilder<T> listResult(ResultElementMapper<T> mapper);
	
	/**
	 * Maps to a set result from a result set. The result will be a set populated by the list mapper.
	 * It will be empty if the SQL result set is empty.
	 * 
	 * @param <T> the type of the result
	 * @param mapper the list result mapper
	 * @return a set result builder
	 */
	<T> SetResultBuilder<T> setResult(ResultElementMapper<T> mapper);
	
	/**
	 * Maps to a void result. Any results are ignored.
	 * 
	 * @return a void result
	 */
	VoidResult voidResult();
	
	/**
	 * Maps to a single result from an update count.
	 * 
	 * @param <T> the type of the result
	 * @param mapper the update count mapper
	 * @return a single result builder
	 */
	<T> SingleResultBuilder<T> updateCount(UpdateCountMapper<T> mapper);
	
	/**
	 * Maps to a single result from an update count and generated keys.
	 * 
	 * @param <T> the type of the result
	 * @param mapper the update count and generated keys mapper
	 * @return a single result builder
	 */
	<T> SingleResultBuilder<T> updateGenKeys(UpdateGenKeysMapper<T> mapper);
	
}
