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

import space.arim.jdbcaesar.query.QueryBuilder;

/**
 * A source of new queries
 * 
 * @author A248
 *
 * @param <B> the query builder type
 */
public interface QuerySource<B extends QueryBuilder<B>> {

	/**
	 * Begins creating a query
	 * 
	 * @param statement the query statement string
	 * @return an initial query builder
	 */
	B query(String statement);
	
}
