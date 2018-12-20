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

/**
 * A builder for creating Jsonb array.
 * This class initializes a Jsonb array and provides methods to add values to the array
 * and to return the resulting array.
 * The methods in this class can be chained to add multiple values to the array.
 */
public class JsonbArrayBuilder {
    private JSONArray arr;

    /**
     * Initializes a Jsonb array.
     */
    public JsonbArrayBuilder() {
        this.arr = new JSONArray();
    }

    /**
     * Returns the current array.
     *
     * @return the current array
     */
    JSONArray getArray() {
        return arr;
    }

    /**
     * Adds a value to the array as a string.
     *
     * @param value the string value
     * @return this array builder
     */
    public JsonbArrayBuilder add(String value) {
        arr.add(value);
        return this;
    }

    /**
     * Adds a value to the array as a long.
     *
     * @param value the long value
     * @return this array builder
     */
    public JsonbArrayBuilder add(long value) {
        arr.add(value);
        return this;
    }

    /**
     * Adds a value to the array as a double.
     *
     * @param value the double value
     * @return this array builder
     */
    public JsonbArrayBuilder add(double value) {
        arr.add(value);
        return this;
    }

    /**
     * Adds a value to the array as a boolean.
     *
     * @param value the boolean value
     * @return this array builder
     */
    public JsonbArrayBuilder add(boolean value) {
        arr.add(value);
        return this;
    }

    /**
     * Adds a value to the array as a null.
     *
     * @return this array builder
     */
    public JsonbArrayBuilder addNull() {
        arr.add(null);
        return this;
    }

    /**
     * Adds a value to the array as a Jsonb.
     *
     * @param j the Jsonb value
     * @return this array builder
     */
    public JsonbArrayBuilder add(Jsonb j) {
        arr.add(j.getJsonValue());
        return this;
    }

    /**
     * Adds a value to the array as an object.
     *
     * @param value the object value
     * @return this array builder
     */
    public JsonbArrayBuilder add(Object value) {
        value = JsonbUtil.filterValueType(value);
        arr.add(value);
        return this;
    }

    /**
     * Adds a JsonbArray from an array builder to the array.
     *
     * @param builder the array builder
     * @return this array builder
     */
    public JsonbArrayBuilder add(JsonbArrayBuilder builder) {
        arr.add(builder.getArray());
        return this;
    }

    /**
     * Adds a JsonbObject from an array builder to the array.
     *
     * @param builder the object builder
     * @return this array builder
     */
    public JsonbArrayBuilder add(JsonbObjectBuilder builder) {
        arr.add(builder.getObject());
        return this;
    }

    /**
     * Returns the Jsonb array associated with this array builder.
     *
     * @return the Jsonb array that is being built
     */
    public Jsonb build() {
        return new Jsonb(arr);
    }
}
