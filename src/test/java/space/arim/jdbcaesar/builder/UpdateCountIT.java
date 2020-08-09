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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import space.arim.jdbcaesar.mapper.UpdateCountMapper;

public class UpdateCountIT extends JdbCaesarImplIT {

	@Test
	public void testUpdateCounts() {
		jdbCaesar().query(
				"CREATE TABLE update_count ("
				+ "col1 INT NOT NULL, "
				+ "col2 BOOLEAN NOT NULL)")
				.voidResult().execute();

		UpdateCountMapper<Integer> identity = (updateCount) -> updateCount;

		Integer count1 = jdbCaesar().query("INSERT INTO update_count (col1, col2) VALUES (?, ?)")
				.params(5, false)
				.updateCount(identity)
				.execute();
		assertNotNull(count1);
		assertEquals(1, count1.intValue());

		Integer count2 = jdbCaesar().query("INSERT INTO update_count (col1, col2) VALUES (?, ?)")
				.params(18, true)
				.updateCount(identity)
				.execute();
		assertNotNull(count2);
		assertEquals(1, count2.intValue());

		Integer count3 = jdbCaesar().query("DELETE FROM update_count")
				.updateCount(identity)
				.execute();
		assertNotNull(count3);
		assertEquals(2, count3.intValue());
	}
	
}
