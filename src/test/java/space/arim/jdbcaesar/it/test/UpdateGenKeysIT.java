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
import space.arim.jdbcaesar.it.IdentifiedDataSource;
import space.arim.jdbcaesar.it.JdbCaesarProvider;
import space.arim.jdbcaesar.it.Vendor;

public class UpdateGenKeysIT {

	@ParameterizedTest
	@ArgumentsSource(JdbCaesarProvider.class)
	public void testGenKeys(JdbCaesar jdbCaesar) {
		Vendor vendor = ((IdentifiedDataSource) jdbCaesar.getProperties().getDataSource()).vendor();
		String identity = vendor.getIdentitySpecification();
		jdbCaesar.query(
				"CREATE TABLE update_genkeys ("
				+ "col1 " + identity + ", "
				+ "col2 VARCHAR(255) NOT NULL)")
				.voidResult().execute();

		String col2Value = "Test generated keys";
		UpdateGenKeys update = jdbCaesar.query("INSERT INTO update_genkeys (col2) VALUES (?)")
				.params(col2Value)
				.updateGenKeys((updateCount, genKeys) -> {
					return (genKeys.next()) ? new UpdateGenKeys(updateCount, genKeys.getInt(1)) : null;
				}).execute();
		assertNotNull(update);
		assertEquals(1, update.updateCount);

		PretendPOJO pojo = jdbCaesar.query("SELECT * FROM update_genkeys")
				.singleResult((resultSet) -> new PretendPOJO(resultSet.getInt("col1"), resultSet.getString("col2")))
				.execute();
		assertEquals(update.autoIncrId, pojo.integer());
		assertEquals(col2Value, pojo.string());
	}
	
	private static class UpdateGenKeys {
		final int updateCount;
		final int autoIncrId;
		
		UpdateGenKeys(int updateCount, int autoIncrId) {
			this.updateCount = updateCount;
			this.autoIncrId = autoIncrId;
		}
	}
	
}
