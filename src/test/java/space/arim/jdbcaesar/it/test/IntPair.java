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
package space.arim.jdbcaesar.it.test;

final class IntPair {

	private final int value1;
	private final int value2;
	
	IntPair(int value1, int value2) {
		this.value1 = value1;
		this.value2 = value2;
	}
	
	int value1() {
		return value1;
	}
	
	int value2() {
		return value2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value1;
		result = prime * result + value2;
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof IntPair)) {
			return false;
		}
		IntPair other = (IntPair) object;
		return value1 == other.value1 && value2 == other.value2;
	}

	@Override
	public String toString() {
		return "IntPair [value1=" + value1 + ", value2=" + value2 + "]";
	}
	
}
