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

import org.json.simple.JSONArray;

public class JsonbArrayBuilder {
    private JSONArray arr;

    public JsonbArrayBuilder() {
        this.arr = new JSONArray();
    }

    JSONArray getArray() {
        return arr;
    }

    public JsonbArrayBuilder add(String value) {
        arr.add(value);
        return this;
    }

    public JsonbArrayBuilder add(long value) {
        arr.add(value);
        return this;
    }

    public JsonbArrayBuilder add(double value) {
        arr.add(value);
        return this;
    }

    public JsonbArrayBuilder add(boolean value) {
        arr.add(value);
        return this;
    }

    public JsonbArrayBuilder addNull() {
        arr.add(null);
        return this;
    }

    public JsonbArrayBuilder add(Jsonb j) {
        arr.add(j.getJsonValue());
        return this;
    }

    public JsonbArrayBuilder add(Object value) {
        value = JsonbUtil.filterValueType(value);
        arr.add(value);
        return this;
    }

    public JsonbArrayBuilder add(JsonbArrayBuilder builder) {
        arr.add(builder.getArray());
        return this;
    }

    public JsonbArrayBuilder add(JsonbObjectBuilder builder) {
        arr.add(builder.getObject());
        return this;
    }

    public Jsonb build() {
        return new Jsonb(arr);
    }
}
