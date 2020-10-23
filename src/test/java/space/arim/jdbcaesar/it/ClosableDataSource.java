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

import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

class ClosableDataSource extends DriverDataSource implements CloseableResource {
	
	private final AutoCloseable closable;
	
	ClosableDataSource(Vendor vendor, String jdbcUrl, AutoCloseable closable) {
		super(vendor, jdbcUrl);
		this.closable = closable;
	}
	
	ClosableDataSource(Vendor vendor, String jdbcUrl, String username, String password, AutoCloseable closable) {
		super(vendor, jdbcUrl, username, password);
		this.closable = closable;
	}

	@Override
	public void close() throws Exception {
		closable.close();
	}
	
}
