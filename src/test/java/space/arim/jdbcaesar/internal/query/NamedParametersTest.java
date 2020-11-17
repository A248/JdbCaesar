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

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class NamedParametersTest {
	
	private final List<String> namedParameters = new ArrayList<>();
	private final NamedParameterAction action = new NamedParameterAction() {

		@Override
		public void visitParameter(String namedParameter) {
			namedParameters.add(namedParameter);
		}
	};
	
	private String parseStatement(String statement) {
		NamedParameters parser = new NamedParameters(statement, action);
		return parser.parseParameters();
	}
	
	@Test
	public void testWithWhereClause() {
		String parsedStatement = parseStatement(
				"SELECT * FROM table WHERE value1 = :param1 AND value2 = :param2 AND value3 = :param3");

		assertEquals("SELECT * FROM table WHERE value1 = ? AND value2 = ? AND value3 = ?", parsedStatement);
		assertEquals(List.of("param1", "param2", "param3"), namedParameters);
	}
	
	@Test
	public void testWithValuesClause() {
		String parsedStatement = parseStatement("INSERT INTO table (col1, col2) VALUES (:param1, :param2)");

		assertEquals("INSERT INTO table (col1, col2) VALUES (?, ?)", parsedStatement);
		assertEquals(List.of("param1", "param2"), namedParameters);
	}
	
	@Test
	public void testWithUpdateClause() {
		String parsedStatement = parseStatement("UPDATE table SET col1 = :param1, col2 = :param2 WHERE col3 = :param3");

		assertEquals("UPDATE table SET col1 = ?, col2 = ? WHERE col3 = ?", parsedStatement);
		assertEquals(List.of("param1", "param2", "param3"), namedParameters);
	}
	
}
