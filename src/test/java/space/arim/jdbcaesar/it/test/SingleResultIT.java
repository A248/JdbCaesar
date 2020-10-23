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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import space.arim.jdbcaesar.JdbCaesar;
import space.arim.jdbcaesar.it.JdbCaesarProvider;

public class SingleResultIT {

	@ParameterizedTest
	@ArgumentsSource(JdbCaesarProvider.class)
	public void testSingleResult(JdbCaesar jdbCaesar) {
		jdbCaesar.query(
				"CREATE TABLE single_res ("
				+ "integer_value INT NOT NULL, "
				+ "string_value VARCHAR(255) NOT NULL)")
		.voidResult().execute();

		Random random = ThreadLocalRandom.current();
		int integer = random.nextInt();
		String string = UUID.randomUUID().toString().substring(random.nextInt(4), 32);

		jdbCaesar.query(
				"INSERT INTO single_res (integer_value, string_value) VALUES (?, ?)")
				.params(integer, string).voidResult().execute();
		PretendPOJO result = jdbCaesar.query(
				"SELECT * FROM single_res")
				.singleResult((rs) -> new PretendPOJO(rs.getInt("integer_value"), rs.getString("string_value")))
				.execute();
		assertNotNull(result);
		assertEquals(integer, result.integer());
		assertEquals(string, result.string());
	}
	
}
