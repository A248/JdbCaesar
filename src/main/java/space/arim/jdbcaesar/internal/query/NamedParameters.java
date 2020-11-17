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

class NamedParameters {

	private final String originalStatement;
	private final NamedParameterAction action;
	
	NamedParameters(String originalStatement, NamedParameterAction action) {
		this.originalStatement = originalStatement;
		this.action = action;
	}
	
	String parseParameters() {
		StringBuilder statementBuilder = new StringBuilder(originalStatement.length());

		int position = 0;
		while (true) {
			int colonIndex = originalStatement.indexOf(':', position);
			if (colonIndex == -1 || colonIndex == originalStatement.length() - 1) {
				// No more named parameters. Append remaining segment
				statementBuilder.append(originalStatement, position, originalStatement.length());
				break;
			}
			// Append previous segment
			statementBuilder.append(originalStatement, position, colonIndex);

			int parameterStartIndex = colonIndex + 1;
			String namedParameter = getNamedParameter(parameterStartIndex);

			// Update position to prepare for next iteration
			position = parameterStartIndex + namedParameter.length();

			action.visitParameter(namedParameter);
			statementBuilder.append('?'); // statementBuilder.append(':').append(namedParameter);
		}
		return statementBuilder.toString();
	}
	
	private String getNamedParameter(int parameterStartIndex) {
		int endParameterIndex = indexOfNonAlphaNumeric(parameterStartIndex);
		if (endParameterIndex == -1) {
			return originalStatement.substring(parameterStartIndex);
		} else {
			return originalStatement.substring(parameterStartIndex, endParameterIndex);
		}
	}
	
	private int indexOfNonAlphaNumeric(int offset) {
		for (int n = offset; n < originalStatement.length(); n++) {
			if (!isAlphaNumeric(originalStatement.charAt(n))) {
				return n;
			}
		}
		return -1;
	}
	
	private static boolean isAlphaNumeric(char character) {
		return Character.isAlphabetic(character) || Character.isDigit(character) || character == '_';
	}
	
}
