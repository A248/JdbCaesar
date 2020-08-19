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
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import space.arim.jdbcaesar.ConnectionSource;
import space.arim.jdbcaesar.JdbCaesar;

public abstract class JdbCaesarImplIT {

	private static JdbCaesar jdbCaesar;
	
	@BeforeAll
	public static void setup() {
		jdbCaesar = new JdbCaesarBuilder().connectionSource(new ConnectionSource() {

			@Override
			public Connection getConnection() throws SQLException {
				return DriverManager.getConnection("jdbc:hsqldb:mem:test;sql.syntax_mys=true", "SA", "");
			}

			@Override
			public void close() throws SQLException {
				
			}
			
		}).exceptionHandler(Assertions::fail).build();
	}
	
	protected JdbCaesar jdbCaesar() {
		return jdbCaesar;
	}
	
}
