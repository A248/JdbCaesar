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
package space.arim.jdbcaesar.adapter;

import java.sql.PreparedStatement;

import space.arim.jdbcaesar.query.QueryBuilder;

/**
 * An adapter to simplify converting Java objects to data in the database. <br>
 * <br>
 * Data type adapters are responsible for adapting the objects passed to {@link QueryBuilder#params(Object...)}
 * to the object which will be passed to the database via {@link PreparedStatement#setObject(int, Object)}. <br>
 * <br>
 * For each parameter passed to {@link QueryBuilder#params(Object...)}, the adapters configured
 * in the construction of JdbCaesar are called sequentially. As soon as the object returned by an adapter
 * is not referentially equal to the original object passed to the adapter, no more adapters are invoked
 * and the result of the adapter is used in {@link PreparedStatement#setObject(int, Object)}. <br>
 * <br>
 * Since implementations are called for <i>all</i> parameters, they should follow the pattern of checking
 * instanceof before returning a different result, like so: <br>
 * <pre>{@code (obj) -> (obj instanceof TargetType) ? ((TargetType) obj).getDatabaseRepresentation() : obj);}</pre> <br>
 * <br>
 * Additionally, implementations should be stateless and thread safe.
 * 
 * @author A248
 *
 */
@FunctionalInterface
public interface DataTypeAdapter {

	/**
	 * Adapts an object passed to {@link QueryBuilder#params(Object...)} to the object
	 * which will be passed to {@link PreparedStatement#setObject(int, Object)}. <br>
	 * <br>
	 * See the class javadoc for the established approach for implementing this method.
	 * 
	 * @param parameter the parameter passed to JdbCaesar
	 * @return the object to pass the prepared statement, or {@code parameter} if this adapter is indifferent
	 */
	Object adaptObject(Object parameter);
	
}
