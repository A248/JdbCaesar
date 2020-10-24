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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import space.arim.jdbcaesar.mapper.TotalResultMapper;
import space.arim.jdbcaesar.mapper.ResultElementMapper;
import space.arim.jdbcaesar.mapper.ResultSingleMapper;
import space.arim.jdbcaesar.mapper.UpdateCountMapper;
import space.arim.jdbcaesar.mapper.UpdateGenKeysMapper;

/**
 * Builder of queries used to specify the query parameters, from which more specific queries may be created
 * 
 * @author A248
 *
 * @param <B> the intermediate builder type
 */
public interface QueryBuilder<B extends QueryBuilder<B>> {

	/**
	 * Sets the parameters of the query to be executed to the specified ones (optional operation). <br>
	 * This is the equivalent of {@link java.sql.PreparedStatement#setObject(int, Object)} and
	 * variants thereof.
	 * 
	 * @param parameters the parameters
	 * @return the intermediate builder
	 */
	B params(Object...parameters);
	
	/**
	 * Sets the fetch size of this initial builder to the specified one (optional operation). <br>
	 * This is the equivalent of {@link java.sql.Statement#setFetchSize(int)}. If the global fetch
	 * size setting is set, it will be overriden for this query.
	 * 
	 * @param fetchSize the fetch size
	 * @return this builder
	 */
	B fetchSize(int fetchSize);
	
	/**
	 * Sets the result set type of the statement to be executed to the specified result set type.
	 * If a mapper not involving a result set is used, this setting is ignored. <br>
	 * <br>
	 * This is the equivalent of the {@code resultSetType} parameter in
	 * {@link java.sql.Connection#prepareStatement(String, int, int)}
	 * 
	 * @param resultSetType the result set type
	 * @return this builder
	 */
	B resultSetType(ResultSetType resultSetType);
	
	/**
	 * Sets the result set concurrency of the statement to be executed to the specified concurrency.
	 * If a mapper not involving a result set is used, this setting is ignored. <br>
	 * <br>
	 * This is the equivalent of the {@code resultSetConcurrency} parameter in
	 * {@link java.sql.Connection#prepareStatement(String, int, int)}
	 * 
	 * @param concurrency the result set concurrency
	 * @return this builder
	 */
	B resultSetConcurrency(ResultSetConcurrency concurrency);
	
	/**
	 * Maps to a single object from a result set. <br>
	 * <br>
	 * The mapper will be fed the first row available, and the mapped result returned.
	 * If no rows are fetched, the result is {@code null}.
	 * 
	 * @param <R> the type of the result
	 * @param mapper the single result mapper
	 * @return an executable query
	 */
	default <R> QueryResult<R> singleResult(ResultSingleMapper<R> mapper) {
		return totalResult((resultSet) -> {
			if (resultSet.next()) {
				return mapper.mapValueFrom(resultSet);
			} else {
				return null;
			}
		});
	}
	
	/**
	 * Maps to a list result from a result set. The result will be a list populated by the list mapper.
	 * It will be empty if the SQL result set is empty.
	 * 
	 * @param <E> the element type of the result
	 * @param mapper the list result mapper
	 * @return an executable query
	 */
	default <E> QueryResult<List<E>> listResult(ResultElementMapper<E> mapper) {
		return totalResult((resultSet) -> {
			List<E> result = new ArrayList<>();
			while (resultSet.next()) {
				result.add(mapper.mapElementFrom(resultSet));
			}
			return result;
		});
	}
	
	/**
	 * Maps to a set result from a result set. The result will be a set populated by the list mapper.
	 * It will be empty if the SQL result set is empty.
	 * 
	 * @param <E> the element type of the result
	 * @param mapper the list result mapper
	 * @return an executable query
	 */
	default <E> QueryResult<Set<E>> setResult(ResultElementMapper<E> mapper) {
		return totalResult((resultSet) -> {
			Set<E> result = new HashSet<>();
			while (resultSet.next()) {
				result.add(mapper.mapElementFrom(resultSet));
			}
			return result;
		});
	}
	
	/**
	 * Maps a result from an entire result set. The cursor of the result set fed to the mapper
	 * will be initially positioned before the first row. <br>
	 * <br>
	 * This method is appropriate when mapping all the results of a result set to a single
	 * object.
	 * 
	 * @param <R> the type of the result
	 * @param mapper the total result mapper
	 * @return an executable query
	 */
	<R> QueryResult<R> totalResult(TotalResultMapper<R> mapper);
	
	/**
	 * Maps to a void result
	 * 
	 * @return a void result
	 */
	QueryResult<Void> voidResult();
	
	/**
	 * Maps to a result from an update count.
	 * 
	 * @param <R> the type of the result
	 * @param mapper the update count mapper
	 * @return an executable query
	 */
	<R> QueryResult<R> updateCount(UpdateCountMapper<R> mapper);
	
	/**
	 * Maps to an update count result
	 * 
	 * @return an executable query yielding the update count
	 */
	default QueryResult<Integer> updateCount() {
		return updateCount(UpdateCountMapper.identity());
	}
	
	/**
	 * Maps to a result from an update count and generated keys.
	 * 
	 * @param <R> the type of the result
	 * @param mapper the update count and generated keys mapper
	 * @return an executable query
	 */
	<R> QueryResult<R> updateGenKeys(UpdateGenKeysMapper<R> mapper);
	
}
