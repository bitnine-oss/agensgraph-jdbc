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

package net.bitnine.agensgraph.graph;

import net.bitnine.agensgraph.util.Jsonb;
import org.json.simple.JSONObject;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class defines the type vertex.
 */
public class Vertex extends GraphEntity {
    private static final Pattern vertexPattern;

    static {
        vertexPattern = Pattern.compile("(.+?)\\[(.+?)\\](.*)");
    }

    public Vertex() {
        setType("vertex");
    }

    @Override
    public void setValue(String value) throws SQLException {
        Matcher m = vertexPattern.matcher(value);
        if (m.find()) {
            setLabel(m.group(1));

            GraphId gid = new GraphId();
            gid.setValue(m.group(2));
            setGraphId(gid);

            Jsonb props = new Jsonb();
            props.setValue(m.group(3));
            if (!(props.getJsonValue() instanceof JSONObject))
                throw new PSQLException("Parsing vertex failed", PSQLState.DATA_ERROR);
            setProperties(props);
        } else {
            throw new PSQLException("Parsing vertex failed", PSQLState.DATA_ERROR);
        }

        super.setValue(value);
    }

    /**
     * Returns id of the vertex.
     *
     * @return id of the vertex
     */
    public GraphId getVertexId() {
        return getGraphId();
    }
}
