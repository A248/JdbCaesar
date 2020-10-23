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

import space.arim.jdbcaesar.internal.PropertiesImpl;
import space.arim.jdbcaesar.transact.TransactionBuilder;
import space.arim.jdbcaesar.transact.IsolationLevel;
import space.arim.jdbcaesar.transact.TransactionBody;
import space.arim.jdbcaesar.transact.Transaction;

public class TransactionBuilderImpl implements TransactionBuilder {

	private final PropertiesImpl properties;
	private final MutableTransactionSettings settings;
	
	public TransactionBuilderImpl(PropertiesImpl properties) {
		this.properties = properties;
		settings = properties.deriveTransactionSettings();
	}

	PropertiesImpl getProperties() {
		return properties;
	}

	MutableTransactionSettings getSettings() {
		return settings;
	}

	@Override
	public TransactionBuilder isolation(IsolationLevel isolation) {
		settings.setIsolation(isolation);
		return this;
	}

	@Override
	public TransactionBuilder readOnly(boolean readOnly) {
		settings.setReadOnly(readOnly);
		return this;
	}

	@Override
	public <T> Transaction<T> body(TransactionBody<T> body) {
		return new TransactionImpl<>(this, body);
	}
	
}
