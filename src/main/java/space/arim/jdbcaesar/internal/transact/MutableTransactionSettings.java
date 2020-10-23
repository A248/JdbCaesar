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
package space.arim.jdbcaesar.internal.transact;

import java.util.Objects;

import space.arim.jdbcaesar.transact.IsolationLevel;

public class MutableTransactionSettings {

	private IsolationLevel isolation;
	private boolean readOnly;
	
	public MutableTransactionSettings(IsolationLevel isolation, boolean readOnly) {
		this.isolation = Objects.requireNonNull(isolation, "isolation");
		this.readOnly = readOnly;
	}

	public IsolationLevel getIsolation() {
		return isolation;
	}

	public void setIsolation(IsolationLevel isolation) {
		this.isolation = Objects.requireNonNull(isolation, "isolation");
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + isolation.hashCode();
		result = prime * result + (readOnly ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof MutableTransactionSettings)) {
			return false;
		}
		MutableTransactionSettings other = (MutableTransactionSettings) object;
		return isolation == other.isolation && readOnly == other.readOnly;
	}

	@Override
	public String toString() {
		return "MutableTransactionSettings [isolation=" + isolation + ", readOnly=" + readOnly + "]";
	}
	
}
