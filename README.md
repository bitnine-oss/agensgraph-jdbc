<img src="http://cfile26.uf.tistory.com/image/251E994857553C91236F07" />

# Agens Graph JDBC driver

## Introduction ##

Agens Graph's JDBC driver is based on PostgreSQL JDBC Driver and offering Java Driver for Java application developer. Thus, When users develop some application, there is little difference between using the API of Agens Graph Java Driver and Postgres JDBC Driver. The only difference is that Agens Graph uses Cypher query language instead of SQL and utilizes Graph Data as data type like Vertex, Edge and Path.

## Usage of Java Driver ##

This section will handle how to use Java Driver through examples.

### Connection ###

It requires two information to connect Agens Graph using Java Driver like as the way that other JDBC Drivers do. These are the name of class to be loading Java Driver and the connection string.

* The name of class is `net.bitnine.agensgraph.Driver`.
* connection string consists of  sub-protocol, server, port, database.
  * `jdbc:agensgraph://`is a sub-protocal to use particular Driver and a hold value.
  * It is written in format of `jdbc:agensgraph://server:port/database` including sub-protocol.

The following is an example code to connect Agens Graph. It is connected to the Agens Graph through Connection object and ready to perform the query.

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

This is an example to export Graph data using MATCH.

A Cypher query is executed using `executeQuery()`. The output is ResultSet object, which is the same output format in JDBC. 
The output of query is the ‘vertex’ type of Agens Graph. Java Driver returns this output as Vertex instance. Because Vertex class is a sub-class of JsonObject, users can obtain information from the property fields.

```java
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import net.bitnine.agensgraph.graph.Vertex;

public class AgensGraphTest {
  public static void main(String[] args) {
    ...
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(
        "MATCH (:Person {name: 'John'})-[:knows]-(friend:Person)" +
        "RETURN friend");
    while (rs.next()) {
      Vertex friend = (Vertex)rs.getObject(1);
      System.out.println(friend.getProperty().getString("name"));
      System.out.println(friend.getProperty().getInt("age"));
    }
  }
}
```

### Create Data ###

The following is an example, which is inputting a vertex with Person label to Agens Graph. 
Users can input a property of a vertex using strings in Cypher queries. They are also able to be binded after making JsonObject like a following example

```java
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;

import net.bitnine.agensgraph.graph.property.JsonObject;

public class AgensGraphTest {
  public static void main(String[] args) {
    ...
    PreparedStatement pstmt = con.prepareStatement("CREATE (:Person ?)");
    JsonObject john = new JsonObject();
    john.put("name", "John");
    john.put("from", "USA");
    john.put("age", 17);
    pstmt.setObject(1, john);
    pstmt.execute();
  }
}
```

The following is converted format to using string.

`CREATE (:Person {name: 'John', from: 'USA', age: 17})`

## Graph Classes ##

This section is a brief explanation of Java class to support graph data.

| Class      | Description |
| ---------- | ----------- |
| Vertex     |It is a java class corresponding to a `vertex` type in the Agens Graph. It supports accessing methods for the label and properties. |
| Edge       | It is a java class corresponding to a `edge` type in the Agens Graph. It supports accessing methods for the label and properties. |
| Path       | It is a java class corresponding to a `graphpath` type in the Agens Graph. It supports methods for the length of the path, and accessing for vertexes and edges in the path. |
| JsonObject | It is a java class corresponding to the property field of Vertex and Edge class. Property field is JSON class. It offers accessing methods for property values by name. As the above CREATE example, it offers methods to create or modify JSON object. |
| JsonArray  | It is a java class in corresponding with JSON array. It offers accessing methods for the element of JSON array using index. It also offers various methods to create or modify of JSON array.|
