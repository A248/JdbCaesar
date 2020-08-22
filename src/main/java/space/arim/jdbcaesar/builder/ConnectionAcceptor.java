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
package space.arim.jdbcaesar.builder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

abstract class ConnectionAcceptor {
	
	final InitialQueryBuilderImpl initialBuilder;
	
	ConnectionAcceptor(InitialQueryBuilderImpl initialBuilder) {
		this.initialBuilder = initialBuilder;
	}
	
	boolean readOnly() {
		// False by default, overridden where necessary
		return false;
	}

	abstract void acceptConnection(Connection conn) throws SQLException;
	
	abstract void onError();
	
	SQLException rewrapExceptionWithDetails(SQLException ex) {
		return new SQLException(
				"For statement [" + initialBuilder.statement + "], parameters " + Arrays.deepToString(initialBuilder.params),
				ex);
	}
	
}
