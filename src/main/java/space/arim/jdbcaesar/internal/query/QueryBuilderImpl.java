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

import java.sql.PreparedStatement;
import java.sql.SQLException;

import space.arim.jdbcaesar.internal.PropertiesImpl;
import space.arim.jdbcaesar.internal.QueryExecutor;
import space.arim.jdbcaesar.mapper.TotalResultMapper;
import space.arim.jdbcaesar.mapper.UpdateCountMapper;
import space.arim.jdbcaesar.mapper.UpdateGenKeysMapper;
import space.arim.jdbcaesar.query.QueryBuilder;
import space.arim.jdbcaesar.query.QueryResult;
import space.arim.jdbcaesar.query.ResultSetConcurrency;
import space.arim.jdbcaesar.query.ResultSetType;

public class QueryBuilderImpl<B extends QueryBuilder<B>> implements QueryBuilder<B> {

	private final QueryExecutor<B> executor;
	private final String statement;
	private final PropertiesImpl properties;

	private Object[] parameters = EMPTY_ARRAY;
	private int fetchSize;
	private ResultSetType resultSetType = ResultSetType.FORWARD_ONLY;
	private ResultSetConcurrency resultSetConcurrency = ResultSetConcurrency.READ_ONLY;
	
	private static final Object[] EMPTY_ARRAY = new Object[] {};
	
	public QueryBuilderImpl(QueryExecutor<B> executor, String statement, PropertiesImpl properties) {
		this.executor = executor;
		this.statement = statement;
		this.properties = properties;
		fetchSize = properties.getDefaultFetchSize();
	}

	QueryExecutor<B> getExecutor() {
		return executor;
	}

	String getStatement() {
		return statement;
	}
	
	PropertiesImpl getProperties() {
		return properties;
	}

	void setParameters(PreparedStatement prepStmt) throws SQLException {
		SqlUtils.setArguments(prepStmt, parameters, properties.getNullType());
	}
	
	Object[] getParameters() {
		return parameters.clone();
	}
	
	int getFetchSize() {
		return fetchSize;
	}
	
	ResultSetType getResultSetType() {
		return resultSetType;
	}
	
	ResultSetConcurrency getResultSetConcurrency() {
		return resultSetConcurrency;
	}

	@SuppressWarnings("unchecked")
	private B generify() {
		return (B) this;
	}
	
	@Override
	public B params(Object...parameters) {
		for (int n = 0; n < parameters.length; n++) {
			parameters[n] = properties.adaptParameter(parameters[n]);
		}
		this.parameters = parameters;
		return generify();
	}
	
	@Override
	public B resultSetType(ResultSetType type) {
		this.resultSetType = type;
		return generify();
	}
	
	@Override
	public B resultSetConcurrency(ResultSetConcurrency concurrency) {
		this.resultSetConcurrency = concurrency;
		return generify();
	}
	
	@Override
	public B fetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
		return generify();
	}
	
	@Override
	public <R> QueryResult<R> totalResult(TotalResultMapper<R> mapper) {
		return new TotalResultImpl<>(this, mapper);
	}
	
	@Override
	public QueryResult<Void> voidResult() {
		return new VoidResultImpl(this);
	}

	@Override
	public <R> QueryResult<R> updateCount(UpdateCountMapper<R> mapper) {
		return new UpdateCountResultImpl<>(this, mapper);
	}

	@Override
	public <R> QueryResult<R> updateGenKeys(UpdateGenKeysMapper<R> mapper) {
		return new UpdateGenKeysResultImpl<>(this, mapper);
	}
	
}
