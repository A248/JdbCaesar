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

import space.arim.jdbcaesar.transact.InitialTransactionBuilder;
import space.arim.jdbcaesar.transact.IsolationLevel;
import space.arim.jdbcaesar.transact.TransactionBody;
import space.arim.jdbcaesar.transact.TransactionBuilder;

class InitialTransactionBuilderImpl implements InitialTransactionBuilder {

	final JdbCaesarImpl jdbCaesar;
	final MutableTransactionSettings settings;
	
	InitialTransactionBuilderImpl(JdbCaesarImpl jdbCaesar, IsolationLevel defaultIsolation, boolean defaultReadOnly) {
		this.jdbCaesar = jdbCaesar;
		settings = new MutableTransactionSettings(defaultIsolation, defaultReadOnly);
	}

	@Override
	public InitialTransactionBuilder isolation(IsolationLevel isolation) {
		settings.isolation = isolation;
		return this;
	}

	@Override
	public InitialTransactionBuilder readOnly(boolean readOnly) {
		settings.readOnly = readOnly;
		return this;
	}
	
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public <T> TransactionBuilder<T> transactor(space.arim.jdbcaesar.transact.Transactor<T> transactor) {
		return body((querySource, controller) -> transactor.transact(querySource));
	}

	@Override
	public <T> TransactionBuilder<T> body(TransactionBody<T> body) {
		return new TransactionBuilderImpl<>(this, body);
	}
	
}
