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
import java.util.Arrays;
import java.util.List;

abstract class BatchUpdateResult<R> extends AbstractQueryResult<R> {

	private final List<Object[]> arguments;

	BatchUpdateResult(QueryBuilderImpl<?> initialBuilder, List<Object[]> arguments) {
		super(initialBuilder);
		this.arguments = arguments;
	}

	@Override
	void setArguments(PreparedStatement prepStmt) throws SQLException {
		int nullType = getInitialBuilder().getProperties().getNullType();
		for (Object[] arguments : arguments) {
			new SqlParameters(arguments, nullType).setArguments(prepStmt);
			prepStmt.addBatch();
		}
	}

	@Override
	public R execute() {
		if (arguments.isEmpty()) {
			return getEmptyResult();
		}
		return super.execute();
	}

	@Override
	public String getExceptionDetails() {
		return "For statement [" + getInitialBuilder().getStatement() + "] and batch parameters ["
				+ Arrays.deepToString(arguments.toArray()) + "]";
	}

	@Override
	abstract R getResult(PreparedStatement prepStmt) throws SQLException;

	abstract R getEmptyResult();

	static class StandardBatchUpdate extends BatchUpdateResult<int[]> {

		StandardBatchUpdate(QueryBuilderImpl<?> initialBuilder, List<Object[]> arguments) {
			super(initialBuilder, arguments);
		}

		@Override
		int[] getResult(PreparedStatement prepStmt) throws SQLException {
			return prepStmt.executeBatch();
		}

		@Override
		int[] getEmptyResult() {
			return new int[] {};
		}

	}
	
	static class LargeBatchUpdate extends BatchUpdateResult<long[]> {

		LargeBatchUpdate(QueryBuilderImpl<?> initialBuilder, List<Object[]> arguments) {
			super(initialBuilder, arguments);
		}

		@Override
		long[] getResult(PreparedStatement prepStmt) throws SQLException {
			return prepStmt.executeLargeBatch();
		}

		@Override
		long[] getEmptyResult() {
			return new long[] {};
		}

	}

}
