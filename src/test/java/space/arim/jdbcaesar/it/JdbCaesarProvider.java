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
package space.arim.jdbcaesar.it;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import space.arim.jdbcaesar.JdbCaesarBuilder;
import space.arim.jdbcaesar.transact.IsolationLevel;

public class JdbCaesarProvider implements ArgumentsProvider {
	
	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
		ExtensionContext.Store store = context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL);
		return Arrays.stream(Vendor.values())
				.map((vendor) -> (DataSource) store.getOrComputeIfAbsent(vendor, DataSourceCreator::create))
				.filter(Objects::nonNull)
				.map((dataSource) -> {
					return new JdbCaesarBuilder()
							.dataSource(dataSource).defaultIsolation(IsolationLevel.SERIALIZABLE).build();
				})
				.map(Arguments::of);
	}

}
