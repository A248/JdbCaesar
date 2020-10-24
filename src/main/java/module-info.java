module space.arim.jdbcaesar {
	exports space.arim.jdbcaesar;
	exports space.arim.jdbcaesar.adapter;
	exports space.arim.jdbcaesar.assimilate;
	exports space.arim.jdbcaesar.error;
	exports space.arim.jdbcaesar.mapper;
	exports space.arim.jdbcaesar.query;
	exports space.arim.jdbcaesar.transact;

	requires transitive java.sql;
}