package net.bitnine.agensgraph;

import org.postgresql.core.Parser;
import org.postgresql.util.GT;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NamedParameterStatement {

    private Connection conn;
    private PreparedStatement pstmt = null;
    private List<String> nameList;

    public NamedParameterStatement(Connection conn) {
        this.conn = conn;
        this.nameList = new ArrayList<>();
    }

    public NamedParameterStatement(Connection conn, String cypher) throws SQLException {
        this(conn);
        prepare(cypher);
    }

    private static boolean isIdentifierStartChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private static boolean isIdentifierContChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
                || c == '_' || (c >= '0' && c <= '9');
    }

    private int parseNamedParameter(char[] query, int offset) throws SQLException {
        int start = ++offset;
        if (offset >= query.length || Parser.isSpace(query[offset]))
            throw new PSQLException(
                    GT.tr("Invalid parameter name {0}.", "''"),
                    PSQLState.INVALID_NAME);

        boolean hasInvalidChar = false;
        if (!isIdentifierStartChar(query[offset]))
            hasInvalidChar = true;

        while (++offset < query.length) {
            char c = query[offset];
            if (Parser.isSpace(c))
                break;
            if (!isIdentifierContChar(c)) {
                hasInvalidChar = true;
            }
        }
        StringBuffer name = new StringBuffer();
        name.append(query, start, offset - start);
        if (hasInvalidChar)
            throw new PSQLException(
                    GT.tr("Invalid parameter name {0}.", name.toString()),
                    PSQLState.INVALID_NAME);

        nameList.add(name.toString());

        return offset;
    }

    private String convert(String cypher) throws SQLException {
        char[] aChars = cypher.toCharArray();
        StringBuilder jdbcCypher = new StringBuilder(cypher.length() + 10);

        for (int i = 0; i < aChars.length; ++i) {
            char aChar = aChars[i];
            int start = i;
            boolean skip = false;
            switch (aChar) {
                case '\'': // single-quotes
                    i = Parser.parseSingleQuotes(aChars, i, true);
                    break;

                case '"': // double-quotes
                    i = Parser.parseDoubleQuotes(aChars, i);
                    break;

                case '-': // possibly -- style comment
                    i = Parser.parseLineComment(aChars, i);
                    break;

                case '/': // possibly /* */ style comment
                    i = Parser.parseBlockComment(aChars, i);
                    break;

                case '$': // possibly dollar quote start
                    i = parseNamedParameter(aChars, i);
                    jdbcCypher.append("? ");
                    skip = true;
                    break;

                default:
                    break;
            }
            if (!skip)
                jdbcCypher.append(aChars, start, i - start + 1);
        }

        return jdbcCypher.toString();
    }

    public void prepare(String cypher) throws SQLException {
        nameList.clear();
        String jdbcCypher = convert(cypher);
        pstmt = conn.prepareStatement(jdbcCypher);
    }

    private void bind(Map<String, Object> params) throws SQLException {
        if (params == null)
            throw new PSQLException(
                    GT.tr("Parameters must be provided."),
                    PSQLState.INVALID_PARAMETER_VALUE);

        int i = 1;
        for (String key : nameList) {
            if (!params.containsKey(key)) {
                throw new PSQLException(
                        GT.tr("No value specified for parameter {0}.", key),
                        PSQLState.INVALID_PARAMETER_VALUE);
            }
            Object param = params.get(key);
            pstmt.setObject(i, param);
            i++;
        }
    }

    public int executeUpdate(String cypher, Map<String, Object> params) throws SQLException {
        prepare(cypher);
        bind(params);
        return pstmt.executeUpdate();
    }

    public int executeUpdate(Map<String, Object> params) throws SQLException {
        if (pstmt == null)
            throw new PSQLException(
                    GT.tr("This statement is not prepared"),
                    PSQLState.OBJECT_NOT_IN_STATE);
        bind(params);
        return pstmt.executeUpdate();
    }

    public ResultSet executeQuery(String cypher, Map<String, Object> params) throws SQLException {
        prepare(cypher);
        bind(params);
        return pstmt.executeQuery();
    }

    public ResultSet executeQuery(Map<String, Object> params) throws SQLException {
        if (pstmt == null)
            throw new PSQLException(
                    GT.tr("This statement is not prepared"),
                    PSQLState.OBJECT_NOT_IN_STATE);
        bind(params);
        return pstmt.executeQuery();
    }

    public void clearParameters() throws SQLException {
        if (pstmt != null)
            pstmt.clearParameters();
    }

    public void close() throws SQLException {
        nameList.clear();
        if (pstmt != null) {
            pstmt.close();
            pstmt = null;
        }
    }
}