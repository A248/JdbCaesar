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

import java.util.Set;

import space.arim.jdbcaesar.error.SubstituteProvider;
import space.arim.jdbcaesar.mapper.ResultElementMapper;
import space.arim.jdbcaesar.query.SetResult;
import space.arim.jdbcaesar.query.SetResultBuilder;

class SetResultBuilderImpl<T> extends AbstractQueryResultBuilder implements SetResultBuilder<T> {

	private final ResultElementMapper<T> mapper;
	
	SetResultBuilderImpl(InitialQueryBuilderImpl initialBuilder, ResultElementMapper<T> mapper) {
		super(initialBuilder);
		this.mapper = mapper;
	}
	
	@Override
	public SetResult<T> onError(SubstituteProvider<Set<T>> onError) {
		return new SetResultImpl<>(initialBuilder, mapper, onError);
	}
	
}
