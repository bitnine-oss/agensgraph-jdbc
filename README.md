# AgensGraph JDBC Driver

## Introduction ##

AgensGraph JDBC Driver is based on [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/) and offers additional features to handle graph data. Thus, when users develop Java applications, there is little difference between using the API of AgensGraph JDBC Driver and PostgreSQL JDBC Driver. The only difference is that AgensGraph uses Cypher query language instead of SQL and utilizes graph data as data types such as `Vertex`, `Edge` and `Path`.

## Usage of Java Driver ##

This section shows how to use AgensGraph JDBC Driver through examples.

### Get the Driver ###

You can download the precompiled driver(jar) from [bitnine.net/downloads](http://bitnine.net/downloads) or use maven as follows.

#### Gradle ####

```
implementation group: 'net.bitnine', name: 'agensgraph-jdbc', version: '1.4.3'
```

#### Maven ####

```xml
<dependency>
  <groupId>net.bitnine</groupId>
  <artifactId>agensgraph-jdbc</artifactId>
  <version>1.4.3</version>
</dependency>
```

You may search the latest version on [The Central Repository with GroupId and ArtifactId](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22net.bitnine%22%20AND%20a%3A%22agensgraph-jdbc%22)

### Connection ###

It requires two strings to connect AgensGraph using the driver like other JDBC drivers do. These are the name of the driver class and a connection string.

* The name of the driver class is `net.bitnine.agensgraph.Driver`.
* A connection string consists of sub-protocol, server, port, and database.
  * `jdbc:agensgraph://` is a sub-protocal to use AgensGraph JDBC driver.
  * The format of a connection string is `jdbc:agensgraph://server:port/database`.

The following is an example to connect AgensGraph.

```java
import java.sql.DriverManager;
import java.sql.Connection;

public class AgensGraphTest {
  public static void main(String[] args) {
    Class.forName("net.bitnine.agensgraph.Driver");
    String connectionString = "jdbc:agensgraph://127.0.0.1:5432/agens";
	String username = "test";
	String password = "test";
    Connection conn = DriverManager.getConnection(connectionString, username, password);
    ...
  }
}
```

### Retrieving Data ###

The following is an example of retrieving graph data using `MATCH` clause. The result of the query is `vertex` type in AgensGraph. You can get the result as a `Vertex` object and get properties in it.

```java
...
import net.bitnine.agensgraph.graph.Vertex;
...
public class AgensGraphTest {
  public static void main(String[] args) {
    ...
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(
      "MATCH (:person {name: 'John'})-[:knows]-(friend:person) RETURN friend");
    while (rs.next()) {
      Vertex friend = (Vertex) rs.getObject(1);
      System.out.println(friend.getString("name"));
      System.out.println(friend.getInt("age"));
    }
  }
}
```

### Creating Data ###

The following example shows how to create a vertex with `person` label. You can build a `Jsonb` object to use it as the property map of the created vertex.

```java
...
import net.bitnine.agensgraph.util.Jsonb;
import net.bitnine.agensgraph.util.JsonbUtil;

import java.sql.PreparedStatement;
...
public class AgensGraphTest {
  public static void main(String[] args) {
    ...
    PreparedStatement pstmt = conn.prepareStatement("CREATE (:person ?)");
	Jsonb j = JsonbUtil.createObjectBuilder()
      .add("name", "John")
      .add("from", "USA")
      .add("age", 17)
      .build();
    pstmt.setObject(1, j);
    pstmt.execute();
  }
}
```

The following is the actual query to be executed.

`CREATE (:person {name: 'John', from: 'USA', age: 17})`

### Using Named Parameter ###

```java
...
import net.bitnine.agensgraph.jdbc.AgConnection;
import net.bitnine.agensgraph.jdbc.AgPreparedStatement;
...
public class AgensGraphTest {
  public static void main(String[] args) {
    ...
    aconn = conn.unwrap(AgConnection.class);
    AgPreparedStatement apstmt = aconn.prepareNamedParameterStatement("CREATE ($data)");
	Jsonb j = JsonbUtil.createObjectBuilder()
      .add("id", 7)
      .add("enabled", true)
      .add("day", JsonbUtil.createArray("Sat", "Sun"))
      .build();
    apstmt.setObject("data", j);
    apstmt.execute();
  }
}
```

## Graph Object Classes ##

This section is a brief explanation of Java class to support graph data.

| Class        | Description |
| ------------ | ----------- |
| `GraphId`    | It is a java class corresponding to `graphid` type in AgensGraph. |
| `Vertex`     | It is a java class corresponding to `vertex` type in AgensGraph. It supports accessing methods for the label and properties. |
| `Edge`       | It is a java class corresponding to `edge` type in AgensGraph. It supports accessing methods for the label and properties. |
| `Path`       | It is a java class corresponding to `graphpath` type in AgensGraph. It supports methods for the length of the path, and accessing for vertexes and edges in the path. |
| `Jsonb`      | It is a java class corresponding to `jsonb` type in AgensGraph. `Vertex` and `Edge` use `Jsonb` to store their properties. It offers accessing methods for JSON scalar, array, and object type. |
| `JsonbUtil`  | It offers various methods to create `Jsonb` object as shown in the above `CREATE` example. |
