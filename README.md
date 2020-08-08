
# JdbCaesar

Conquer the verbosity of plain JDBC.

## Introduction

Normal `java.sql` JDBC is well-known for the cumbersome Connection, PreparedStatement, and ResultSet declarations. This often results in several try-with-resources and nested try blocks. However, most code is simply intended to map a SQL result to some POJO or Collection.

JdbCaesar is a simple yet effective alternative substantially reducing the amount of boilerplate required in a typical query. Chainable builders allow executing a single query and mapping to a result while falling back to a default value should a `SQLException` occur.

### Features

* Easily execute single queries.
* Don't write database-related try-with-resources blocks any more.
* No need to sacrifice useful tools such as generated keys.
* Made to use functional code.
* Support for transactions in the same manner as the rest of the library.
* Reduce vendor-dependent details.
* Support for invoking stored procedures.

### Requirements

* Java 8 or later
* Connections with auto-commit *disabled*

Auto-commit support was initially incorporated into the library. However, it created more room for error with little benefit. Disabling auto-commit allows more powerful queries with less maintenance.

### Dependency Information

The dependency FQDN is `space.arim.jdbcaesar:jdbcaesar:{VERSION}` and is available from the repository `https://www.arim.space/maven/`.

## Usage

### Querying

**Single Result**

```java
Type obj = jdbCaesar
	.query("SELECT * FROM table WHERE value1 = ? AND value2 = ?")
	.params("value", 1) // varargs
	.singleResult( // result mapper body
			(resultSet) -> new Type(resultSet.getString("column1"), resultSet.getInt("column2")))
	.onError(() -> null) // fallback value should SQLException occur
	.execute();
```

If no results were returned from the database, `execute()` returns null. If an SQLException occurs, the global exception handler
is invoked and the onError callback is used.

**Collection Result**

*List* and *Set* are both supported specially:

```java
List<Type> list = jdbCaesar
	.query("SELECT * FROM table WHERE value1 = ? AND value2 = ?")
	.params("anothervalue", otherObject)
	.listResult( // or setResult for a Set
			(resultSet) -> new Type(resultSet.getString("column1"), resultSet.getInt("column2")))
			
	.onError(() -> List.of()) // works well with Java 9 immutable collection factories
	.execute();
```

JdbCaesar will automatically iterate over the `ResultSet` and use the list or set mapper provided to map
each row.

**Result Based on Update Count and/or Generated Keys**

```java
Type obj = jdbCaesar.query("SELECT * FROM table WHERE value1 = ? AND value2 = ?")
	.params(1, "information")
	.updateCount((updateCount) -> new Type(updateCount)) // Type::new
	.onError(() -> null)
	.execute();
```

Use `updateGenKeys` instead of `updateCount` when generated keys are needed.

**Result Based on Full ResultSet**

```java
Type obj = jdbCaesar
	.query("SELECT * FROM table WHERE value1 = ? AND value2 = ?")
	.params("value", 1) // varargs
	.combinedResult((resultSet) -> {
	    // Use entire ResultSet here
	    int count;
	    while (resultSet.next()) {
	        count++;
	    }
	    return new Type(count);
	}).onError(() -> null) // fallback value should SQLException occur
	.execute();
```

### Obtaining the Instance

Use the JdbCaesarBuilder:

```java
DatabaseSource dbSource;
ExceptionHandler exHandler;
new JdbCaesarBuilder()
    .databaseSource(dbSource)
    .exceptionHandler(exHandler)
    .defaultFetchSize(1000) // optional, if unspecified, uses vendor default
    .defaultIsolation(IsolationLevel.READ_COMMITTED) // optional
    .build();
```

Both the *DatabaseSource* and *ExceptionHandler* are required. You would not want to omit handling any exceptions.

Adding `DataTypeAdapter`s, setting the default fetch size, and defining the default transaction isolation level is optional.

### Transactions

**Example**

```java
Type transactionResult;
transactionResult = jdbCaesar.transaction()
		.isolation(IsolationLevel.REPEATABLE_READ) // optional, if unspecified, uses global default
		
		.transactor((querySource) -> { // be sure to use this `querySource` to create queries and not the JdbCaesar instance!
		
		// main body of transaction
		// multiple queries may be run here in the same fashion
		// as would be done normally
		
		}).onError(() -> fallbackValue).execute();
	return (Type) result;
});
```

The body of the transaction should use the `TransactionQuerySource` for running queries as part of the transaction.

**Isolation Level**

The transaction isolation level may be set per transaction or globally. Specifying the value per transaction overrides the global setting.

If neither the global isolation level or per transaction value is set, JdbCaesar uses `REPEATABLE_READ`. The objective of this practice is to avoid using the database vendor's default isolation.

**Exception Handling**

JdbCaesar handles all SQLExceptions. Should a SQLException occur during any query in a transaction, the entire transaction is automatically rolled back. The `Transactor` (the body of the transaction) may manually rollback by throwing `RollMeBackException`. *RollMeBackException* always has an empty stacktrace to improve performance of rolling back transactions.

