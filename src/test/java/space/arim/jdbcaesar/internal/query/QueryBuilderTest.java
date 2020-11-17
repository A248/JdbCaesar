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

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QueryBuilderTest extends QuerySourceTestingBase {

	@Test
	public void testSimpleParamsNoAdapters() {
		QueryBuilderImpl<?> queryBuilder = query("SELECT * FROM table");

		Object[] arguments = new Object[] {1, "str", false};
		queryBuilder.params(arguments);
		assertArrayEquals(arguments, queryBuilder.getArguments());
	}
	
	@Test
	public void testCorrectNamedParameters() {
		QueryBuilderImpl<?> queryBuilder;
		try {
			queryBuilder = query(
					"SELECT * FROM table WHERE value1 = :param1 AND value2 = :param2 AND value3 = :param3");
			queryBuilder.params(Map.of("param1", 1, "param2", "str", "param3", false));
		} catch (IllegalArgumentException ex) {
			throw Assertions.<RuntimeException>fail("False positive detecting mismatched named parameters", ex);
		}
		assertEquals("SELECT * FROM table WHERE value1 = ? AND value2 = ? AND value3 = ?", queryBuilder.getStatement());
		assertArrayEquals(new Object[] {1, "str", false}, queryBuilder.getArguments());
	}
	
	@Test
	public void testNamedParametersExtraArguments() {
		QueryBuilderImpl<?> queryBuilder = query(
				"SELECT * FROM table WHERE value1 = :param1 AND value2 = :param2 AND value3 = :param3");
		try {
			queryBuilder.params(Map.of("param1", 1, "param2", "str", "param3", false, "param4", "nonexistent"));
			fail("Parser should have detected extra arguments");
		} catch (IllegalArgumentException expected) {}
	}
	
	@Test
	public void testNamedParametersInsufficientArguments() {
		QueryBuilderImpl<?> queryBuilder = query(
				"SELECT * FROM table WHERE value1 = :param1 AND value2 = :param2 AND value3 = :param3 AND value4 = :param4");
		try {
			queryBuilder.params(Map.of("param1", 1, "param2", "str", "param3", false));
			fail("Parser should have detected insufficient arguments");
		} catch (IllegalArgumentException expected) {}
	}
	
}
