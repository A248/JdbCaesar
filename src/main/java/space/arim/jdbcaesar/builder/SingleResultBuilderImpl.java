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

import space.arim.jdbcaesar.error.SubstituteProvider;
import space.arim.jdbcaesar.mapper.ResultSingleMapper;
import space.arim.jdbcaesar.query.SingleResult;
import space.arim.jdbcaesar.query.SingleResultBuilder;

class SingleResultBuilderImpl<R> extends AbstractQueryResultBuilder<R> implements SingleResultBuilder<R> {

	private final ResultSingleMapper<R> mapper;
	
	SingleResultBuilderImpl(InitialQueryBuilderImpl<?> initialBuilder, ResultSingleMapper<R> mapper) {
		super(initialBuilder);
		this.mapper = mapper;
	}
	
	@Override
	public SingleResult<R> onError(SubstituteProvider<R> onError) {
		return new SingleResultImpl<>(initialBuilder, mapper, onError);
	}
	
}
