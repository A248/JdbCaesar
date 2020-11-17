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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class BatchUpdateTest extends QuerySourceTestingBase {

	private final Object[] params1 = new Object[] {"value1-1", "value1-2"};
	private final Object[] params2 = new Object[] {"value2-1", "value2-2"};
	private final Object[][] allParams = new Object[][] {params1, params2};

	private BatchUpdateImpl batch(String statement) {
		return query(statement).batch();
	}

	@Test
	public void testSimpleParameters() {
		BatchUpdateImpl batchUpdate = batch("INSERT INTO table (col1, col2) VALUES (?, ?)");
		batchUpdate.addParams(params1);
		batchUpdate.addParams(params2);
		assertArrayEquals(allParams, batchUpdate.getAllArguments().toArray());
	}

	@Test
	public void testNamedParameters() {
		BatchUpdateImpl batchUpdate = batch("INSERT INTO table (col1, col2) VALUES (:param1, :param2)");
		batchUpdate.addParams(Map.of("param1", "value1-1", "param2", "value1-2"));
		batchUpdate.addParams(Map.of("param1", "value2-1", "param2", "value2-2"));
		assertArrayEquals(allParams, batchUpdate.getAllArguments().toArray());
	}

}
