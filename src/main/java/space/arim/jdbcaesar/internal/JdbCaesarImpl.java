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
package space.arim.jdbcaesar.internal;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import space.arim.jdbcaesar.JdbCaesar;
import space.arim.jdbcaesar.JdbCaesarProperties;
import space.arim.jdbcaesar.assimilate.AssimilatedQuerySource;
import space.arim.jdbcaesar.error.UncheckedSQLException;
import space.arim.jdbcaesar.internal.assimilate.AssimilatedQuerySourceImpl;
import space.arim.jdbcaesar.internal.query.ConnectionAcceptor;
import space.arim.jdbcaesar.internal.transact.MutableTransactionSettings;
import space.arim.jdbcaesar.internal.transact.TransactionBuilderImpl;
import space.arim.jdbcaesar.query.SingleQueryBuilder;
import space.arim.jdbcaesar.transact.TransactionBuilder;

public class JdbCaesarImpl implements JdbCaesar, QueryExecutor<SingleQueryBuilder> {

	private final PropertiesImpl properties;
	
	public JdbCaesarImpl(PropertiesImpl properties) {
		this.properties = Objects.requireNonNull(properties, "properties");
	}

	@Override
	public JdbCaesarProperties getProperties() {
		return properties;
	}
	
	@Override
	public SingleQueryBuilder query(String statement) {
		return new SingleQueryBuilderImpl(this, statement, properties);
	}
	
	@Override
	public TransactionBuilder transaction() {
		return new TransactionBuilderImpl(properties);
	}
	
	@Override
	public AssimilatedQuerySource assimilate(Connection connection) {
		return new AssimilatedQuerySourceImpl(properties, connection);
	}
	
	@Override
	public <R> R execute(ConnectionAcceptor<R> acceptor) {
		SingleQueryBuilderImpl sqbi = (SingleQueryBuilderImpl) acceptor.getInitialBuilder();
		MutableTransactionSettings settings = sqbi.getSettings();
		int isolationLevel = settings.getIsolation().getLevel();
		boolean readOnly = settings.isReadOnly();

		try (Connection connection = properties.getDataSource().getConnection()) {
			connection.setAutoCommit(false);
			connection.setTransactionIsolation(isolationLevel);
			connection.setReadOnly(readOnly);

			R result;
			try {
				result = acceptor.acceptConnection(connection);
			} catch (RuntimeException ex) {
				try {
					connection.rollback();
				} catch (SQLException suppressed) { ex.addSuppressed(suppressed); }
				throw ex;
			} catch (SQLException ex) {
				try {
					connection.rollback();
				} catch (SQLException suppressed) { ex.addSuppressed(suppressed); }
				if (properties.isRewrapExceptions()) {
					ex = acceptor.rewrapExceptionWithDetails(ex);
				}
				throw new UncheckedSQLException(ex);
			}
			connection.commit();
			return result;

		} catch (SQLException ex) {
			throw new UncheckedSQLException("Unable to manage single-query transaction", ex);
		}
	}
	
}
