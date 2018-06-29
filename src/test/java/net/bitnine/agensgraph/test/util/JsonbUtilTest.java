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

package net.bitnine.agensgraph.test.util;

import junit.framework.TestCase;
import net.bitnine.agensgraph.util.Jsonb;
import net.bitnine.agensgraph.util.JsonbUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonbUtilTest extends TestCase {
    @Test
    public void testArrayBuilder() {
        Jsonb j = JsonbUtil.createArrayBuilder()
                .add("")
                .add(0)
                .add(0.0)
                .add(false)
                .add(true)
                .addNull()
                .add(JsonbUtil.createArrayBuilder()
                        .add(0))
                .add(JsonbUtil.createObjectBuilder()
                        .add("i", 0))
                .build();
        assertEquals("[\"\",0,0.0,false,true,null,[0],{\"i\":0}]", j.toString());
    }

    @Test
    public void testObjectBuilder() {
        Jsonb j = JsonbUtil.createObjectBuilder()
                .add("s", "")
                .add("l", 0)
                .add("d", 0.0)
                .add("f", false)
                .add("t", true)
                .addNull("z")
                .add("a", JsonbUtil.createArrayBuilder()
                        .add(0))
                .add("o", JsonbUtil.createObjectBuilder()
                        .add("i", 0))
                .build();
        assertNotNull(j.toString());

        assertTrue(j.containsKey("z"));
        assertTrue(j.isNull("z"));
        assertFalse(j.containsKey("x"));
        assertTrue(j.isNull("x"));
    }

    @Test
    public void testCreateScalar() {
        Jsonb j = JsonbUtil.create("s");
        assertEquals("s", j.getString());

        j = JsonbUtil.create(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, j.getInt());

        j = JsonbUtil.create(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, j.getLong());

        j = JsonbUtil.create(Math.PI);
        assertEquals(Math.PI, j.getDouble());

        j = JsonbUtil.create(false);
        assertFalse(j.getBoolean());

        j = JsonbUtil.create(true);
        assertTrue(j.getBoolean());

        j = JsonbUtil.createNull();
        assertTrue(j.isNull());
    }

    @Test
    public void testCreateArray() {
        Jsonb j = JsonbUtil.createArray();
        assertEquals(0, j.size());
        assertEquals("[]", j.toString());

        j = JsonbUtil.createArray("0", "1", "2");
        assertEquals(3, j.size());
        assertEquals("[\"0\",\"1\",\"2\"]", j.toString());

        j = JsonbUtil.createArray(0, 1, 2);
        assertEquals(3, j.size());
        assertEquals("[0,1,2]", j.toString());

        j = JsonbUtil.createArray(0L, 1L, 2L);
        assertEquals(3, j.size());
        assertEquals("[0,1,2]", j.toString());

        j = JsonbUtil.createArray(0.0, 1.0, 2.0);
        assertEquals("[0.0,1.0,2.0]", j.toString());

        j = JsonbUtil.createArray(false, true);
        assertEquals(2, j.size());
        assertEquals("[false,true]", j.toString());

        j = JsonbUtil.createArray(
                JsonbUtil.create(""),
                JsonbUtil.create(0),
                JsonbUtil.create(0.0),
                JsonbUtil.create(false),
                JsonbUtil.createNull(),
                JsonbUtil.createArray(),
                JsonbUtil.createObject(),
                new JSONArray(),
                new JSONObject());
        assertEquals(9, j.size());
        assertEquals("[\"\",0,0.0,false,null,[],{},[],{}]", j.toString());

        List<Object> a = new ArrayList<>();
        a.add("");
        a.add(0);
        a.add(0.0);
        a.add(false);
        a.add(null);
        a.add(JsonbUtil.createArray());
        a.add(JsonbUtil.createObject());
        a.add(new JSONArray());
        a.add(new JSONObject());
        j = JsonbUtil.createArray(a);
        assertEquals(9, j.size());
        assertEquals("[\"\",0,0.0,false,null,[],{},[],{}]", j.toString());
    }

    @Test
    public void testCreateObject() {
        Jsonb j = JsonbUtil.createObject();
        assertEquals(0, j.size());
        assertEquals("{}", j.toString());

        Map<String, Object> m = new HashMap<>();
        m.put("i", 0);
        m.put("a", new JSONArray());
        m.put("o", new JSONObject());
        j = JsonbUtil.createObject(m);
        assertEquals(3, j.size());
        assertEquals("{\"a\":[],\"i\":0,\"o\":{}}", j.toString());
    }

    @Test
    public void testConvertStringToNumber() {
        Jsonb j = JsonbUtil.create(Integer.toString(Integer.MAX_VALUE) + " ");
        assertEquals(Integer.MAX_VALUE, j.getInt());

        j = JsonbUtil.create(Long.toString(Long.MAX_VALUE) + " ");
        assertEquals(Long.MAX_VALUE, j.getLong());

        j = JsonbUtil.create(Double.toString(Double.MAX_VALUE) + " ");
        assertEquals(Double.MAX_VALUE, j.getDouble());
    }
}
