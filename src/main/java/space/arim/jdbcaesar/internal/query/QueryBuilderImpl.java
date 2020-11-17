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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import space.arim.jdbcaesar.internal.PropertiesImpl;
import space.arim.jdbcaesar.internal.QueryExecutor;
import space.arim.jdbcaesar.mapper.LargeUpdateCountMapper;
import space.arim.jdbcaesar.mapper.TotalResultMapper;
import space.arim.jdbcaesar.mapper.UpdateCountMapper;
import space.arim.jdbcaesar.mapper.UpdateGenKeysMapper;
import space.arim.jdbcaesar.query.QueryBuilder;
import space.arim.jdbcaesar.query.QueryResult;
import space.arim.jdbcaesar.query.ResultSetConcurrency;
import space.arim.jdbcaesar.query.ResultSetType;

public class QueryBuilderImpl<B extends QueryBuilder<B>> implements QueryBuilder<B> {

	private final QueryExecutor<B> executor;
	private final String originalStatement;
	private final PropertiesImpl properties;

	private String statement;
	private Object[] arguments = EMPTY_ARRAY;
	private int fetchSize;
	private ResultSetType resultSetType = ResultSetType.FORWARD_ONLY;
	private ResultSetConcurrency resultSetConcurrency = ResultSetConcurrency.READ_ONLY;
	
	private static final Object[] EMPTY_ARRAY = new Object[] {};
	
	public QueryBuilderImpl(QueryExecutor<B> executor, String originalStatement, PropertiesImpl properties) {
		this.executor = executor;
		this.originalStatement = originalStatement;
		this.properties = properties;
		statement = originalStatement;
		fetchSize = properties.getDefaultFetchSize();
	}

	QueryExecutor<B> getExecutor() {
		return executor;
	}

	String getStatement() {
		return statement;
	}
	
	void setStatement(String statement) {
		this.statement = statement;
	}
	
	PropertiesImpl getProperties() {
		return properties;
	}

	void setSingleArguments(PreparedStatement prepStmt) throws SQLException {
		new SqlParameters(arguments, properties.getNullType()).setArguments(prepStmt);
	}
	
	Object[] copyAndAdaptArguments(Object[] arguments) {
		Object[] adapted = new Object[arguments.length];
		for (int n = 0; n < arguments.length; n++) {
			adapted[n] = properties.adaptArgument(arguments[n]);
		}
		return adapted;
	}
	
	Object[] getArguments() {
		return arguments.clone();
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
	public B params(Object...arguments) {
		this.arguments = copyAndAdaptArguments(arguments);
		return generify();
	}

	@Override
	public B params(Map<String, Object> arguments) {
		if (arguments.isEmpty()) {
			this.arguments = EMPTY_ARRAY;
			return generify();
		}
		List<Object> orderedArguments = new ArrayList<>(arguments.size());
		NamedParameterAction action = new NamedParameterAction() {

			@Override
			public void visitParameter(String namedParameter) {
				if (!arguments.containsKey(namedParameter)) {
					throw new IllegalArgumentException("No argument found for named parameter " + namedParameter);
				}
				Object argument = arguments.get(namedParameter);
				orderedArguments.add(properties.adaptArgument(argument));
			}
		};
		String statement = new NamedParameters(originalStatement, action).parseParameters();
		if (orderedArguments.size() < arguments.size()) {
			throw new IllegalArgumentException(
					"Some named parameters in " + arguments + " not present in query " + originalStatement);
		}
		this.statement = statement;
		this.arguments = orderedArguments.toArray();
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
	
	@Override
	public <R> QueryResult<R> largeUpdateCount(LargeUpdateCountMapper<R> mapper) {
		return new LargeUpdateCountResultImpl<>(this, mapper);
	}

	@Override
	public BatchUpdateImpl batch() {
		return new BatchUpdateImpl(this);
	}
	
}
