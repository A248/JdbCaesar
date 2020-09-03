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

import java.util.Objects;

class PretendPOJO {

	private final int integer;
	private final String string;
	
	PretendPOJO(int integer, String string) {
		this.integer = integer;
		this.string = Objects.requireNonNull(string, "string");
	}
	
	int integer() {
		return integer;
	}
	
	String string() {
		return string;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + integer;
		result = prime * result + string.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof PretendPOJO)) {
			return false;
		}
		PretendPOJO other = (PretendPOJO) object;
		return integer == other.integer && string.equals(other.string);
	}

	@Override
	public String toString() {
		return "PretendPOJO [integer=" + integer + ", string=" + string + "]";
	}
	
}
