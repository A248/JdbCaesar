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
package space.arim.jdbcaesar.it.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import space.arim.jdbcaesar.JdbCaesar;
import space.arim.jdbcaesar.assimilate.AssimilatedQuerySource;
import space.arim.jdbcaesar.it.JdbCaesarProvider;

public class AssimilationIT {
	
	@ParameterizedTest
	@ArgumentsSource(JdbCaesarProvider.class)
	public void testAssimilate(JdbCaesar jdbCaesar) throws SQLException {
		DataSource dataSource = jdbCaesar.getProperties().getDataSource();
		try (Connection connection = dataSource.getConnection()) {
			connection.setAutoCommit(false);

			AssimilatedQuerySource querySource = jdbCaesar.assimilate(connection);
			querySource.query("CREATE TABLE assimilate (data VARCHAR(32) NOT NULL)").voidResult().execute();
			querySource.query("INSERT INTO assimilate (data) VALUES (?)").params("mystring").voidResult().execute();
			querySource.query("INSERT INTO assimilate (data) VALUES (?)").params("anotherstring").voidResult().execute();

			connection.commit();
		}
		try (Connection connection = dataSource.getConnection()) {

			AssimilatedQuerySource querySource = jdbCaesar.assimilate(connection);
			Set<String> selected = querySource.query("SELECT * FROM assimilate")
					.setResult((resultSet) -> resultSet.getString("data")).execute();
			assertEquals(Set.of("mystring", "anotherstring"), selected);
		}
	}
	
}
