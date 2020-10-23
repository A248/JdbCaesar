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
package space.arim.jdbcaesar.it;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

class DriverDataSource extends IdentifiedDataSource {

	private final String jdbcUrl;
	private final String username;
	private final String password;
	private final boolean credentials;
	
	DriverDataSource(Vendor vendor, String jdbcUrl, String username, String password) {
		this(vendor, jdbcUrl, username, password, true);
	}
	
	DriverDataSource(Vendor vendor, String jdbcUrl) {
		this(vendor, jdbcUrl, null, null, false);
	}
	
	private DriverDataSource(Vendor vendor, String jdbcUrl, String username, String password,
			boolean credentials) {
		super(vendor);
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
		this.credentials = credentials;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		if (credentials) {
			return DriverManager.getConnection(jdbcUrl, username, password);
		} else {
			return DriverManager.getConnection(jdbcUrl);
		}
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException("Not a wrapper");
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
	
}
