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
package space.arim.jdbcaesar.error;

/**
 * The supplier of some default value should an error or rollback occur.
 * 
 * @author A248
 *
 */
@FunctionalInterface
public interface SubstituteProvider<T> {

	/**
	 * Gets the value of this substitute provider. This method should be stateless, as it
	 * may be called multiple times in some edge situations.
	 * 
	 * @return the substitute value
	 */
	T getSubstituteValue();
	
}
