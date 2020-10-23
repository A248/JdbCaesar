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

import java.sql.Connection;
import java.sql.SQLException;

import space.arim.jdbcaesar.error.SubstituteProvider;
import space.arim.jdbcaesar.error.UncheckedSQLException;
import space.arim.jdbcaesar.internal.PropertiesImpl;
import space.arim.jdbcaesar.transact.Transaction;
import space.arim.jdbcaesar.transact.TransactionBody;

class TransactionImpl<R> implements Transaction<R> {
	
	private final TransactionBuilderImpl initialBuilder;
	private final TransactionBody<R> body;
	
	TransactionImpl(TransactionBuilderImpl initialBuilder, TransactionBody<R> body) {
		this.initialBuilder = initialBuilder;
		this.body = body;
	}
	
	@Override
	public R execute() {
		PropertiesImpl properties = initialBuilder.getProperties();
		MutableTransactionSettings settings = initialBuilder.getSettings();
		int isolationLevel = settings.getIsolation().getLevel();
		boolean readOnly = settings.isReadOnly();

		try (Connection connection = properties.getDataSource().getConnection()) {
			connection.setAutoCommit(false);
			connection.setTransactionIsolation(isolationLevel);
			connection.setReadOnly(readOnly);

			R result;
			try {
				TransactionQuerySourceImpl sourceAndController = new TransactionQuerySourceImpl(connection, properties);
				result = body.transact(sourceAndController, sourceAndController);

			} catch (RuntimeException ex) { // includes UncheckedSQLException
				try {
					connection.rollback();
				} catch (SQLException suppressed) { ex.addSuppressed(suppressed); }
				throw ex;
			} catch (SQLException ex) {
				try {
					connection.rollback();
				} catch (SQLException suppressed) { ex.addSuppressed(suppressed); }
				throw new UncheckedSQLException(ex);
			}
			connection.commit();
			return result;

		} catch (SQLException ex) {
			throw new UncheckedSQLException("Unable to manage transaction", ex);
		}
	}

	@Override
	public R executeOrGet(SubstituteProvider<R> onError) {
		try {
			return execute();
		} catch (UncheckedSQLException ex) {
			initialBuilder.getProperties().getExceptionHandler().handleException(ex.getCause());
			return onError.getSubstituteValue();
		}
	}

}
