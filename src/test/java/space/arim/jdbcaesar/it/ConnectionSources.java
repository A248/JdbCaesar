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
import java.util.Objects;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;

class ConnectionSources {
	
	private static final IdentifiedConnectionSource HSQLDB;
	
	private static final IdentifiedConnectionSource MARIADB;
	
	private static final IdentifiedConnectionSource POSTGRES;
	
	private static final IdentifiedConnectionSource SQLITE;
	
	private static final IdentifiedConnectionSource H2;
	
	private static final Logger logger = LoggerFactory.getLogger(ConnectionSources.class);
	
	static {
		HSQLDB = new SimpleConnectionSource(Vendor.HSQLDB, "jdbc:hsqldb:mem:testdb", "SA", "");
		MARIADB = createMariaDb();
		POSTGRES = createPostgres();
		SQLITE = createSqlite();
		H2 = new SimpleConnectionSource(Vendor.H2, "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
	}
	
	static Stream<IdentifiedConnectionSource> all() {
		return Stream.of(HSQLDB, MARIADB, POSTGRES, SQLITE, H2).filter(Objects::nonNull)
				.peek((source) -> logger.debug("Using vendor " + source.vendor()));
	}
	
	private static IdentifiedConnectionSource createMariaDb() {
		try {
			DB db = DB.newEmbeddedDB(0);
			db.start();
			shutdownHook(() -> {
				try {
					db.stop();
				} catch (ManagedProcessException ex) {
					ex.printStackTrace();
				}
			});
			int port = db.getConfiguration().getPort();
			String jdbcUrl = "jdbc:mysql://localhost:" + port + "/test?autocommit=false";
			return new SimpleConnectionSource(Vendor.MARIADB, jdbcUrl, "root", "", false);

		} catch (ManagedProcessException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	private static IdentifiedConnectionSource createPostgres() {
		EmbeddedPostgres postgres;
		try {
			postgres = EmbeddedPostgres.start();
			shutdownHook(() -> {
				try {
					postgres.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			});
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
			return new SimpleConnectionSource(Vendor.POSTGRES, jdbcUrl, "postgres", "");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	private static IdentifiedConnectionSource createSqlite() {
		Path tempDir = createTempDir("sqlite");
		if (tempDir == null) {
			return null;
		}
		Path dbFile = tempDir.resolve("test.db");
		return new SimpleConnectionSource(Vendor.SQLITE, "jdbc:sqlite:" + dbFile);
	}
	
	private static Path createTempDir(String prefix) {
		Path tempDir;
		try {
			tempDir = Files.createTempDirectory("jdbcaesar-it-" + prefix);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		shutdownHook(() -> {
			try (Stream<Path> walk = Files.walk(tempDir)){
				walk.sorted(Comparator.reverseOrder())
					.forEach((path) -> {
						try {
							Files.delete(path);
						} catch (IOException ex) {
							throw new UncheckedIOException(ex);
						}
					});
			} catch (IOException | UncheckedIOException ex) {
				Assertions.fail(ex);
			}
		});
		return tempDir;
	}
	
	private static void shutdownHook(Runnable toDo) {
		Runtime.getRuntime().addShutdownHook(new Thread(toDo));
	}
	
	static Connection getWithoutAutoCommit(Connection conn) throws SQLException {
		try {
			conn.setAutoCommit(false);
			return conn;
		} catch (SQLException ex) {
			conn.close();
			throw ex;
		} catch (RuntimeException ex) {
			conn.close();
			throw ex;
		}
	}
	
	private static class SimpleConnectionSource extends IdentifiedConnectionSource {

		private final String jdbcUrl;
		private final String username;
		private final String password;
		private final boolean adjustAutoCommit;
		private final boolean credentials;
		
		SimpleConnectionSource(Vendor vendor, String jdbcUrl, String username, String password) {
			this(vendor, jdbcUrl, username, password, true, true);
		}
		
		SimpleConnectionSource(Vendor vendor, String jdbcUrl, String username, String password,
				boolean adjustAutoCommit) {
			this(vendor, jdbcUrl, username, password, true, adjustAutoCommit);
		}
		
		SimpleConnectionSource(Vendor vendor, String jdbcUrl) {
			this(vendor, jdbcUrl, null, null, false, true);
		}
		
		private SimpleConnectionSource(Vendor vendor, String jdbcUrl, String username, String password,
				boolean credentials, boolean adjustAutoCommit) {
			super(vendor);
			this.jdbcUrl = jdbcUrl;
			this.username = username;
			this.password = password;
			this.credentials = credentials;
			this.adjustAutoCommit = adjustAutoCommit;
		}
		
		@SuppressWarnings("resource")
		@Override
		public Connection getConnection() throws SQLException {
			Connection conn;
			if (credentials) {
				conn = DriverManager.getConnection(jdbcUrl, username, password);
			} else {
				conn = DriverManager.getConnection(jdbcUrl);
			}
			if (adjustAutoCommit) {
				return getWithoutAutoCommit(conn);
			} else {
				return conn;
			}
		}

		@Override
		public void close() throws SQLException {
		}
		
	}
	
}
