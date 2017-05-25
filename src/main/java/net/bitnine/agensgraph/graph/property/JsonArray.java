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

public class JsonArray extends Jsonb {
    private JSONArray array;

    public JsonArray() {
        array = new JSONArray();
        setJsonValue(this);
    }

    public JsonArray(String s) {
        array = (JSONArray) JSONValue.parse(s);
        if (array == null)
            throw new IllegalArgumentException("invalid json array format string");
        setJsonValue(this);
    }

    protected JsonArray(JSONArray json) {
        array = json;
        setJsonValue(this);
    }

    public static JsonArray create() {
        return new JsonArray();
    }

    public static JsonArray create(String s) {
        return new JsonArray(s);
    }

    static JsonArray createArray(boolean... values) {
        JsonArray array = new JsonArray();
        for (boolean value : values) {
            array.add(value);
        }
        return array;
    }

    static JsonArray createArray(int... values) {
        JsonArray array = new JsonArray();
        for (int value : values) {
            array.add(value);
        }
        return array;
    }

    static JsonArray createArray(long... values) {
        JsonArray array = new JsonArray();
        for (long value : values) {
            array.add(value);
        }
        return array;
    }

    static JsonArray createArray(double... values) {
        JsonArray array = new JsonArray();
        for (double value : values) {
            array.add(value);
        }
        return array;
    }

    static JsonArray createArray(String... values) {
        JsonArray array = new JsonArray();
        for (String value : values) {
            array.add(value);
        }
        return array;
    }

    public static JsonArray create(Object... values) {
        JsonArray array = new JsonArray();
        for (Object value : values)
            array.add(value);
        return array;
    }

    public static JsonArray create(List<?> list) {
        JsonArray array = new JsonArray();
        if (list == null || list.isEmpty())
            return array;
        for (Object value : list)
            array.add(value);
        return array;
    }

    public boolean isEmpty() {
        return array.isEmpty();
    }

    public boolean equals(Object o) {
        return array.equals(o);
    }

    public List<Object> toList() {
        return (List<Object>) array.clone();
    }

    public Object get(int index) {
        return array.get(index);
    }

    public Object get(int index, Object defaultValue) {
        return isOutOfBounds(index) || isNull(index) ? defaultValue : get(index);
    }

    public boolean isNull(int index) { return null == get(index); }

    public boolean isOutOfBounds(int index) { return index < 0 || index >= array.size(); }

    public Boolean getBoolean(int index) {
        return (Boolean) get(index);
    }

    public Boolean getBoolean(int index, Boolean defaultValue) {
        return isOutOfBounds(index) || isNull(index) ? defaultValue : getBoolean(index);
    }

    public Integer getInt(int index) {
        Long value = getLong(index);
        return (value == null) ? null : value.intValue();
    }

    public Integer getInt(int index, Integer defaultValue) {
        return isOutOfBounds(index) || isNull(index) ? defaultValue : getInt(index);
    }

    public Long getLong(int index) {
        return (Long) get(index);
    }

    public Long getLong(int index, Long defaultValue) {
        return isOutOfBounds(index) || isNull(index) ? defaultValue : getLong(index);
    }

    public Double getDouble(int index) {
        return (Double) get(index);
    }

    public Double getDouble(int index, Double defaultValue) {
        return isOutOfBounds(index) || isNull(index) ? defaultValue : getDouble(index);
    }

    public String getString(int index) {
        return (String) get(index);
    }

    public String getString(int index, String defaultValue) {
        return isOutOfBounds(index) || isNull(index) ? defaultValue : getString(index);
    }

    public JsonObject getObject(int index) {
        return new JsonObject((JSONObject) get(index));
    }

    public JsonObject getObject(int index, JsonObject defaultValue) {
        return isOutOfBounds(index) || isNull(index) ? defaultValue : getObject(index);
    }

    public JsonArray getArray(int index) {
        return new JsonArray((JSONArray) get(index));
    }

    public JsonArray getArray(int index, JsonArray defaultValue) {
        return isOutOfBounds(index) || isNull(index) ? defaultValue : getArray(index);
    }

    public Iterator<Object> iterator() {
        ArrayList<Object> l = new ArrayList<>();
        for (Object elem : array) {
            if (elem instanceof JSONObject) {
                l.add(new JsonObject((JSONObject) elem));
            } else if (elem instanceof JSONArray) {
                l.add(new JsonArray((JSONArray) elem));
            } else {
                l.add(elem);
            }
        }
        return l.iterator();
    }

    public int size() {
        return array.size();
    }

    public String toString() {
        return array.toString();
    }

    public JsonArray add(boolean value) {
        array.add(value);
        return this;
    }

    public JsonArray add(int value) {
        array.add(new Long(value));
        return this;
    }

    public JsonArray add(long value) {
        array.add(value);
        return this;
    }

    public JsonArray add(double value) {
        array.add(value);
        return this;
    }

    public JsonArray add(String value) {
        array.add(value);
        return this;
    }

    public JsonArray add(JsonArray value) {
        array.add(value);
        return this;
    }

    public JsonArray add(JsonObject value) {
        array.add(value);
        return this;
    }

    public JsonArray addNull() {
        array.add(null);
        return this;
    }

    public JsonArray add(Object value) {
        if (isJsonValue(value))
            array.add(value);
        else if (value instanceof Map)
            array.add(JsonObject.create((Map<String, ?>) value));
        else if (value instanceof List)
            array.add(JsonArray.create((List<?>) value));
        else if (value instanceof boolean[])
            array.add(JsonArray.createArray((boolean[]) value));
        else if (value instanceof int[])
            array.add(JsonArray.createArray((int[]) value));
        else if (value instanceof long[])
            array.add(JsonArray.createArray((long[]) value));
        else if (value instanceof double[])
            array.add(JsonArray.createArray((double[]) value));
        else if (value instanceof String[])
            array.add(JsonArray.createArray((String[]) value));
        else if (value instanceof Object[])
            array.add(JsonArray.create((Object[]) value));
        else
            throw new IllegalArgumentException("invalid json value type: " + value.getClass());
        return this;
    }
}