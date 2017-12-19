1.4.1 / 2017-12-19
------------------
* `JsonbUtil`, `JsonbArrayBuilder`, and `JsonbObjectBuilder` now support `Iterable` and `Map` for their values.
* Support `containsKey()` and getters with default value

1.4.0 / 2017-10-14
------------------
* Improve `Vertex`, `Edge`, and `Path` class
* Fix bugs in parsing routine for `graphpath` type
  * Merge the logic into `Path` and remove `TopCommaTokenizer`
* Change `GID` to `GraphId` and improve it
* Improve `Jsonb` and move it to `net.bitnine.agensgraph.util` package
* Introduce `AgConnection`, `AgStatement`, and `AgResultSet`
  * Remove surrounding '"' character from JSON string
  * Handle JSON values properly
* Improve named parameter feature
  * Introduce `AgPreparedStatement`
  * Merge features in `NamedParameterStatement` into `AgPreparedStatement`
  * Improve parsing routine for named parameters
* Rewrite the API to create JSON value
  * Introduce `JsonUtil`, `JsonbArrayBuilder`, and `JsonbObjectBuilder`
  * Remove `JsonArray` and `JsonObject`
* Remove unused classes
  * `AbstractDatabaseMetaData`
  * `AgensGraphConnection`
  * `AgensGraphDatabaseMetaData`
* Remove all existing tests and write unit tests
* Upgrade PostgreSQL JDBC Driver from 42.1.1.jre7 to 42.1.4.jre7
* Change version from 1.3.2 to 1.4.0
* Update README

1.3.2 / 2017-06-26
------------------
* Support for named parameters

1.3.1 / 2017-05-23
------------------
* Support for graphid binding
* Add get(String name) to JsonObject class
* Add get(int index) and toList() methods to JsonArray class

1.3.0 / 2017-05-15
------------------
* Change PG-JDBC 94.1208.jre7 to 42.1.1.jre7

1.2.1 / 2017-05-08
------------------
* Support for conversion from primitive array to JsonArray
* Added property access methods for vertex and edge objects
* Added property access methods with a default value argument

1.2.0 / 2017-04-12
------------------
* Remove "[", "]" from GID's text representation
* Implicit casting support for List and Map in `create` static methods
