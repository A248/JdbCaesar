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

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import space.arim.jdbcaesar.ConnectionSource;
import space.arim.jdbcaesar.JdbCaesar;
import space.arim.jdbcaesar.JdbCaesarInfo;
import space.arim.jdbcaesar.adapter.DataTypeAdapter;
import space.arim.jdbcaesar.error.ExceptionHandler;
import space.arim.jdbcaesar.query.InitialQueryBuilder;
import space.arim.jdbcaesar.transact.IsolationLevel;
import space.arim.jdbcaesar.transact.TransactionSettingsBuilder;

/**
 * Builder of {@link JdbCaesar} instances. <br>
 * <br>
 * No null parameter should be passed. {@code NullPointerException} will be thrown otherwise.
 * 
 * @author A248
 *
 */
public class JdbCaesarBuilder implements JdbCaesarInfo {

	private ConnectionSource connectionSource;
	private ExceptionHandler exceptionHandler;
	private final List<DataTypeAdapter> adapters = new ArrayList<>();
	private int fetchSize;
	private IsolationLevel isolation = IsolationLevel.REPEATABLE_READ;
	private boolean readOnly;
	private int nullType = Types.NULL;
	private boolean rewrapExceptions;
	
	/**
	 * Sets the connection source of this builder to the specified one
	 * 
	 * @param connectionSource the connection source
	 * @return this builder
	 */
	public JdbCaesarBuilder connectionSource(ConnectionSource connectionSource) {
		this.connectionSource = Objects.requireNonNull(connectionSource, "connectionSource");
		return this;
	}
	
	/**
	 * Sets the exception handler of this builder to the specified one
	 * 
	 * @param exceptionHandler the exception handler
	 * @return this builder
	 */
	public JdbCaesarBuilder exceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = Objects.requireNonNull(exceptionHandler, "exceptionHandler");
		return this;
	}
	
	/**
	 * Adds the specified {@link DataTypeAdapter} to this builder (optional operation)
	 * 
	 * @param adapter the data adapter to add
	 * @return this builder
	 */
	public JdbCaesarBuilder addAdapter(DataTypeAdapter adapter) {
		this.adapters.add(Objects.requireNonNull(adapter, "adapter"));
		return this;
	}
	
	/**
	 * Adds the specified {@link DataTypeAdapter}s to this builder (optional operation)
	 * 
	 * @param adapters the data adapters to add
	 * @return this builder
	 */
	public JdbCaesarBuilder addAdapters(DataTypeAdapter...adapters) {
		Objects.requireNonNull(adapters, "adapters");
		for (DataTypeAdapter adapter : adapters) {
			this.adapters.add(Objects.requireNonNull(adapter, "adapter in the array"));
		}
		return this;
	}
	
	/**
	 * Sets the default fetch size of all queries to the specified one (optional operation). <br>
	 * If specified neither here nor in {@link InitialQueryBuilder#fetchSize(int)}, or if the value
	 * is 0, the vendor dependent default setting will be used.
	 * 
	 * @param fetchSize the default fetch size for all queries
	 * @return this builder
	 */
	public JdbCaesarBuilder defaultFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
		return this;
	}
	
	/**
	 * Sets the default isolation level of queries and transactions. If unspecified, it is {@link IsolationLevel#REPEATABLE_READ}. <br>
	 * <br>
	 * This setting can be overridden per query or transaction, using {@link TransactionSettingsBuilder#isolation(IsolationLevel)}
	 * 
	 * @param isolation the default transaction isolation level
	 * @return this builder
	 */
	public JdbCaesarBuilder defaultIsolation(IsolationLevel isolation) {
		this.isolation = Objects.requireNonNull(isolation, "isolation");
		return this;
	}
	
	/**
	 * Sets the default read only mode of queries and transactions. If unspecified, it is {@code false}. <br>
	 * <br>
	 * This setting can be overridden per query or transaction, using {@link TransactionSettingsBuilder#readOnly(boolean)}
	 * 
	 * @param readOnly the default read only flag
	 * @return this builder
	 */
	public JdbCaesarBuilder defaultReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}
	
	/**
	 * Allows changing the SQL type passed when {@code null} is given as a statement parameter. <br>
	 * <br>
	 * The default value is {@link Types#NULL} and is sufficient for the majority of use caces. However,
	 * some databases may expect {@link Types#OTHER}, notably Oracle DBMS.
	 * 
	 * @param nullType the SQL type to use for null values
	 * @return this builder
	 */
	public JdbCaesarBuilder nullSqlType(int nullType) {
		this.nullType = nullType;
		return this;
	}
	
	/**
	 * Decides whether to rewrap {@code SQLException}s with more detailed exception messages.
	 * By default this setting is false, but using it may be helpful for debugging purposes. <br>
	 * <br>
	 * If enabled, all {@code SQLException}s arising from executing statements through JdbCaesar will be
	 * rewrapped to provide more detailed exception messages, including the executed SQL statement and
	 * all parameters passed to {@link InitialQueryBuilder#params(Object...)}. Only {@code SQLException}s
	 * directly arising from query execution will be wrapped, and not circumstantial operations such as
	 * retrieving and closing connections, setting transaction isolation values, and committing and rolling back.
	 * 
	 * @param rewrapExceptions true to rewrap {@code SQLException}s with more details, false otherwise
	 * @return this builder
	 */
	public JdbCaesarBuilder rewrapExceptions(boolean rewrapExceptions) {
		this.rewrapExceptions = rewrapExceptions;
		return this;
	}
	
	/**
	 * Creates a {@link JdbCaesar} from the details of this builder
	 * 
	 * @return a freshly created {@code JdbCaesar}
	 */
	public JdbCaesar build() {
		return new JdbCaesarImpl(
				connectionSource, exceptionHandler, adapters, fetchSize, isolation, readOnly, nullType, rewrapExceptions);
	}

	@Override
	public ConnectionSource getConnectionSource() {
		return connectionSource;
	}

	@Override
	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}
	
}
