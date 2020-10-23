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
package space.arim.jdbcaesar.internal.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public abstract class ConnectionAcceptor<R> {
	
	private final QueryBuilderImpl<?> initialBuilder;
	
	ConnectionAcceptor(QueryBuilderImpl<?> initialBuilder) {
		this.initialBuilder = initialBuilder;
	}

	public QueryBuilderImpl<?> getInitialBuilder() {
		return initialBuilder;
	}

	public abstract R acceptConnection(Connection conn) throws SQLException;
	
	public SQLException rewrapExceptionWithDetails(SQLException ex) {
		return new SQLException(
				"For statement [" + initialBuilder.getStatement() + "], "
						+ "parameters " + Arrays.deepToString(initialBuilder.getParameters()),
				ex);
	}
	
}
