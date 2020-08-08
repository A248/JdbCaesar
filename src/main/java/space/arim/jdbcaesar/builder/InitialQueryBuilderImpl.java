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
import space.arim.jdbcaesar.mapper.CombinedResultMapper;
import space.arim.jdbcaesar.mapper.ResultElementMapper;
import space.arim.jdbcaesar.mapper.ResultSingleMapper;
import space.arim.jdbcaesar.mapper.UpdateCountMapper;
import space.arim.jdbcaesar.mapper.UpdateGenKeysMapper;
import space.arim.jdbcaesar.query.InitialQueryBuilder;
import space.arim.jdbcaesar.query.ListResultBuilder;
import space.arim.jdbcaesar.query.QueryResultBuilder;
import space.arim.jdbcaesar.query.SetResultBuilder;
import space.arim.jdbcaesar.query.SingleResultBuilder;
import space.arim.jdbcaesar.query.VoidResult;

class InitialQueryBuilderImpl implements InitialQueryBuilder {

	final DataTypeAdapter[] adapters;
	final QueryExecutor executor;
	final String statement;
	Object[] params = EMPTY_PARAMS;
	int fetchSize;
	
	private static final Object[] EMPTY_PARAMS = new Object[] {};
	
	InitialQueryBuilderImpl(DataTypeAdapter[] adapters, QueryExecutor executor,
			String statement, int defaultFetchSize) {
		this.adapters = adapters;
		this.executor = executor;
		this.statement = statement;
		fetchSize = defaultFetchSize;
	}
	
	@Override
	public InitialQueryBuilder params(Object...params) {
		for (int n = 0; n < params.length; n++) {
			params[n] = adapt(params[n]);
		}
		this.params = params;
		return this;
	}
	
	private Object adapt(Object param) {
		for (DataTypeAdapter adapter : adapters) {
			Object result = adapter.adaptObject(param);
			if (result != param) {
				return result;
			}
		}
		return param;
	}
	
	@Override
	public InitialQueryBuilder fetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
		return this;
	}

	@Override
	public <T> SingleResultBuilder<T> singleResult(ResultSingleMapper<T> mapper) {
		return new SingleResultBuilderImpl<>(this, mapper);
	}
	
	@Override
	public <T> ListResultBuilder<T> listResult(ResultElementMapper<T> mapper) {
		return new ListResultBuilderImpl<>(this, mapper);
	}
	
	@Override
	public <T> SetResultBuilder<T> setResult(ResultElementMapper<T> mapper) {
		return new SetResultBuilderImpl<>(this, mapper);
	}
	
	@Override
	public <T> QueryResultBuilder<T> combinedResult(CombinedResultMapper<T> mapper) {
		return new CombinedResultBuilderImpl<>(this, mapper);
	}
	
	@Override
	public VoidResult voidResult() {
		return new VoidResultImpl(this);
	}

	@Override
	public <T> QueryResultBuilder<T> updateCount(UpdateCountMapper<T> mapper) {
		return new UpdateCountResultBuilderImpl<>(this, mapper);
	}

	@Override
	public <T> QueryResultBuilder<T> updateGenKeys(UpdateGenKeysMapper<T> mapper) {
		return new UpdateGenKeysResultBuilderImpl<>(this, mapper);
	}
	
}
