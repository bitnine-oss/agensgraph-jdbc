/*
 * Copyright (c) 2014-2016, Bitnine Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.bitnine.agensgraph.graph.property;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.*;

public class JsonObject extends Jsonb {
    JSONObject props;

    public JsonObject() {
        props = new JSONObject();
        setJsonValue(this);
    }

    public JsonObject(String s) {
        props = (JSONObject) JSONValue.parse(s);
        if (props == null)
            throw new IllegalArgumentException("invalid json object format string");
        setJsonValue(this);
    }

    JsonObject(JSONObject json) {
        props = json;
        setJsonValue(this);
    }

    public static JsonObject create() {
        return new JsonObject();
    }

    public static JsonObject create(String s) {
        return new JsonObject(s);
    }

    public static JsonObject create(Map<String, ?> map) {
        if (map == null || map.isEmpty())
            return new JsonObject();

        JsonObject jobj = new JsonObject();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = entry.getKey();
            if (key == null)
                throw new IllegalArgumentException("'null' key is not allowed in JsonObject");
            jobj.put(key, entry.getValue());
        }

        return jobj;
    }

    public boolean containsKey(String name) {
        return props.containsKey(name);
    }

    public boolean containsValue(Object value) {
        return props.containsValue(value);
    }

    public boolean equals(Object o) {
        return props.equals(o);
    }

    public boolean isEmpty() {
        return props.isEmpty();
    }

    public Object get(String name) {
        return props.get(name);
    }

    public boolean isNull(String name) { return null == props.get(name); }

    public Boolean getBoolean(String name) {
        return (Boolean)get(name);
    }

    public Boolean getBoolean(String name, Boolean defaultValue) {
        return isNull(name) ? defaultValue : getBoolean(name);
    }

    public Integer getInt(String name) {
        Long value = getLong(name);
        return (value == null) ? null : value.intValue();
    }

    public Integer getInt(String name, Integer defaultValue) {
        return isNull(name) ? defaultValue : getInt(name);
    }

    public Long getLong(String name) {
        return (Long)get(name);
    }

    public Long getLong(String name, Long defaultValue) {
        return isNull(name) ? defaultValue : getLong(name);
    }

    public Double getDouble(String name) {
        return (Double)get(name);
    }

    public Double getDouble(String name, Double defaultValue) {
        return isNull(name) ? defaultValue : getDouble(name);
    }

    public String getString(String name) {
        return (String)get(name);
    }

    public String getString(String name, String defaultValue) {
        return isNull(name) ? defaultValue : getString(name);
    }

    public JsonObject getObject(String name) {
        return new JsonObject((JSONObject)get(name));
    }

    public JsonObject getObject(String name, JsonObject defaultValue) {
        return isNull(name) ? defaultValue : getObject(name);
    }

    public JsonArray getArray(String name) {
        return new JsonArray((JSONArray)get(name));
    }

    public JsonArray getArray(String name, JsonArray defaultValue) {
        return isNull(name) ? defaultValue : getArray(name);
    }

    public Set<String> getNames() {
        return (Set<String>)props.keySet();
    }

    public int size() {
        return props.size();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> newMap = new HashMap<>();
        for (String name : getNames()) {
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

    public JsonObject put(String name, boolean value) {
        props.put(name, value);
        return this;
    }

    public JsonObject put(String name, int value) {
        props.put(name, new Long(value));
        return this;
    }

    public JsonObject put(String name, long value) {
        props.put(name, value);
        return this;
    }

    public JsonObject put(String name, double value) {
        props.put(name, value);
        return this;
    }

    public JsonObject put(String name, String value) {
        props.put(name, value);
        return this;
    }

    public JsonObject put(String name, JsonObject value) {
        props.put(name, value);
        return this;
    }

    public JsonObject put(String name, JsonArray value) {
        props.put(name, value);
        return this;
    }

    public JsonObject putNull(String name) {
        props.put(name, null);
        return this;
    }

    public JsonObject put(String key, Object value) {
        if (isJsonValue(value))
            props.put(key, value);
        else if (value instanceof Map)
            props.put(key, JsonObject.create((Map<String, ?>) value));
        else if (value instanceof List)
            props.put(key, JsonArray.create((List<?>) value));
        else if (value instanceof boolean[])
            props.put(key, JsonArray.createArray((boolean[]) value));
        else if (value instanceof int[])
            props.put(key, JsonArray.createArray((int[]) value));
        else if (value instanceof long[])
            props.put(key, JsonArray.createArray((long[]) value));
        else if (value instanceof double[])
            props.put(key, JsonArray.createArray((double[]) value));
        else if (value instanceof String[])
            props.put(key, JsonArray.createArray((String[]) value));
        else if (value instanceof Object[])
            props.put(key, JsonArray.create((Object[]) value));
        else
            throw new IllegalArgumentException("invalid json value type: " + value.getClass());
        return this;
    }
}
