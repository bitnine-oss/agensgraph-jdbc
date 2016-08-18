package net.bitnine.agensgraph.graph.property;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.postgresql.util.PGobject;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

public class Jsonb extends PGobject implements Serializable, Closeable {

    private Object jsonValue;

    public Jsonb() {
        setType("jsonb");
    }

    @Override
    public void setValue(String value) throws SQLException {
        Object o = JSONValue.parse(value);
        if (o instanceof JSONObject) jsonValue = new JsonObject((JSONObject)o);
        else jsonValue = new JsonArray((JSONArray)o);
    }

    @Override
    public String getValue() {
        return jsonValue.toString();
    }

    @Override
    public void close() throws IOException {

    }

    public JsonObject jo() {
        return (JsonObject)jsonValue;
    }

    public JsonArray ja() {
        return (JsonArray)jsonValue;
    }
}
