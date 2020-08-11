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
package space.arim.jdbcaesar.adapter;

/**
 * Adapter for an enum which adapts instances of the enum to the enum name ({@link Enum#name()}).
 * 
 * @author A248
 *
 * @param <E> the enum type
 */
public class EnumNameAdapter<E extends Enum<E>> implements DataTypeAdapter {

	private final Class<E> enumClass;
	
	/**
	 * Creates from an enum class
	 * 
	 * @param enumClass the enum class
	 * @throws IllegalArgumentException if {@code enumClass} does not represent an enum
	 */
	public EnumNameAdapter(Class<E> enumClass) {
		if (!enumClass.isEnum()) {
			throw new IllegalArgumentException("Class " + enumClass + " is not an enum class");
		}
		this.enumClass = enumClass;
	}

	@Override
	public Object adaptObject(Object parameter) {
		if (enumClass.isInstance(parameter)) {
			@SuppressWarnings("unchecked")
			Enum<E> asEnum = ((Enum<E>) parameter);
			return asEnum.name();
		}
		return parameter;
	}
	
}
