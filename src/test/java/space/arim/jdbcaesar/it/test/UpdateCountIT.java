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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import space.arim.jdbcaesar.JdbCaesar;
import space.arim.jdbcaesar.it.JdbCaesarProvider;
import space.arim.jdbcaesar.mapper.UpdateCountMapper;

public class UpdateCountIT {

	@ParameterizedTest
	@ArgumentsSource(JdbCaesarProvider.class)
	public void testUpdateCounts(JdbCaesar jdbCaesar) {
		jdbCaesar.query(
				"CREATE TABLE update_counts ("
				+ "col1 INT NOT NULL, "
				+ "col2 BOOLEAN NOT NULL)")
				.voidResult().execute();

		UpdateCountMapper<Integer> identity = (updateCount) -> updateCount;

		Integer count1 = jdbCaesar.query("INSERT INTO update_counts (col1, col2) VALUES (?, ?)")
				.params(5, false)
				.updateCount(identity)
				.execute();
		assertNotNull(count1);
		assertEquals(1, count1.intValue());

		Integer count2 = jdbCaesar.query("INSERT INTO update_counts (col1, col2) VALUES (?, ?)")
				.params(18, true)
				.updateCount(identity)
				.execute();
		assertNotNull(count2);
		assertEquals(1, count2.intValue());

		Integer count3 = jdbCaesar.query("DELETE FROM update_counts")
				.updateCount(identity)
				.execute();
		assertNotNull(count3);
		assertEquals(2, count3.intValue());
	}
	
}
