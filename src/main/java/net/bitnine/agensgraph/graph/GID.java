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

import org.postgresql.util.GT;
import org.postgresql.util.PGobject;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

public class GID extends PGobject implements Serializable, Closeable {
    /**
     * ID of base object
     */
    private int oid;

    /**
     * ID in the base object
     */
    private int id;

    public GID() {
        setType("graphid");
    }

    public GID(int oid, int id) {
        setType("graphid");
        setOid(oid);
        setId(id);
    }

    public GID(String s) throws SQLException {
        setType("graphid");
        setValue(s);
    }

    GID(String oid, String id) throws SQLException {
        this(Integer.parseInt(oid), Integer.parseInt(id));
    }

    public String toString() {
        return oid + "." + id;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) { this.oid = oid; }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public void setValue(String s) throws SQLException {
        String[] ids = s.split("\\.");
        if (ids.length == 2) {
            setOid(Integer.parseInt(ids[0]));
            setId(Integer.parseInt(ids[1]));
        }
        else {
            throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", new Object[]{type, s}),
                    PSQLState.DATA_TYPE_MISMATCH);
        }
    }

    public String getValue() {
        return toString();
    }

    @Override
    public void close() throws IOException {}

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!GID.class.isAssignableFrom(obj.getClass()))
            return false;
        final GID other = (GID)obj;
        if (this.getOid() != other.getOid())
            return false;
        if (this.getId() != other.getId())
            return false;
        return true;
    }
}
