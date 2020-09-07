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
import java.sql.Savepoint;

import java.sql.Connection;

/**
 * Stateful interface for manipulating rollbacks and savepoints
 * 
 * @author A248
 *
 */
public interface TransactionController {

	/**
	 * Creates a save point with the given name. Equivalent of {@link Connection#setSavepoint(String)}
	 * 
	 * @param name the name of the save point
	 * @return the save point
	 * @throws SQLException generally, per JDBC
	 */
	Savepoint savepoint(String name) throws SQLException;
	
	/**
	 * Create an unnamed save point. Equivalent of {@link Connection#setSavepoint()}
	 * 
	 * @return the save point
	 * @throws SQLException generally, per JDBC
	 */
	Savepoint savepoint() throws SQLException;
	
	/**
	 * Releases the specified save point and subsequent save points. Equivalent of {@link Connection#releaseSavepoint(Savepoint)}
	 * 
	 * @param savepoint the save point
	 * @throws SQLException generally, per JDBC
	 */
	void release(Savepoint savepoint) throws SQLException;
	
	/**
	 * Rolls back to the specified save point. Equivalent of {@link Connection#rollback(Savepoint)}
	 * 
	 * @param savepoint the save point
	 * @throws SQLException generally, per JDBC
	 */
	void rollbackTo(Savepoint savepoint) throws SQLException;
	
	/**
	 * Rolls back any changes made since the start of the transaction. Equivalent of {@link Connection#rollback()}
	 * 
	 * @throws SQLException generally, per JDBC
	 */
	void rollback() throws SQLException;
	
}
