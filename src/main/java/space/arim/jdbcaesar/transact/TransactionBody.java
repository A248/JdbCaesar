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
package space.arim.jdbcaesar.transact;

import java.sql.SQLException;

/**
 * Body of a transaction
 * 
 * @author A248
 *
 * @param <T> the result type of the whole transaction
 */
@FunctionalInterface
public interface TransactionBody<T> {

	/**
	 * Completes the transaction. All queries should be executed using the given {@link TransactionQuerySource}. <br>
	 * <br>
	 * The {@link TransactionController} may be used to control rollbacks and savepoints. The transaction is committed
	 * once this method returns normally. <br>
	 * <br>
	 * If any query run within the transaction encounters a {@link SQLException}, the transaction will be rolled back
	 * and the global error handler invoked. <br>
	 * 
	 * @param querySource the query source which should be used
	 * @param controller the transaction controller used to rollback
	 * @return the result of the entire transaction
	 * @throws SQLException generally, per JDBC
	 */
	T transact(TransactionQuerySource querySource, TransactionController controller) throws SQLException;
	
}
