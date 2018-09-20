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

package net.bitnine.agensgraph.jdbc;

import net.bitnine.agensgraph.graph.Edge;
import net.bitnine.agensgraph.graph.GraphId;
import net.bitnine.agensgraph.graph.Path;
import net.bitnine.agensgraph.graph.Vertex;
import net.bitnine.agensgraph.util.Jsonb;
import org.postgresql.core.Parser;
import org.postgresql.jdbc.PgConnection;
import org.postgresql.util.GT;
import org.postgresql.util.HostSpec;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class defines connection to AgensGraph.
 */
public class AgConnection extends PgConnection {
    public AgConnection(HostSpec[] hostSpecs, String user, String database, Properties info, String url) throws SQLException {
        super(hostSpecs, user, database, info, url);

        addDataType("jsonb", Jsonb.class);
        addDataType("graphid", GraphId.class);
        addDataType("vertex", Vertex.class);
        addDataType("edge", Edge.class);
        addDataType("graphpath", Path.class);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        Statement stmt = super.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        return new AgStatement(stmt);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        PreparedStatement pstmt = super.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        return new AgPreparedStatement(pstmt);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        if (columnNames != null && columnNames.length == 0)
            return prepareStatement(sql);

        PreparedStatement pstmt = super.prepareStatement(sql, columnNames);
        return new AgPreparedStatement(pstmt);
    }

    /**
     * Returns a prepared statement with a named parameter.
     *
     * @param queryString the query string
     * @return a prepared statement with a named parameter
     * @throws SQLException the SQL exception
     */
    public AgPreparedStatement prepareNamedParameterStatement(String queryString) throws SQLException {
        return prepareNamedParameterStatement(queryString, java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
    }

    /**
     * Returns a prepared statement with a named parameter.
     *
     * @param queryString          the query string
     * @param resultSetType        the result set type
     * @param resultSetConcurrency the result set concurrency
     * @return a prepared statement with a named parameter.
     * @throws SQLException the SQL exception
     */
    public AgPreparedStatement prepareNamedParameterStatement(String queryString, int resultSetType, int resultSetConcurrency) throws SQLException {
        return prepareNamedParameterStatement(queryString, resultSetType, resultSetConcurrency, getHoldability());
    }

    /**
     * Returns a prepared statement with a named parameter.
     *
     * @param queryString          the query string
     * @param resultSetType        the result set type
     * @param resultSetConcurrency the result set concurrency
     * @param resultSetHoldability the result set holdability
     * @return a prepared statement with a named parameter
     * @throws SQLException the SQL exception
     */
    public AgPreparedStatement prepareNamedParameterStatement(String queryString, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        checkClosed();

        ArrayList<String> params = new ArrayList<>();
        queryString = preprocessQueryString(queryString, params);

        PreparedStatement pstmt = super.prepareStatement(queryString, resultSetType, resultSetConcurrency, resultSetHoldability);
        return new AgPreparedStatement(pstmt, params);
    }

    /**
     * Returns a prepared statement with a named parameter.
     *
     * @param queryString       the query string
     * @param autoGeneratedKeys the auto generated keys
     * @return a prepared statement with a named parameter
     * @throws SQLException the SQL exception
     */
    public AgPreparedStatement prepareNamedParameterStatement(String queryString, int autoGeneratedKeys) throws SQLException {
        if (autoGeneratedKeys != Statement.RETURN_GENERATED_KEYS)
            return prepareNamedParameterStatement(queryString);

        return prepareNamedParameterStatement(queryString, (String[]) null);
    }

    /**
     * Returns a prepared statement with a named parameter.
     *
     * @param queryString   the query string
     * @param columnIndexes the column indexes
     * @return a prepared statement with a named parameter
     * @throws SQLException the SQL exception
     */
    public AgPreparedStatement prepareNamedParameterStatement(String queryString, int[] columnIndexes) throws SQLException {
        if (columnIndexes != null && columnIndexes.length == 0)
            return prepareNamedParameterStatement(queryString);

        checkClosed();
        throw new PSQLException(GT.tr("Returning autogenerated keys is not supported."), PSQLState.NOT_IMPLEMENTED);
    }

    /**
     * Returns a prepared statement with a named parameter.
     *
     * @param queryString the query string
     * @param columnNames the column names
     * @return a prepared statement with a named parameter
     * @throws SQLException the SQL exception
     */
    public AgPreparedStatement prepareNamedParameterStatement(String queryString, String[] columnNames) throws SQLException {
        if (columnNames != null && columnNames.length == 0)
            return prepareNamedParameterStatement(queryString);

        checkClosed();

        ArrayList<String> params = new ArrayList<>();
        queryString = preprocessQueryString(queryString, params);

        PreparedStatement pstmt = super.prepareStatement(queryString, columnNames);
        return new AgPreparedStatement(pstmt, params);
    }

    private String preprocessQueryString(String queryString, ArrayList<String> params) throws SQLException {
        StringBuilder bld = new StringBuilder(queryString.length());

        char[] buf = queryString.toCharArray();
        for (int pos = 0; pos < buf.length; pos++) {
            char c = buf[pos];
            int start = pos;

            switch (c) {
                case '\'':
                    pos = Parser.parseSingleQuotes(buf, start, true);
                    break;
                case '"':
                    pos = Parser.parseDoubleQuotes(buf, start);
                    break;
                case '-': // possibly -- style comment
                    pos = Parser.parseLineComment(buf, start);
                    break;
                case '/': // possibly /* */ style comment
                    pos = Parser.parseBlockComment(buf, start);
                    break;
                case '$': // possibly dollar quote start
                    pos = Parser.parseDollarQuotes(buf, start);
                    // found a complete dollar-quoted string or hit unmatched right dollar quote
                    if (pos > start)
                        break;
                    pos = parseAndStoreNamedParameter(buf, start, params);
                    if (pos > start) {
                        bld.append('?');
                        continue;
                    }
                    break;
                default:
                    break;
            }

            if (pos == start)
                bld.append(c);
            else
                bld.append(buf, start, pos - start + 1);
        }

        return bld.toString();
    }

    private int parseAndStoreNamedParameter(final char[] query, int offset, ArrayList<String> params) throws SQLException {
        // '$' is the last character in query or offset is out of range
        if (offset + 1 >= query.length)
            return offset;

        // '$' is in an identifier
        if (offset > 0 && Parser.isIdentifierContChar(query[offset - 1]))
            return offset;

        int pos = offset + 1;

        // just '$' (not a named parameter)
        if (!Parser.isDollarQuoteStartChar(query[pos]))
            return offset;

        pos++;

        while (pos < query.length) {
            if (!Parser.isDollarQuoteContChar(query[pos]))
                break;

            pos++;
        }

        String paramName = new String(query, offset + 1, pos - (offset + 1));
        params.add(paramName);

        return pos - 1;
    }
}
