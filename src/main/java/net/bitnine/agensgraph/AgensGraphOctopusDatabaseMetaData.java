/*
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

package net.bitnine.agensgraph;

import org.postgresql.jdbc4.Jdbc4Connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


class AgensGraphOctopusDatabaseMetaData extends AbstractDatabaseMetaData {
    private final Connection connection;

    AgensGraphOctopusDatabaseMetaData(Jdbc4Connection conn) {
        this.connection = conn;
    }

    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        Statement stmt = createMetaDataStatement();

        String sql = "SHOW TABLES";
        if (catalog != null)
            sql += " DATASOURCE \"" + catalog + '"';
        if (schemaPattern != null)
            sql += " SCHEMA '" + schemaPattern + "'";
        if (tableNamePattern != null)
            sql += " TABLE '" + tableNamePattern + "'";

        return stmt.executeQuery(sql);
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
        return createMetaDataStatement().executeQuery("SHOW SCHEMAS");
    }

    @Override
    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
        Statement stmt = createMetaDataStatement();

        String sql = "SHOW SCHEMAS";
        if (catalog != null)
            sql += " DATASOURCE \"" + catalog + '"';
        if (schemaPattern != null)
            sql += " SCHEMA '" + schemaPattern + "'";

        return stmt.executeQuery(sql);
    }

    @Override
    public ResultSet getCatalogs() throws SQLException {
        return createMetaDataStatement().executeQuery("SHOW DATASOURCES");
    }

    @Override
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        Statement stmt = createMetaDataStatement();

        String sql = "SHOW COLUMNS";
        if (catalog != null)
            sql += " DATASOURCE \"" + catalog + '"';
        if (schemaPattern != null)
            sql += " SCHEMA '" + schemaPattern + "'";
        if (tableNamePattern != null)
            sql += " TABLE '" + tableNamePattern + "'";
        if (columnNamePattern != null)
            sql += " COLUMN '" + columnNamePattern + "'";

        return stmt.executeQuery(sql);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    private Statement createMetaDataStatement() throws SQLException {
        return getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }
}
