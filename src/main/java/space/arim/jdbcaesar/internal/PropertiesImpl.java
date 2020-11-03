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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import space.arim.jdbcaesar.JdbCaesarProperties;
import space.arim.jdbcaesar.adapter.DataTypeAdapter;
import space.arim.jdbcaesar.error.ExceptionHandler;
import space.arim.jdbcaesar.internal.transact.MutableTransactionSettings;
import space.arim.jdbcaesar.transact.IsolationLevel;

public class PropertiesImpl implements JdbCaesarProperties {

	private final DataSource dataSource;
	private final ExceptionHandler exceptionHandler;
	private final List<DataTypeAdapter> adapters;
	private transient List<DataTypeAdapter> adaptersView;
	private final int fetchSize;
	private final IsolationLevel defaultIsolation;
	private final boolean defaultReadOnly;
	private final int nullType;
	private final boolean rewrapExceptions;

	public PropertiesImpl(DataSource dataSource, ExceptionHandler exceptionHandler,
			List<DataTypeAdapter> adapters, int defaultFetchSize, IsolationLevel defaultIsolation,
			boolean defaultReadOnly, int nullType, boolean rewrapExceptions) {
		this.dataSource = dataSource;
		this.exceptionHandler = exceptionHandler;
		this.adapters = Arrays.asList(adapters.toArray(new DataTypeAdapter[] {}));
		this.fetchSize = defaultFetchSize;
		this.defaultIsolation = defaultIsolation;
		this.defaultReadOnly = defaultReadOnly;
		this.nullType = nullType;
		this.rewrapExceptions = rewrapExceptions;
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

	public Object adaptParameter(Object param) {
		for (DataTypeAdapter adapter : adapters) {
			Object result = adapter.adaptObject(param);
			if (result != param) {
				return result;
			}
		}
		return param;
	}

	@Override
	public int getDefaultFetchSize() {
		return fetchSize;
	}
	
	@Override
	public IsolationLevel getDefaultIsolation() {
		return defaultIsolation;
	}

	@Override
	public boolean isDefaultReadOnly() {
		return defaultReadOnly;
	}
	
	public MutableTransactionSettings deriveTransactionSettings() {
		return new MutableTransactionSettings(defaultIsolation, defaultReadOnly);
	}

	@Override
	public int getNullType() {
		return nullType;
	}

	@Deprecated
	@Override
	public boolean isRewrapExceptions() {
		return rewrapExceptions;
	}

	@Override
	public String toString() {
		return "PropertiesImpl [dataSource=" + dataSource + ", exceptionHandler=" + exceptionHandler + ", adapters="
				+ adapters + ", fetchSize=" + fetchSize + ", defaultIsolation=" + defaultIsolation
				+ ", defaultReadOnly=" + defaultReadOnly + ", nullType=" + nullType + ", rewrapExceptions="
				+ rewrapExceptions + "]";
	}
	
}
