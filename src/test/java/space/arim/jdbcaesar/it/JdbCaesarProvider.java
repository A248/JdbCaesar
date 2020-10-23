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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import space.arim.jdbcaesar.JdbCaesar;
import space.arim.jdbcaesar.JdbCaesarBuilder;

public class JdbCaesarProvider implements ArgumentsProvider {
	
	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
		return Arrays.stream(Vendor.values())
				.map((vendor) -> {
					return (IdentifiedDataSource) context.getRoot().getStore(Namespace.GLOBAL)
							.getOrComputeIfAbsent(vendor, DataSourceCreator::create);
				})
				.filter(Objects::nonNull)
				.map(this::fromSource)
				.map(Arguments::of);
	}
	
	private JdbCaesar fromSource(IdentifiedDataSource source) {
		JdbCaesarBuilder jdbCaesarBuilder = new JdbCaesarBuilder()
				.dataSource(source)
				.exceptionHandler(Assertions::fail)
				.rewrapExceptions(true)
				.defaultIsolation(source.vendor().defaultIsolation());
		return jdbCaesarBuilder.build();
	}

}
