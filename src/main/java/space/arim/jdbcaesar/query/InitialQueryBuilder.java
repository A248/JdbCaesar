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
import space.arim.jdbcaesar.mapper.CombinedResultMapper;
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
	 * Sets the result set type of the statement to be executed to the specified result set type.
	 * If a mapper not involving a result set is used, this setting is ignored. <Br>
	 * This is the equivalent of the {@code resultSetType} parameter in
	 * {@link java.sql.Connection#prepareStatement(String, int, int)}
	 * 
	 * @param resultSetType the result set type
	 * @return this builder
	 */
	InitialQueryBuilder resultSetType(ResultSetType resultSetType);
	
	/**
	 * Sets the result set concurrency of the statement to be executed to the specified concurrency.
	 * If a mapper not involving a result set is used, this setting is ignored. <Br>
	 * This is the equivalent of the {@code resultSetConcurrency} parameter in
	 * {@link java.sql.Connection#prepareStatement(String, int, int)}
	 * 
	 * @param concurrency the result set concurrency
	 * @return this builder
	 */
	InitialQueryBuilder resultSetConcurrency(ResultSetConcurrency concurrency);
	
	/**
	 * Maps to a single object from a result set. <br>
	 * <br>
	 * The mapper will be fed the first row available, and the mapped result returned.
	 * If the result set is empty, the mapper is ignored and the result is {@code null}.
	 * 
	 * @param <R> the type of the result
	 * @param mapper the single result mapper
	 * @return a single result builder
	 */
	<R> SingleResultBuilder<R> singleResult(ResultSingleMapper<R> mapper);
	
	/**
	 * Maps to a list result from a result set. The result will be a list populated by the list mapper.
	 * It will be empty if the SQL result set is empty.
	 * 
	 * @param <E> the element type of the result
	 * @param mapper the list result mapper
	 * @return a list result builder
	 */
	<E> ListResultBuilder<E> listResult(ResultElementMapper<E> mapper);
	
	/**
	 * Maps to a set result from a result set. The result will be a set populated by the list mapper.
	 * It will be empty if the SQL result set is empty.
	 * 
	 * @param <E> the element type of the result
	 * @param mapper the list result mapper
	 * @return a set result builder
	 */
	<E> SetResultBuilder<E> setResult(ResultElementMapper<E> mapper);
	
	/**
	 * Maps a result from an entire result set. The cursor of the result set fed to the mapper
	 * will be initially positioned before the first row. <br>
	 * <br>
	 * This method is appropriate when mapping all the results of a result set to a single
	 * object.
	 * 
	 * @param <R> the type of the result
	 * @param mapper the combined result mapper
	 * @return a result builder
	 */
	<R> QueryResultBuilder<R> combinedResult(CombinedResultMapper<R> mapper);
	
	/**
	 * Maps to a void result. Any results are ignored.
	 * 
	 * @return a void result
	 */
	VoidResult voidResult();
	
	/**
	 * Maps to a result from an update count.
	 * 
	 * @param <R> the type of the result
	 * @param mapper the update count mapper
	 * @return a result builder
	 */
	<R> QueryResultBuilder<R> updateCount(UpdateCountMapper<R> mapper);
	
	/**
	 * Maps to a result from an update count and generated keys.
	 * 
	 * @param <R> the type of the result
	 * @param mapper the update count and generated keys mapper
	 * @return a result builder
	 */
	<R> QueryResultBuilder<R> updateGenKeys(UpdateGenKeysMapper<R> mapper);
	
}
