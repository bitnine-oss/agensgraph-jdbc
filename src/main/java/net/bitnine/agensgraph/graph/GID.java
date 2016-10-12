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

package net.bitnine.agensgraph.graph;

public class GID {
    /**
     * ID of base object
     */
    private int oid;

    /**
     * ID in the base object
     */
    private int id;

    GID(int oid, int id) {
        this.oid = oid;
        this.id = id;
    }

    GID(String oid, String id) {
        this(Integer.parseInt(oid), Integer.parseInt(id));
    }

    public String toString() {
        return "[" + oid + "." + id + "]";
    }

    public int getOid() {
        return oid;
    }

    public int getId() {
        return id;
    }
}
