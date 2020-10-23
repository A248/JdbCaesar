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
package space.arim.jdbcaesar.internal;

import space.arim.jdbcaesar.internal.query.QueryBuilderImpl;
import space.arim.jdbcaesar.internal.transact.MutableTransactionSettings;
import space.arim.jdbcaesar.query.SingleQueryBuilder;
import space.arim.jdbcaesar.transact.IsolationLevel;

class SingleQueryBuilderImpl extends QueryBuilderImpl<SingleQueryBuilder>
		implements SingleQueryBuilder {

	private final MutableTransactionSettings settings;

	SingleQueryBuilderImpl(QueryExecutor<SingleQueryBuilder> executor,
			String statement, PropertiesImpl properties) {
		super(executor, statement, properties);
		settings = properties.deriveTransactionSettings();
	}
	
	MutableTransactionSettings getSettings() {
		return settings;
	}

	@Override
	public SingleQueryBuilder isolation(IsolationLevel isolation) {
		settings.setIsolation(isolation);
		return this;
	}

	@Override
	public SingleQueryBuilder readOnly(boolean readOnly) {
		settings.setReadOnly(readOnly);
		return this;
	}

}
