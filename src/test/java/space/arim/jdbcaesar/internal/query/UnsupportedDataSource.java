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

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

final class UnsupportedDataSource implements DataSource {
	
	private UnsupportedOperationException uoe() {
		return new UnsupportedOperationException();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw uoe();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw uoe();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw uoe();
	}

	@Override
	public Connection getConnection() throws SQLException {
		throw uoe();
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		throw uoe();
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		throw uoe();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		throw uoe();
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		throw uoe();
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		throw uoe();
	}

}
