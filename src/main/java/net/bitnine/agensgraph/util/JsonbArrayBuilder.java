/*
 * Copyright (c) 2014-2018 Bitnine, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
