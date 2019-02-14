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
import net.bitnine.agensgraph.util.AgTokenizer;
import org.junit.Test;

import java.sql.SQLException;

public class AgTokenizerTest extends TestCase {

    @Test
    public void testParseString() throws SQLException {
        String s1 = "vertex[3.1]{\"vid\": 1}";
        String s2 = "edge[4.1][3.1,3.2]{\"rid\": 1}";
        String s3 = "vertex[3.2]{\"vid\": 2}";
        String string = "[" + s1 + "," + s2 + "," + s3 + "]";
        AgTokenizer t = new AgTokenizer(string);

        assertEquals(s1, t.getToken(0));
        assertEquals(s2, t.getToken(1));
        assertEquals(s3, t.getToken(2));
    }

    @Test
    public void testNull() throws SQLException {
        String string = "[NULL,NULL]";
        AgTokenizer t = new AgTokenizer(string);

        assertNull(t.getToken(0));
        assertNull(t.getToken(1));
    }

    @Test
    public void testEmpty() throws SQLException {
        String string = "[]";
        AgTokenizer t = new AgTokenizer(string);

        assertEquals(0, t.getSize());
    }

}
