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
package space.arim.jdbcaesar.it;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.stream.Stream;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;

class DataSourceCreator {
	
	private final Vendor vendor;
	
	private DataSourceCreator(Vendor vendor) {
		this.vendor = vendor;
	}
	
	static IdentifiedDataSource create(Vendor vendor) {
		return new DataSourceCreator(vendor).createDataSource();
	}
	
	IdentifiedDataSource createDataSource() {
		switch (vendor) {
		case HSQLDB:
			return new DriverDataSource(Vendor.HSQLDB, "jdbc:hsqldb:mem:testdb", "SA", "");
		case MARIADB:
			return createMariaDb();
		case POSTGRES:
			return createPostgres();
		case SQLITE:
			return createSqlite();
		case H2:
			return new DriverDataSource(Vendor.H2, "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
		default:
			throw new IllegalArgumentException("" + vendor);
		}
	}
	
	private IdentifiedDataSource createMariaDb() {
		DB db;
		try {
			db = DB.newEmbeddedDB(0);
			db.start();
		} catch (ManagedProcessException ex) {
			ex.printStackTrace();
			return null;
		}
		int port = db.getConfiguration().getPort();
		String jdbcUrl = "jdbc:mysql://localhost:" + port + "/test?autocommit=false";

		return new ClosableDataSource(Vendor.MARIADB, jdbcUrl, "root", "", db::stop);
	}
	
	private IdentifiedDataSource createPostgres() {
		EmbeddedPostgres postgres;
		try {
			postgres = EmbeddedPostgres.start();
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		int port = postgres.getPort();
		String jdbcUrl = "jdbc:postgresql://localhost:" + port + '/';
		try (Connection conn = DriverManager.getConnection(jdbcUrl, "postgres", "");
				PreparedStatement prepStmt = conn.prepareStatement("CREATE DATABASE testdb")) {

			prepStmt.execute();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		jdbcUrl += "testdb";

		return new ClosableDataSource(Vendor.POSTGRES, jdbcUrl, "postgres", "", postgres);
	}
	
	private IdentifiedDataSource createSqlite() {
		Path tempDir = createTempDir("sqlite");
		Path dbFile = tempDir.resolve("test.db");
		return new ClosableDataSource(Vendor.SQLITE, "jdbc:sqlite:" + dbFile, () -> cleanupTempDir(tempDir));
	}
	
	private static Path createTempDir(String prefix) {
		Path tempDir;
		try {
			tempDir = Files.createTempDirectory("jdbcaesar-it-" + prefix);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		return tempDir;
	}
	
	private static void cleanupTempDir(Path tempDir) throws IOException {
		try (Stream<Path> walk = Files.walk(tempDir)){
			walk.sorted(Comparator.reverseOrder())
				.forEach((path) -> {
					try {
						Files.delete(path);
					} catch (IOException ex) {
						throw new UncheckedIOException(ex);
					}
				});
		}
	}

}
