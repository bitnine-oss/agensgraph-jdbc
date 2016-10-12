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
import org.postgresql.util.PGobject;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.regex.Matcher;

public class Jsonb extends PGobject implements Serializable, Closeable {

    private Object jsonValue;
    private JsonType type;

    public Jsonb() {
        setType("jsonb");
    }

    public Jsonb(Object value) {
        setType("jsonb");
        setJsonValue(value);
    }

    @Override
    public void setValue(String value) throws SQLException {
        Object o = JSONValue.parse(value);
        if (o instanceof JSONObject) {
            jsonValue = new JsonObject((JSONObject)o);
        }
        else if (o instanceof JSONArray) {
            jsonValue = new JsonArray((JSONArray)o);
        }
        else {
            jsonValue = o;
        }
        setJsonType(jsonValue);
    }

    private void setJsonType(Object value) {
        if (value == null) {
            type = JsonType.NULL;
        }
        if (value instanceof JsonObject) {
            type = JsonType.OBJECT;
        }
        else if (value instanceof JsonArray) {
            type = JsonType.ARRAY;
        }
        else if (value instanceof String) {
            type = JsonType.STRING;
        }
        else if (value instanceof Integer) {
            type = JsonType.LONG;
        }
        else if (value instanceof Long) {
            type = JsonType.LONG;
        }
        else if (value instanceof Double) {
            type = JsonType.DOUBLE;
        }
        else if (value instanceof Boolean) {
            type = JsonType.BOOLEAN;
        }
        else {
            throw new IllegalArgumentException("invalid json type");
        }
    }

    public void setJsonValue(Object value) {
        setJsonType(value);
        jsonValue = value;
    }

    @Override
    public String getValue() {
        if (JsonType.STRING == getJsonType())
            return "\""+ jsonValue.toString().replaceAll("\"", Matcher.quoteReplacement("\\\"")) + "\"";
        else
            return jsonValue.toString();
    }

    @Override
    public void close() throws IOException {

    }

    public JsonObject jo() {
        return (JsonObject)getJsonValue();
    }

    public JsonArray ja() {
        return (JsonArray)getJsonValue();
    }

    public JsonObject getJsonObject() {
        return jo();
    }

    public JsonArray getJsonArray() {
        return ja();
    }

    public String getString() {
        return (String)getJsonValue();
    }

    public Integer getInt() {
        Long value = getLong();
        return (value == null) ? null : value.intValue();
    }

    public Long getLong() {
        return (Long)getJsonValue();
    }

    public Double getDouble() {
        return (Double)getJsonValue();
    }

    public Object getJsonValue() {
        return jsonValue;
    }

    public JsonType getJsonType() {
        return type;
    }

    public static boolean isJsonValue(Object value) {
        return (value == null ||
                value instanceof JsonObject ||
                value instanceof JsonArray ||
                value instanceof String ||
                value instanceof Integer ||
                value instanceof Long ||
                value instanceof Double ||
                value instanceof Boolean);
    }

    public String toString() {
        return getValue();
    }
}
