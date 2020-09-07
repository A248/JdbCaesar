
# JdbCaesar

Conquer the verbosity of plain JDBC.

## Introduction

Normal `java.sql` is known for cumbersome Connection, PreparedStatement, and ResultSet declarations. This often results in several try-with-resources and nested try blocks. However, most code is simply intended to map a SQL result to some POJO or Collection.

JdbCaesar is a simple yet effective alternative substantially reducing the amount of boilerplate required in a typical query. Chainable builders allow executing a single query and mapping to a result while falling back to a default value should a `SQLException` occur.

### Features

* Easily execute single queries. Never again write database-related try-with-resources blocks.
* Don't sacrifice useful tools such as generated keys, updatable result sets, fetch size control, etc.
* Made to use functional code.
* Fully parameterised queries.
* Support for transactions in the same manner as the rest of the library.
* Convert custom types to SQL data.
* Reduce vendor-dependent details.
* Lightweight library with no dependencies besides JDK 8.

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
is invoked and the onError callback is used as the result.

**Collection Result**

*List* and *Set* are both supported specially:

```java
List<Type> list = jdbCaesar
	.query("SELECT * FROM table WHERE value1 = ? AND value2 = ?")
	.params("anothervalue", otherObject)
	.listResult( // setResult for Set
			(resultSet) -> new Type(resultSet.getString("column1"), resultSet.getInt("column2")))
	.onError(() -> Collections.emptyList()) // works well with Java 9 immutable collection factories
	.execute();
```

JdbCaesar will automatically iterate over the `ResultSet` and use the list or set mapper provided to map
each row to an element in the resulting collection.

**Result Based on Update Count or Generated Keys**

```java
Type obj = jdbCaesar.query("SELECT * FROM table WHERE value1 = ? AND value2 = ?")
	.params(1, "information")
	.updateCount((updateCount) -> new Type(updateCount)) // Type::new
	.onError(() -> null)
	.execute();
```

Use `updateGenKeys` instead of `updateCount` when generated keys are needed.

**Result Based on Entire ResultSet**

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
	}).onError(() -> null)
	.execute();
```

### Transactions

**Example**

```java
Type transactionResult;
transactionResult = jdbCaesar.transaction()
		// Optional arguments
		.readOnly(true)
		.isolation(IsolationLevel.REPEATABLE_READ)
		
		.body((querySource, controller) -> { // be sure to use this `querySource` to create queries and not the JdbCaesar instance!
		
		// main body of transaction
		// multiple queries may be run here in the same fashion
		
		}).onError(() -> null).execute();
```

The body of the transaction should use the `TransactionQuerySource` provided for running queries as part of the transaction. Additionally, the `TransactionController` may be used for fine control over

### Obtaining the Instance

To get started, use the JdbCaesarBuilder:

```java
ConnectionSource connectSource;
ExceptionHandler exHandler;
new JdbCaesarBuilder()
    .connectionSource(connectSource)
    .exceptionHandler(exHandler)
    .build();
```

Both the *ConnectionSource* and *ExceptionHandler* of the builder are required. You would not want to omit handling any exceptions.

Other settings, described later, are also available.

## Dependency Information

The dependency FQDN is:

````
space.arim.jdbcaesar:jdbcaesar:{VERSION}
````

It is available from:

```
https://mvn-repo.arim.space/lesser-gpl3/
```

A thanks to Cloudsmith for providing free repositories for FOSS.

## Other Information

### Knobs

Connection and transaction settings may be specified globally or overridden per query.

A few common settings:

* Read-only mode - All queries are writable by default
* Transaction isolation - *REPEATABLE_READ* is the default.
* Query fetch size - The default fetch size is the vendor default.

**Note that read only mode, auto commit mode, and transaction isolation are not reset once a Connection is used by JdbCaesar**. If you are using a connection pool, it is good practice to resets these options when the connection is returned to the pool. HikariCP, for example, does this automatically.

### Exception Handling

**SQLException**

JdbCaesar handles all SQLExceptions. After invoking the global `ExceptionHandler`, it continues:

* During a standalone query, the result of the query is that provided by the `onError` argument.
* During a transaction, the entire transaction is halted and rolled back, and the result of the whole transaction is that provided by its `onError` argument.
* If another SQLException is encountered while rolling back, it is added as a suppressed exception where appropriate.

Note that it is possible for the *ExceptionHandler* to be implemented as fail-fast (by throwing some RuntimeException of its own), but keep in mind the caller may be surprised by this.

**RuntimeException**

RuntimeExceptions thrown from within queries and transactions are caught and rethrown. JdbCaesar rolls back any relevant transactions.

