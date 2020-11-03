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
package space.arim.jdbcaesar.internal.assimilate;

import java.sql.Connection;
import java.sql.SQLException;

import space.arim.jdbcaesar.assimilate.AssimilatedQuerySource;
import space.arim.jdbcaesar.error.UncheckedSQLException;
import space.arim.jdbcaesar.internal.query.ConnectionAcceptor;
import space.arim.jdbcaesar.internal.PropertiesImpl;
import space.arim.jdbcaesar.internal.QueryExecutor;
import space.arim.jdbcaesar.assimilate.AssimilatedQueryBuilder;

public class AssimilatedQuerySourceImpl implements AssimilatedQuerySource, QueryExecutor<AssimilatedQueryBuilder> {

	private final PropertiesImpl properties;
	private final Connection connection;
	
	public AssimilatedQuerySourceImpl(PropertiesImpl properties, Connection connection) {
		this.properties = properties;
		this.connection = connection;
	}
	
	@Override
	public AssimilatedQueryBuilder query(String statement) {
		return new AssimilatedQueryBuilderImpl(this, statement, properties);
	}
	
	@Override
	public <R> R execute(ConnectionAcceptor<R> acceptor) {
		try {
			return acceptor.acceptConnection(connection);
		} catch (SQLException ex) {
			throw new UncheckedSQLException(acceptor.getExceptionDetails(), ex);
		}
	}
	
}
