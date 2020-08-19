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

import space.arim.jdbcaesar.query.InitialQueryBuilder;
import space.arim.jdbcaesar.transact.InitialTransactionBuilder;

/**
 * Main interface for JdbCaesar. <br>
 * <br>
 * This interface is immutable and thread safe. However, {@link InitialQueryBuilder}s and
 * {@link InitialTransactionBuilder}s returned are intended to only be used by a single thread. <br>
 * <br>
 * When no longer needed, the {@link ConnectionSource} used to create this instance should be closed.
 * This may be done with {@code getConnectionSource().close()}
 * 
 * @author A248
 *
 */
public interface JdbCaesar extends JdbCaesarInfo, QuerySource {

	@Override
	InitialQueryBuilder query(String statement);
	
	/**
	 * Begins creating a transaction
	 * 
	 * @return an initial transaction builder
	 */
	InitialTransactionBuilder transaction();
	
}
