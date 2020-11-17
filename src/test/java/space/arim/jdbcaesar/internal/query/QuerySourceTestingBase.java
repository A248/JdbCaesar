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

import space.arim.jdbcaesar.JdbCaesarBuilder;
import space.arim.jdbcaesar.QuerySource;

abstract class QuerySourceTestingBase {

	private final QuerySource<?> querySource;
	
	QuerySourceTestingBase() {
		querySource = new JdbCaesarBuilder()
				// QueryResult#execute() should not be called from this query source
				.dataSource(new UnsupportedDataSource())
				.build();
	}
	
	QueryBuilderImpl<?> query(String statement) {
		return (QueryBuilderImpl<?>) querySource.query(statement);
	}
	
}
