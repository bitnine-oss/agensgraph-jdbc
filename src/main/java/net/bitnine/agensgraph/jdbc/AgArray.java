/*
 * Copyright (c) 2014-2018, Bitnine Inc.
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

package net.bitnine.agensgraph.jdbc;

import net.bitnine.agensgraph.core.Oid;
import net.bitnine.agensgraph.util.AgTokenizer;
import org.postgresql.core.BaseConnection;
import org.postgresql.core.BaseStatement;
import org.postgresql.core.Field;
import org.postgresql.core.Tuple;
import org.postgresql.jdbc.PgArray;
import org.postgresql.util.GT;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Array is used collect one column of query result data.
 */
public class AgArray extends PgArray {

    /**
     * The OID of this field.
     */
    private final int oid;

    /**
     * Value of field. Will be initialized only once within
     * {@link #buildArrayList()}.
     */
    private ArrayList<String> arrayList;

    /**
     * Create a new Array.
     *
     * @param connection  a database connection
     * @param oid         the oid of the array datatype
     * @param fieldString the array data in string form
     * @throws SQLException if something wrong happens
     */
    public AgArray(BaseConnection connection, int oid, String fieldString) throws SQLException {
        super(connection, oid, fieldString);
        this.oid = oid;
    }

    /**
     * Create a new Array.
     *
     * @param connection a database connection
     * @param oid        the oid of the array datatype
     * @param fieldBytes the array data in byte form
     * @throws SQLException if something wrong happens
     */
    public AgArray(BaseConnection connection, int oid, byte[] fieldBytes) throws SQLException {
        super(connection, oid, fieldBytes);
        this.oid = oid;
    }

    @Override
    public Object getArrayImpl(long index, int count, Map<String, Class<?>> map) throws SQLException {
        // for now maps aren't supported.
        if (map != null && !map.isEmpty()) {
            throw org.postgresql.Driver.notImplemented(this.getClass(), "getArrayImpl(long,int,Map)");
        }

        // array index is out of range
        if (index < 1) {
            throw new PSQLException(GT.tr("The array index is out of range: {0}", index),
                    PSQLState.DATA_ERROR);
        }

        if (fieldBytes != null) {
            throw new PSQLException(GT.tr("The type Binary is not supported."), PSQLState.DATA_ERROR);
        }

        if (fieldString == null) {
            return null;
        }

        buildArrayList();

        if (count == 0) {
            count = arrayList.size();
        }

        // array index out of range
        if ((--index) + count > arrayList.size()) {
            throw new PSQLException(
                    GT.tr("The array index is out of range: {0}, number of elements: {1}.",
                            index + count, (long) arrayList.size()),
                    PSQLState.DATA_ERROR);
        }

        return buildArray(arrayList, (int) index, count);
    }

    /**
     * Build {@link ArrayList} from field's string input. As a result of this method
     * {@link #arrayList} is build. Method can be called many times in order to make sure that array
     * list is ready to use, however {@link #arrayList} will be set only once during first call.
     */
    private void buildArrayList() throws SQLException {
        if (arrayList != null) {
            return;
        }

        arrayList = AgTokenizer.tokenize(fieldString);
    }

    /**
     * Convert {@link ArrayList} to array.
     *
     * @param input list to be converted into array
     */
    private Object buildArray(ArrayList<String> input, int index, int count) throws SQLException {
        if (count < 0) {
            count = input.size();
        }

        // array elements counter
        int length = 0;

        // array elements name
        String typeName = getBaseTypeName();

        // array to be returned
        Object ret;
        Object[] oa;
        ret = oa = (Object[]) java.lang.reflect.Array
                .newInstance(connection.getTypeInfo().getPGobject(typeName), count);

        // add elements
        for (; count > 0; count--) {
            String v = input.get(index++);
            if (v == null) {
                oa[length++] = null;
            } else {
                oa[length++] = connection.getObject(typeName, v, null);
            }
        }

        return ret;
    }

    @Override
    public String getBaseTypeName() throws SQLException {
        buildArrayList();
        int elementOID = connection.getTypeInfo().getPGArrayElement(oid);
        return connection.getTypeInfo().getPGType(elementOID);
    }

    @Override
    public ResultSet getResultSetImpl(long index, int count, Map<String, Class<?>> map) throws SQLException {
        // for now maps aren't supported
        if (map != null && !map.isEmpty()) {
            throw org.postgresql.Driver.notImplemented(this.getClass(), "getResultSetImpl(long,int,Map)");
        }

        // array index is out of range
        if (index < 1) {
            throw new PSQLException(GT.tr("The array index is out of range: {0}", index),
                    PSQLState.DATA_ERROR);
        }

        if (fieldBytes != null) {
            throw new PSQLException(GT.tr("The type Binary is not supported."), PSQLState.DATA_ERROR);
        }

        buildArrayList();

        if (count == 0) {
            count = arrayList.size();
        }

        // array index out of range
        if ((--index) + count > arrayList.size()) {
            throw new PSQLException(
                    GT.tr("The array index is out of range: {0}, number of elements: {1},",
                            index + count, (long) arrayList.size()),
                    PSQLState.DATA_ERROR);
        }

        List<Tuple> tuples = new ArrayList<>();
        Field[] fields = new Field[2];

        final int baseOid = connection.getTypeInfo().getPGArrayElement(oid);
        fields[0] = new Field("INDEX", Oid.INT4);
        fields[1] = new Field("VALUE", baseOid);

        for (int i = 0; i < count; i++) {
            int offset = (int) index + i;
            byte[][] t = new byte[2][0];
            String v = arrayList.get(offset);
            t[0] = connection.encodeString(Integer.toString(offset + 1));
            t[1] = v == null ? null : connection.encodeString(v);
            tuples.add(new Tuple(t));
        }

        BaseStatement stat = (BaseStatement) connection
                .createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stat.createDriverResultSet(fields, tuples);
    }

    @Override
    public void free() throws SQLException {
        super.free();
        arrayList = null;
    }
}
