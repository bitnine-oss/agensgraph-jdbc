/*
 * Copyright (c) 2014-2018, Bitnine Inc.
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

package net.bitnine.agensgraph.util;

import org.json.simple.JSONObject;

public class JsonbObjectBuilder {
    private JSONObject obj;

    public JsonbObjectBuilder() {
        this.obj = new JSONObject();
    }

    JSONObject getObject() {
        return obj;
    }

    public JsonbObjectBuilder add(String name, String value) {
        obj.put(name, value);
        return this;
    }

    public JsonbObjectBuilder add(String name, long value) {
        obj.put(name, value);
        return this;
    }

    public JsonbObjectBuilder add(String name, double value) {
        obj.put(name, value);
        return this;
    }

    public JsonbObjectBuilder add(String name, boolean value) {
        obj.put(name, value);
        return this;
    }

    public JsonbObjectBuilder addNull(String name) {
        obj.put(name, null);
        return this;
    }

    public JsonbObjectBuilder add(String name, Jsonb j) {
        obj.put(name, j.getJsonValue());
        return this;
    }

    public JsonbObjectBuilder add(String name, Object value) {
        value = JsonbUtil.filterValueType(value);
        obj.put(name, value);
        return this;
    }

    public JsonbObjectBuilder add(String name, JsonbArrayBuilder builder) {
        obj.put(name, builder.getArray());
        return this;
    }

    public JsonbObjectBuilder add(String name, JsonbObjectBuilder builder) {
        obj.put(name, builder.getObject());
        return this;
    }

    public Jsonb build() {
        return new Jsonb(obj);
    }
}
