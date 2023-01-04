/*
 * Copyright (c) 2004, PostgreSQL Global Development Group
 * See the LICENSE file in the project root for more information.
 */

/*
 * Borrowed from PostgreSQL JDBC driver
 */

package net.bitnine.agensgraph.ds;

import net.bitnine.agensgraph.ds.common.BaseDataSource;
import org.postgresql.ds.PGPooledConnection;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;

public class AGConnectionPoolDataSource extends BaseDataSource
        implements ConnectionPoolDataSource, Serializable {
    private boolean defaultAutoCommit = true;

    /**
     * Gets a description of this DataSource.
     */
    public String getDescription() {
        return "ConnectionPoolDataSource from " + org.postgresql.util.DriverInfo.DRIVER_FULL_NAME;
    }

    /**
     * Gets a connection which may be pooled by the app server or middleware implementation of
     * DataSource.
     *
     * @throws java.sql.SQLException Occurs when the physical database connection cannot be
     *         established.
     */
    public PooledConnection getPooledConnection() throws SQLException {
        return new PGPooledConnection(getConnection(), defaultAutoCommit);
    }

    /**
     * Gets a connection which may be pooled by the app server or middleware implementation of
     * DataSource.
     *
     * @throws java.sql.SQLException Occurs when the physical database connection cannot be
     *         established.
     */
    public PooledConnection getPooledConnection(String user, String password) throws SQLException {
        return new PGPooledConnection(getConnection(user, password), defaultAutoCommit);
    }

    /**
     * Gets whether connections supplied by this pool will have autoCommit turned on by default. The
     * default value is {@code true}, so that autoCommit will be turned on by default.
     *
     * @return true if connections supplied by this pool will have autoCommit
     */
    public boolean isDefaultAutoCommit() {
        return defaultAutoCommit;
    }

    /**
     * Sets whether connections supplied by this pool will have autoCommit turned on by default. The
     * default value is {@code true}, so that autoCommit will be turned on by default.
     *
     * @param defaultAutoCommit whether connections supplied by this pool will have autoCommit
     */
    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        this.defaultAutoCommit = defaultAutoCommit;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        writeBaseObject(out);
        out.writeBoolean(defaultAutoCommit);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        readBaseObject(in);
        defaultAutoCommit = in.readBoolean();
    }
}
