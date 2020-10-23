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
package space.arim.jdbcaesar;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import space.arim.jdbcaesar.adapter.DataTypeAdapter;
import space.arim.jdbcaesar.error.ExceptionHandler;
import space.arim.jdbcaesar.error.SubstituteProvider;
import space.arim.jdbcaesar.internal.JdbCaesarImpl;
import space.arim.jdbcaesar.internal.PropertiesImpl;
import space.arim.jdbcaesar.query.QueryResult;
import space.arim.jdbcaesar.query.QueryBuilder;
import space.arim.jdbcaesar.transact.IsolationLevel;
import space.arim.jdbcaesar.transact.Transaction;
import space.arim.jdbcaesar.transact.TransactionSettingsBuilder;

/**
 * Builder of {@link JdbCaesar} instances. <br>
 * <br>
 * No null parameter should be passed. {@code NullPointerException} will be thrown otherwise.
 * 
 * @author A248
 *
 */
public class JdbCaesarBuilder implements JdbCaesarProperties {

	private DataSource dataSource;
	private ExceptionHandler exceptionHandler = ExceptionHandler.DEFAULT;
	private final List<DataTypeAdapter> adapters = new ArrayList<>();
	private transient List<DataTypeAdapter> adaptersView;
	private int defaultFetchSize;
	private IsolationLevel defaultIsolation = IsolationLevel.REPEATABLE_READ;
	private boolean defaultReadOnly;
	private int nullType = Types.NULL;
	private boolean rewrapExceptions;
	
	/**
	 * Sets the data source of this builder to the specified one. Must be called before building
	 * 
	 * @param dataSource the data source
	 * @return this builder
	 */
	public JdbCaesarBuilder dataSource(DataSource dataSource) {
		this.dataSource = Objects.requireNonNull(dataSource, "dataSource");
		return this;
	}
	
	/**
	 * Sets the exception handler of this builder to the specified one. <br>
	 * <br>
	 * This exception handler is used only {@link QueryResult#executeOrGet(SubstituteProvider)}
	 * and {@link Transaction#executeOrGet(SubstituteProvider)}. The default implementation
	 * is {@link ExceptionHandler#DEFAULT}
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
	 * Adds the specified {@link DataTypeAdapter}s to this builder
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
	 * Adds the specified {@link DataTypeAdapter}s to this builder
	 * 
	 * @param adapters the data adapters to add
	 * @return this builder
	 */
	public JdbCaesarBuilder addAdapters(List<DataTypeAdapter> adapters) {
		Objects.requireNonNull(adapters, "adapters");
		for (DataTypeAdapter adapter : adapters) {
			this.adapters.add(Objects.requireNonNull(adapter, "adapter in the array"));
		}
		return this;
	}
	
	/**
	 * Sets the default fetch size of all queries to the specified one. <br>
	 * If specified neither here nor in {@link QueryBuilder#fetchSize(int)}, or if the value
	 * is 0, the vendor dependent default setting will be used.
	 * 
	 * @param fetchSize the default fetch size for all queries
	 * @return this builder
	 */
	public JdbCaesarBuilder defaultFetchSize(int fetchSize) {
		this.defaultFetchSize = fetchSize;
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
		this.defaultIsolation = Objects.requireNonNull(isolation, "isolation");
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
		this.defaultReadOnly = readOnly;
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
	 * all parameters passed to {@link QueryBuilder#params(Object...)}. Only {@code SQLException}s
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
	 * Creates a {@link JdbCaesar} from the details of this builder. May be used
	 * repeatedly without side effects. <br>
	 * <br>
	 * Only {@link #dataSource(DataSource)} is required to be set before building.
	 * 
	 * @return a freshly created {@code JdbCaesar}
	 * @throws IllegalStateException if the data source has not been set
	 */
	public JdbCaesar build() {
		if (dataSource == null) {
			throw new IllegalStateException("Data source is required before building");
		}
		PropertiesImpl properties = new PropertiesImpl(dataSource, exceptionHandler, adapters, defaultFetchSize,
				defaultIsolation, defaultReadOnly, nullType, rewrapExceptions);
		return new JdbCaesarImpl(properties);
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	@Override
	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}
	
	@Override
	public List<DataTypeAdapter> getAdapters() {
		return (adaptersView != null) ? adaptersView : (adaptersView = Collections.unmodifiableList(adapters));
	}

	@Override
	public int getDefaultFetchSize() {
		return defaultFetchSize;
	}
	
	@Override
	public IsolationLevel getDefaultIsolation() {
		return defaultIsolation;
	}

	@Override
	public boolean isDefaultReadOnly() {
		return defaultReadOnly;
	}

	@Override
	public int getNullType() {
		return nullType;
	}

	@Override
	public boolean isRewrapExceptions() {
		return rewrapExceptions;
	}

	@Override
	public String toString() {
		return "JdbCaesarBuilder [dataSource=" + dataSource + ", exceptionHandler=" + exceptionHandler + ", adapters="
				+ adapters + ", defaultFetchSize=" + defaultFetchSize + ", defaultIsolation=" + defaultIsolation
				+ ", defaultReadOnly=" + defaultReadOnly + ", nullType=" + nullType + ", rewrapExceptions="
				+ rewrapExceptions + "]";
	}
	
}
