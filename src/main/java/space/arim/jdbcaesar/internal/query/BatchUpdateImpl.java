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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import space.arim.jdbcaesar.query.BatchUpdate;
import space.arim.jdbcaesar.query.QueryResult;

class BatchUpdateImpl implements BatchUpdate {
	
	private final QueryBuilderImpl<?> initialBuilder;
	private final List<Object[]> allArguments = new ArrayList<>();
	
	private int orderedParameterCount = -1;
	private List<String> namedParameterList;
	
	BatchUpdateImpl(QueryBuilderImpl<?> initialBuilder) {
		this.initialBuilder = initialBuilder;
	}
	
	List<Object[]> getAllArguments() {
		return allArguments;
	}
	
	private void addArguments(Object[] arguments) {
		allArguments.add(arguments);
	}
	
	@Override
	public BatchUpdate addParams(Object... arguments) {
		if (orderedParameterCount == -1) {
			orderedParameterCount = arguments.length;
		} else if (orderedParameterCount != arguments.length) {
			throw new IllegalArgumentException("Argument count does not match previous calls");
		}
		addArguments(initialBuilder.copyAndAdaptArguments(arguments));
		return this;
	}
	
	@Override
	public BatchUpdate addParams(Map<String, Object> arguments) {
		if (namedParameterList == null) {
			namedParameterList = buildNamedParameterList(arguments.size());
		}
		Object[] orderedArguments = new Object[namedParameterList.size()];
		for (int n = 0; n < orderedArguments.length; n++) {
			String namedParameter = namedParameterList.get(n);
			if (!arguments.containsKey(namedParameter)) {
				throw new IllegalArgumentException("No argument found for named batch parameter " + namedParameter);
			}
			orderedArguments[n] = initialBuilder.getProperties().adaptArgument(arguments.get(namedParameter));
		}
		addArguments(orderedArguments);
		return this;
	}
	
	private List<String> buildNamedParameterList(int estimatedSize) {
		List<String> namedParameterList = new ArrayList<>(Math.min(estimatedSize, 8));
		NamedParameterAction action = new NamedParameterAction() {

			@Override
			public void visitParameter(String namedParameter) {
				namedParameterList.add(namedParameter);
			}
		};
		String statement = new NamedParameters(initialBuilder.getStatement(), action).parseParameters();
		initialBuilder.setStatement(statement);
		return namedParameterList;
	}

	@Override
	public QueryResult<int[]> readyBatch() {
		return new BatchUpdateResult.StandardBatchUpdate(initialBuilder, allArguments);
	}

	@Override
	public QueryResult<long[]> readyLargeBatch() {
		return new BatchUpdateResult.LargeBatchUpdate(initialBuilder, allArguments);
	}

}
