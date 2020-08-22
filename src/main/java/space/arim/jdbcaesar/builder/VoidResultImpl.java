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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import space.arim.jdbcaesar.query.VoidResult;

class VoidResultImpl extends ConnectionAcceptor implements VoidResult {

	VoidResultImpl(InitialQueryBuilderImpl initialBuilder) {
		super(initialBuilder);
	}

	@Override
	public Void execute() {
		initialBuilder.executor.execute(this);
		return null;
	}

	@Override
	void acceptConnection(Connection conn) throws SQLException {
		String statement = initialBuilder.statement;
		Object[] params = initialBuilder.params;
		int nullType = initialBuilder.executor.nullType();
		try (PreparedStatement prepStmt = conn.prepareStatement(statement, Statement.NO_GENERATED_KEYS)) {
			SqlUtils.setArguments(prepStmt, params, nullType);
			prepStmt.execute();
		}
	}

	@Override
	void onError() {
		// ignore
	}

}
