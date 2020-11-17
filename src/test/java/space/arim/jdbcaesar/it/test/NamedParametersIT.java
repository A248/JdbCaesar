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

import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import space.arim.jdbcaesar.JdbCaesar;
import space.arim.jdbcaesar.it.JdbCaesarProvider;

public class NamedParametersIT {

	@ParameterizedTest
	@ArgumentsSource(JdbCaesarProvider.class)
	public void testNamedParameters(JdbCaesar jdbCaesar) {
		jdbCaesar.query(
				"CREATE TABLE named_parameters (param1 INT NOT NULL, param2 VARCHAR(32) NOT NULL)")
				.voidResult().execute();

		String param2Value = "str";
		jdbCaesar.query(
				"INSERT INTO named_parameters (param1, param2) VALUES (:param1, :param2)")
				.params(Map.of("param1", 5, "param2", param2Value))
				.voidResult().execute();
		String selectedValue = jdbCaesar.query(
				"SELECT param2 FROM named_parameters WHERE param1 = :param1")
				.params(Map.of("param1", 5))
				.singleResult((resultSet) -> resultSet.getString("param2"))
				.execute();
		assertEquals(param2Value, selectedValue);
	}
	
}
