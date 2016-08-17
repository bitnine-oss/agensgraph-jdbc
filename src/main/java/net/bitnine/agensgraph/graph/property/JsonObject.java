package net.bitnine.agensgraph.graph.property;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ktlee on 16. 8. 16.
 */
@SuppressWarnings("ALL")
public class JsonObject {
    private JSONObject props;

    public JsonObject(String s) {
        props = (JSONObject) JSONValue.parse(s);
    }

    protected JsonObject(JSONObject json) {
        props = json;
    }

    public boolean containsKey(String name) {
        return props.containsKey(name);
    }

    public boolean containsValue(String value) {
        return props.containsValue(value);
    }

    public boolean equals(Object o) {
        return props.equals(o);
    }

    public boolean isEmpty() {
        return props.isEmpty();
    }

    private Object get(String name) {
        return props.get(name);
    }

    public JsonObject getObject(String name) {
        return new JsonObject((JSONObject)get(name));
    }

    public JsonArray getArray(String name) {
        return new JsonArray((JSONArray)get(name));
    }

    public String getString(String name) {
        return (String)get(name);
    }

    public Boolean getBoolean(String name) {
        return (Boolean)get(name);
    }

    public Integer getInt(String name) {
        return getLong(name).intValue();
    }

    public Long getLong(String name) {
        return (Long)get(name);
    }

    public Double getDouble(String name) {
        return (Double)get(name);
    }

    public Set<String> getNames() {
        return (Set<String>)props.keySet();
    }

    public int size() {
        return props.size();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> newMap = new HashMap();
        for (String name : (Set<String>)props.keySet()) {
            Object value = props.get(name);
            if (value instanceof JSONObject) {
                newMap.put(name, new JsonObject((JSONObject)value));
            }
            else if (value instanceof JSONArray) {
                newMap.put(name, new JsonArray((JSONArray)value));
            }
            else {
                newMap.put(name, value);
            }
        }
        return newMap;
    }

    public String toString() {
        return props.toString();
    }
}
