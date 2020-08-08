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
package space.arim.jdbcaesar.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A mapper which maps a row of a result set to a single value
 * 
 * @author A248
 *
 * @param <T> the result type
 */
@FunctionalInterface
public interface ResultSingleMapper<T> {

	/**
	 * Maps a single result from the current row of the specified result set. <br>
	 * <Br>
	 * Implementations thus need not call {@literal rs.next()} since the cursor is
	 * already positioned on the first row.
	 * 
	 * @param resultSet the result set
	 * @return the single result
	 * @throws SQLException generally, per JDBC
	 */
	T mapValueFrom(ResultSet resultSet) throws SQLException;
	
}
