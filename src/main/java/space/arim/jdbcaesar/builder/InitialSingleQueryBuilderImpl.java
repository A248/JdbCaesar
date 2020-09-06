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

import space.arim.jdbcaesar.adapter.DataTypeAdapter;
import space.arim.jdbcaesar.query.InitialSingleQueryBuilder;
import space.arim.jdbcaesar.transact.IsolationLevel;

class InitialSingleQueryBuilderImpl extends InitialQueryBuilderImpl<InitialSingleQueryBuilder> implements InitialSingleQueryBuilder {

	final MutableTransactionSettings settings;
	
	InitialSingleQueryBuilderImpl(DataTypeAdapter[] adapters, QueryExecutor<InitialSingleQueryBuilder> executor,
			String statement, int defaultFetchSize, IsolationLevel defaultIsolation, boolean defaultReadOnly) {
		super(adapters, executor, statement, defaultFetchSize);
		settings = new MutableTransactionSettings(defaultIsolation, defaultReadOnly);
	}

	@Override
	public InitialSingleQueryBuilder isolation(IsolationLevel isolation) {
		settings.isolation = isolation;
		return this;
	}

	@Override
	public InitialSingleQueryBuilder readOnly(boolean readOnly) {
		settings.readOnly = readOnly;
		return this;
	}

}
