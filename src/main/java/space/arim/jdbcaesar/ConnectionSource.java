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

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A source of database connections, similar to {@code javax.sql.DataSource} but simpler
 * in its specifications for implementers.
 * 
 * @author A248
 *
 */
public interface ConnectionSource {

	/**
	 * Obtains an open connection. When no longer needed, the connection will be closed
	 * by the caller.
	 * 
	 * @return an open connection
	 * @throws SQLException generally, per JDBC
	 */
	Connection getConnection() throws SQLException;
	
	/**
	 * Closes this connection source
	 * 
	 * @throws SQLException generally, per JDBC
	 */
	void close() throws SQLException;
	
}
