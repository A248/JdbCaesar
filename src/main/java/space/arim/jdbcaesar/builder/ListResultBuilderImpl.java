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

import java.util.Collections;
import java.util.List;

import space.arim.jdbcaesar.error.SubstituteProvider;
import space.arim.jdbcaesar.mapper.ResultElementMapper;
import space.arim.jdbcaesar.query.ListResult;
import space.arim.jdbcaesar.query.ListResultBuilder;

class ListResultBuilderImpl<E> extends AbstractQueryResultBuilder<List<E>> implements ListResultBuilder<E> {

	private final ResultElementMapper<E> mapper;
	
	ListResultBuilderImpl(InitialQueryBuilderImpl<?> initialBuilder, ResultElementMapper<E> mapper) {
		super(initialBuilder);
		this.mapper = mapper;
	}
	
	@Override
	public ListResult<E> onError(SubstituteProvider<List<E>> onError) {
		return new ListResultImpl<>(initialBuilder, mapper, onError);
	}
	
	@Override
	public List<E> execute() {
		return onError(Collections::emptyList).execute();
	}
	
}
