
# JdbCaesar

Conquer the verbosity of plain JDBC.

## Introduction

Normal `java.sql` is known for cumbersome Connection, PreparedStatement, and ResultSet declarations. This often results in several try-with-resources and nested try blocks. However, most code is simply intended to map a SQL result to some POJO or Collection.

JdbCaesar is a simple yet effective alternative substantially reducing the amount of boilerplate required in a typical query. Fluent builders allow easily executing queries and mapping to a result.

### Features

* Easily execute single queries. Never again write database-related try-with-resources blocks.
* Don't sacrifice useful tools such as generated keys, updatable result sets, fetch size control, etc.
* Made to use functional code. Unchecked rather than checked exceptions.
* Fully parameterised queries.
* Support for transactions in the same manner as the rest of the library.
* Convert custom types to SQL data.
* Reduce vendor-dependent details.
* No dependencies.
* Java 8 compatible while also providing module-info.

## Usage

### Querying

**Collection Result**

*List* and *Set* are both supported specially:

```java
List<Type> list = jdbCaesar
	.query("SELECT * FROM table WHERE value1 = ? AND value2 = ?")
	.params("anothervalue", otherObject)
	.listResult( // setResult for Set
			(resultSet) -> new Type(resultSet.getString("column1"), resultSet.getInt("column2")))
	.execute();
```

JdbCaesar will automatically iterate over the `ResultSet` and use the list or set mapper provided to map
each row to an element in the resulting collection.

If a SQLException occurs, it is wrapped in UncheckedSQLException, which is rethrown.

**Single Result**

```java
Type obj = jdbCaesar
	.query("SELECT * FROM table WHERE value1 = ? AND value2 = ?")
	.params("value", 1) // varargs
	.singleResult( // result mapper body
			(resultSet) -> new Type(resultSet.getString("column1"), resultSet.getInt("column2")))
	.execute();
```

If no rows were returned from the database, `execute()` returns null.

**Result Based on Update Count or Generated Keys**

```java
Type obj = jdbCaesar.query("SELECT * FROM table WHERE value1 = ? AND value2 = ?")
	.params(1, "information")
	.updateCount((updateCount) -> new Type(updateCount)) // Type::new
	.execute();
```

Use `updateGenKeys` instead of `updateCount` when generated keys are needed.

**Result Based on Entire ResultSet**

```java
Type obj = jdbCaesar
	.query("SELECT * FROM table WHERE value1 = ? AND value2 = ?")
	.params("value", 1) // varargs
	.totalResult((resultSet) -> {
	    // Use entire ResultSet here
	    int count;
	    while (resultSet.next()) {
	        count++; // just an example, use COUNT(x) for real
	    }
	    return new Type(count);
	}).execute();
```

### Transactions

**Example**

```java
Type transactionResult;
transactionResult = jdbCaesar.transaction()
		// Optional arguments
		.readOnly(true)
		.isolation(IsolationLevel.REPEATABLE_READ)
		
		.body((querySource, controller) -> { // be sure to use this `querySource` to create queries and not the JdbCaesar instance
		
		// main body of transaction
		// multiple queries may be run here in the same fashion
		
		}).execute();
```

The body of the transaction should use the `TransactionQuerySource` provided for running queries as part of the transaction. Additionally, the `TransactionController` may be used for finer control, including rolling back and save points.

Once the body of the transaction returns, the transaction is committed automatically.

### Obtaining the Instance

To get started, use the JdbCaesarBuilder:

```java
DataSource dataSource;
new JdbCaesarBuilder()
    .dataSource(dataSource)
    .build();
```

Only the *DataSource* of the builder is required. All other settings are optional and described further below.

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

**Note that read only mode, auto commit mode, and transaction isolation are not reset once a Connection is used by JdbCaesar** from its DataSource. If you are using a connection pool, it is good practice have the pool reset these options when the connection is returned. HikariCP, for example, does this automatically.

### Exception Handling

**SQLException**

When a SQLException occurs, JdbCaesar rolls back any enclosing transactions, then rethrows the SQLExceptions in UncheckedSQLException.

If another SQLException is encountered while rolling back, it is added as a suppressed exception where appropriate.

**RuntimeException**

When any other RuntimeException occurs, again JdbCaesar rolls back any relevant transactions. Then the exception is rethrown.

