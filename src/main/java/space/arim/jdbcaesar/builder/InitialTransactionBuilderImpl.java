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
import space.arim.jdbcaesar.transact.TransactionBuilder;
import space.arim.jdbcaesar.transact.Transactor;

class InitialTransactionBuilderImpl implements InitialTransactionBuilder {

	private final JdbCaesarImpl jdbCaesar;
	private IsolationLevel isolation;
	private boolean readOnly;
	
	InitialTransactionBuilderImpl(JdbCaesarImpl jdbCaesar) {
		this.jdbCaesar = jdbCaesar;
		isolation = jdbCaesar.isolation;
	}

	@Override
	public InitialTransactionBuilder isolation(IsolationLevel isolation) {
		this.isolation = isolation;
		return this;
	}

	@Override
	public InitialTransactionBuilder readOnly(boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}
	
	@Override
	public <T> TransactionBuilder<T> transactor(Transactor<T> transactor) {
		return new TransactionBuilderImpl<>(jdbCaesar, isolation.getLevel(), readOnly, transactor);
	}
	
}
