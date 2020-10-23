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

import java.util.List;

import javax.sql.DataSource;

import space.arim.jdbcaesar.adapter.DataTypeAdapter;
import space.arim.jdbcaesar.error.ExceptionHandler;
import space.arim.jdbcaesar.transact.IsolationLevel;

/**
 * Configured properties of a {@link JdbCaesarBuilder} or {@link JdbCaesar} instance
 * 
 * @author A248
 *
 */
public interface JdbCaesarProperties {

	/**
	 * Gets the data source for acquiring connections
	 * 
	 * @return the data source
	 */
	DataSource getDataSource();
	
	/**
	 * Gets the exception handler
	 * 
	 * @return the exception handler
	 */
	ExceptionHandler getExceptionHandler();
	
	/**
	 * Gets a view of the data type adapters
	 * 
	 * @return an unmodifiable view of the data type adapters
	 */
	List<DataTypeAdapter> getAdapters();
	
	/**
	 * Gets the configured default fetch size
	 * 
	 * @return the default fetch size
	 */
	int getDefaultFetchSize();
	
	/**
	 * Gets the configured null SQL type
	 * 
	 * @return the null type
	 */
	int getNullType();
	
	/**
	 * Gets whether rewrapping exceptions with more info is enabled
	 * 
	 * @return whether exceptions are rewrapped with more details
	 */
	boolean isRewrapExceptions();
	
	/**
	 * Gets the default transaction isolation
	 * 
	 * @return the default isolation
	 */
	IsolationLevel getDefaultIsolation();
	
	/**
	 * Gets the default read only mode
	 * 
	 * @return true if read only by default, false otherwise
	 */
	boolean isDefaultReadOnly();
	
}
