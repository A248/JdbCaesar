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

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import space.arim.jdbcaesar.JdbCaesar;
import space.arim.jdbcaesar.it.JdbCaesarProvider;
import space.arim.jdbcaesar.query.BatchUpdate;

public class BatchUpdateIT {

	@ParameterizedTest
	@ArgumentsSource(JdbCaesarProvider.class)
	public void testBatchUpdateSimpleParams(JdbCaesar jdbCaesar) {
		assertBatchUpdate(new TestInstance(jdbCaesar, false));
	}
	
	@ParameterizedTest
	@ArgumentsSource(JdbCaesarProvider.class)
	public void testBatchUpdateNamedParams(JdbCaesar jdbCaesar) {
		assertBatchUpdate(new TestInstance(jdbCaesar, true));
	}
	
	private void assertBatchUpdate(TestInstance testInstance) {
		Set<IntPair> pairs = randomPairs();
		int[] updateCounts = testInstance.performBatchInsert(pairs);
		assertEquals(pairs.size(), updateCounts.length);
		for (int updateCount : updateCounts) {
			assertEquals(1, updateCount);
		}
		assertEquals(pairs, testInstance.performBatchRetrieve());
	}
	
	private static Set<IntPair> randomPairs() {
		Random random = ThreadLocalRandom.current();
		int count = 5 + random.nextInt(10 + 1);
		Set<IntPair> objects = new HashSet<>(count);
		for (int n = 0; n < count; n++) {
			IntPair pair = new IntPair(random.nextInt(), random.nextInt());
			objects.add(pair);
		}
		return objects;
	}
	
	private static class TestInstance {
		
		private JdbCaesar jdbCaesar;
		private boolean named;
		
		TestInstance(JdbCaesar jdbCaesar, boolean named) {
			this.jdbCaesar = jdbCaesar;
			this.named = named;
		}
		
		private String tableName() {
			return (named) ? "batch_update_named" : "batch_update_simple";
		}
		
		int[] performBatchInsert(Set<IntPair> pairs) {
			String tableName = tableName();
			jdbCaesar.query(
					"CREATE TABLE " + tableName + " (value1 INT NOT NULL, value2 INT NOT NULL)")
					.voidResult().execute();

			BatchUpdate batchUpdate = jdbCaesar.query(
					"INSERT INTO " + tableName + " (value1, value2) VALUES (" + ((named) ? ":value1, :value2" : "?, ?") + ")")
					.batch();
			for (IntPair pair : pairs) {
				if (named) {
					batchUpdate.addParams(Map.of("value1", pair.value1(), "value2", pair.value2()));
				} else {
					batchUpdate.addParams(pair.value1(), pair.value2());
				}
			}
			return batchUpdate.readyBatch().execute();
		}
		
		Set<IntPair> performBatchRetrieve() {
			return jdbCaesar.query("SELECT * FROM " + tableName())
					.setResult((resultSet) -> new IntPair(resultSet.getInt("value1"), resultSet.getInt("value2")))
					.execute();
		}

	}
	
}
