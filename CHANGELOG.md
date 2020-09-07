
## Changelog

All versions and their changes, in reverse chronological order of date made available.

Release versions typically include the features introduced by past snapshot versions since the last release.

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
