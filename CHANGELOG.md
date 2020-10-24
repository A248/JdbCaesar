
## Changelog

All versions and their changes detailed.

### 0.6.0

* Overhauls exception handling to be fail-fast by default.
* JdbCaesarBuilder no longer requires an exception handler.
* Minor performance gain when rolling back transactions.
* Migrating:
    * ConnectionSource is removed in favour of javax.sql.DataSource
    * `execute()` is fail-fast and may throw `UncheckedSQLException`
    * `onError(() -> {}).execute()` is removed. Use `executeOrGet(() -> {})` to retain behaviour. Alternatively, use `execute()`.
    * JdbCaesarBuilder is moved, so updating imports is necessary.
    * All other changes should be fixed by a recompile. Intermediate builder objects have been changed, but these are rarely referenced.

### 0.5.0

* Adds all features from snapshot versions since release 0.4.0:
    * JdbCaesarBuilder#rewrapExceptions
    * Updatable and scrollable ResultSet support
* Auto-commit support
    * No extra configuration is necessary, but note that JdbCaesar will disable auto-commit for its own purposes while executing queries, but auto-commit will not be re-enabled. See the README for more information
* More powerful transaction support using TransactionBody/TransactionController
* Improvements to exception handling
* Migrating:
    * No source-incompatible changes, but a recompile may be necessary
    * Deprecations where noted

### 0.5.0-SNAPSHOT

* Read-only override for individual queries
* Support for updatable ResultSets using ResultSetConcurrency.CONCUR_UPDATABLE
* Support for scrollable ResultSets using ResultSetType.SCROLL_INSENSITIVE or SCROLL_SENSITIVE

### 0.4.1-SNAPSHOT

* Adds JdbCaesarBuilder#rewrapExceptions

### 0.4.0

* First full release
