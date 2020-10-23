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

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Savepoint;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import space.arim.jdbcaesar.JdbCaesar;
import space.arim.jdbcaesar.QuerySource;
import space.arim.jdbcaesar.it.JdbCaesarProvider;

public class TransactionIT {

	@ParameterizedTest
	@ArgumentsSource(JdbCaesarProvider.class)
	public void testTransactions(JdbCaesar jdbCaesar) {
		jdbCaesar.transaction().body((querySource, controller) -> {
			querySource.query(
					"CREATE TABLE transactions ("
					+ "someNumber INT NOT NULL)")
					.voidResult().execute();
			Savepoint pointOfNothing = controller.savepoint("pointOfNothing");

			querySource.query(
					"INSERT INTO transactions (someNumber) VALUES (?)")
					.params(1)
					.voidResult().execute();
			Savepoint pointOfOne = controller.savepoint("pointOfOne");

			querySource.query("UPDATE transactions SET someNumber = ? WHERE someNumber = ?")
					.params(3, 1)
					.voidResult().execute();

			assertSomeNumberValue(querySource, 3);

			controller.rollbackTo(pointOfOne);

			controller.release(pointOfNothing);

			assertSomeNumberValue(querySource, 1);
			return null;
		}).executeOrGet(() -> fail("This should never happen"));

		jdbCaesar.transaction().body((querySource, controller) -> {
			querySource.query("UPDATE transactions SET someNumber = ? WHERE someNumber = ?")
					.params(15, 1)
					.voidResult().execute();
			assertSomeNumberValue(querySource, 15);

			controller.rollback();
			return null;
		}).executeOrGet(() -> fail("This should never happen"));

		assertSomeNumberValue(jdbCaesar, 1);
	}
	
	private void assertSomeNumberValue(QuerySource<?> querySource, int expected) {
		Integer actual = querySource.query(
				"SELECT someNumber FROM transactions")
				.singleResult((resultSet) -> resultSet.getInt("someNumber"))
				.execute();
		assertNotNull(actual);
		assertEquals(expected, actual.intValue());
	}
	
}
