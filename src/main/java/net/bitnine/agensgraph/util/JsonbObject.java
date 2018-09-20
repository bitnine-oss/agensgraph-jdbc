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

/**
 * This interface defines the JsonbObject.
 */
public interface JsonbObject {
    /**
     * Returns a set of keys.
     *
     * @return a set of keys
     */
    Iterable<String> getKeys();

    /**
     * Returns true if the given key is contained.
     *
     * @param key the given key
     * @return true if the given key is contained
     */
    boolean containsKey(String key);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @return the string value stored at the key
     */
    String getString(String key);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @param defaultValue returned the default value if the value stored at the key is null
     * @return the string value stored at the key
     */
    String getString(String key, String defaultValue);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @return the int value stored at the key
     */
    int getInt(String key);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @param defaultValue returned the default value if the value stored at the key is null
     * @return the int value stored at the key
     */
    int getInt(String key, int defaultValue);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @return the long value stored at the key
     */
    long getLong(String key);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @param defaultValue returned the default value if the value stored at the key is null
     * @return the long value stored at the key
     */
    long getLong(String key, long defaultValue);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @return the double value stored at the key
     */
    double getDouble(String key);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @param defaultValue returned the default value if the value stored at the key is null
     * @return the double value stored at the key
     */
    double getDouble(String key, double defaultValue);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @return the boolean value stored at the key
     */
    boolean getBoolean(String key);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @param defaultValue returned the default value if the value stored at the key is null
     * @return the boolean value stored at the key
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @return the array value stored at the key
     */
    Jsonb getArray(String key);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @return the object value stored at the key
     */
    Jsonb getObject(String key);

    /**
     * Returns true if the value stored at the key is null.
     *
     * @param key the given key
     * @return true if the value stored at the key is null
     */
    boolean isNull(String key);
}
