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

import net.bitnine.agensgraph.Driver;
import org.postgresql.core.BaseStatement;
import org.postgresql.core.CachedQuery;
import org.postgresql.core.Field;
import org.postgresql.core.Query;
import org.postgresql.core.ResultCursor;
import org.postgresql.jdbc.PgStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.List;

/**
 * This class defines a statement to AgensGraph.
 */
public class AgStatement implements BaseStatement {
    private final PgStatement stmt;

    AgStatement(Statement stmt) throws SQLException {
        this.stmt = stmt.unwrap(PgStatement.class);
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return new AgResultSet(stmt.executeQuery(sql));
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return stmt.executeUpdate(sql);
    }

    @Override
    public void close() throws SQLException {
        stmt.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return stmt.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        stmt.setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return stmt.getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        stmt.setMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        stmt.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return stmt.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        stmt.setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        stmt.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return stmt.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        stmt.clearWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        stmt.setCursorName(name);
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        return stmt.execute(sql);
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return new AgResultSet(stmt.getResultSet());
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return stmt.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return stmt.getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        stmt.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return stmt.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        stmt.setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return stmt.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return stmt.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return stmt.getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        stmt.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        stmt.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return stmt.executeBatch();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return stmt.getConnection();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return stmt.getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return new AgResultSet(stmt.getGeneratedKeys());
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return stmt.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return stmt.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return stmt.executeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return stmt.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return stmt.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return stmt.execute(sql, columnNames);
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return stmt.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return stmt.isClosed();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        stmt.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return stmt.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        stmt.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return stmt.isCloseOnCompletion();
    }

    public long getLargeUpdateCount() throws SQLException {
        throw Driver.notImplemented(stmt.getClass(), "getLargeUpdateCount");
    }

    public void setLargeMaxRows(long max) throws SQLException {
        throw Driver.notImplemented(stmt.getClass(), "setLargeMaxRows");
    }

    public long getLargeMaxRows() throws SQLException {
        throw Driver.notImplemented(stmt.getClass(), "getLargeMaxRows");
    }

    public long[] executeLargeBatch() throws SQLException {
        throw Driver.notImplemented(stmt.getClass(), "executeLargeBatch");
    }

    public long executeLargeUpdate(String sql) throws SQLException {
        throw Driver.notImplemented(stmt.getClass(), "executeLargeUpdate");
    }

    public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        throw Driver.notImplemented(stmt.getClass(), "executeLargeUpdate");
    }

    public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
        throw Driver.notImplemented(stmt.getClass(), "executeLargeUpdate");
    }

    public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException {
        throw Driver.notImplemented(stmt.getClass(), "executeLargeUpdate");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        Statement stmt;
        if (iface.equals(AgStatement.class))
            stmt = this;
        else
            stmt = this.stmt;

        if (iface.isAssignableFrom(stmt.getClass())) {
            return iface.cast(stmt);
        } else {
            throw new SQLException("Cannot unwrap to " + iface.getName());
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        Statement stmt;
        if (iface.equals(AgStatement.class))
            stmt = this;
        else
            stmt = this.stmt;

        return iface.isAssignableFrom(stmt.getClass());
    }

    @Override
    public ResultSet createDriverResultSet(Field[] fields, List<byte[][]> tuples) throws SQLException {
        return stmt.createDriverResultSet(fields, tuples);
    }

    @Override
    public ResultSet createResultSet(Query originalQuery, Field[] fields, List<byte[][]> tuples, ResultCursor cursor) throws SQLException {
        return stmt.createResultSet(originalQuery, fields, tuples, cursor);
    }

    @Override
    public boolean executeWithFlags(String p_sql, int flags) throws SQLException {
        return stmt.executeWithFlags(p_sql, flags);
    }

    @Override
    public boolean executeWithFlags(CachedQuery p_sql, int flags) throws SQLException {
        return stmt.executeWithFlags(p_sql, flags);
    }

    @Override
    public boolean executeWithFlags(int flags) throws SQLException {
        return stmt.executeWithFlags(flags);
    }

    @Override
    public long getLastOID() throws SQLException {
        return stmt.getLastOID();
    }

    @Override
    public void setUseServerPrepare(boolean flag) throws SQLException {
        stmt.setUseServerPrepare(flag);
    }

    @Override
    public boolean isUseServerPrepare() {
        return stmt.isUseServerPrepare();
    }

    @Override
    public void setPrepareThreshold(int threshold) throws SQLException {
        stmt.setPrepareThreshold(threshold);
    }

    @Override
    public int getPrepareThreshold() {
        return stmt.getPrepareThreshold();
    }
}
