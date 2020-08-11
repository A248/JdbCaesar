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

import java.sql.PreparedStatement;
import java.sql.SQLException;

import space.arim.jdbcaesar.error.SubstituteProvider;

final class SqlUtils {

	private SqlUtils() {}
	
	static void setArguments(PreparedStatement prepStmt, Object[] args, int nullType) throws SQLException {
		for (int n = 0; n < args.length; n++) {
			Object param = args[n];
			int position = n + 1;
			if (param == null) {
				prepStmt.setNull(position, nullType);
			} else {
				prepStmt.setObject(position, param);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	static <T> SubstituteProvider<T> nullSubstitute() {
		return (SubstituteProvider<T>) NullSubstituteProvider.INSTANCE;
	}
	
	private static class NullSubstituteProvider implements SubstituteProvider<Object> {
		
		static final SubstituteProvider<?> INSTANCE = new NullSubstituteProvider();
		
		private NullSubstituteProvider() {}
		
		@Override
		public Object getSubstituteValue() {
			return null;
		}
	}

}
