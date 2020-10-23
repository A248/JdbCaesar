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
import space.arim.jdbcaesar.query.SingleQueryBuilder;
import space.arim.jdbcaesar.transact.TransactionBuilder;

/**
 * Main interface for JdbCaesar. <br>
 * <br>
 * This interface is immutable and thread safe. However, {@link QueryBuilder}s and
 * {@link TransactionBuilder}s returned are intended to only be used by a single thread.
 * 
 * @author A248
 *
 */
public interface JdbCaesar extends QuerySource<SingleQueryBuilder> {

	/**
	 * Gets the configured properties of this instance. This {@code JdbCaesarProperties}
	 * implementation is immutable.
	 * 
	 * @return the immutable configured properties
	 */
	JdbCaesarProperties getProperties();
	
	/**
	 * Begins creating a single query
	 * 
	 */
	@Override
	SingleQueryBuilder query(String statement);
	
	/**
	 * Begins creating a transaction
	 * 
	 * @return an initial transaction builder
	 */
	TransactionBuilder transaction();
	
}
