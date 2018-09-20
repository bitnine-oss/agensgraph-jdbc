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
 * This class defines the type edge.
 */
public class Edge extends GraphEntity {
    private static final Pattern edgePattern;

    private GraphId start;
    private GraphId end;

    static {
        edgePattern = Pattern.compile("(.+?)\\[(.+?)\\]\\[(.+?),(.+?)\\](.*)");
    }

    public Edge() {
        setType("edge");
    }

    @Override
    public void setValue(String value) throws SQLException {
        Matcher m = edgePattern.matcher(value);
        if (m.find()) {
            setLabel(m.group(1));

            GraphId gid = new GraphId();
            gid.setValue(m.group(2));
            setGraphId(gid);

            gid = new GraphId();
            gid.setValue(m.group(3));
            start = gid;

            gid = new GraphId();
            gid.setValue(m.group(4));
            end = gid;

            Jsonb props = new Jsonb();
            props.setValue(m.group(5));
            if (!(props.getJsonValue() instanceof JSONObject))
                throw new PSQLException("Parsing edge failed", PSQLState.DATA_ERROR);
            setProperties(props);
        } else {
            throw new PSQLException("Parsing edge failed", PSQLState.DATA_ERROR);
        }

        super.setValue(value);
    }

    /**
     * Returns id of the edge.
     *
     * @return id of the edge
     */
    public GraphId getEdgeId() {
        return getGraphId();
    }

    /**
     * Returns id of the start vertex.
     *
     * @return id of the start vertex
     */
    public GraphId getStartVertexId() {
        return start;
    }

    /**
     * Returns id of the end vertex.
     *
     * @return id of the end vertex
     */
    public GraphId getEndVertexId() {
        return end;
    }
}
