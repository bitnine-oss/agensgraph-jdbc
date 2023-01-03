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

import net.bitnine.agensgraph.test.AbstractAGDockerizedTest;
import net.bitnine.agensgraph.util.AgTokenizer;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AgTokenizerTest extends AbstractAGDockerizedTest {

    @Test
    public void testParseString() throws SQLException {
        String s1 = "vertex[3.1]{\"vid\": 1}";
        String s2 = "edge[4.1][3.1,3.2]{\"rid\": 1}";
        String s3 = "vertex[3.2]{\"vid\": 2}";
        String string = "[" + s1 + "," + s2 + "," + s3 + "]";
        ArrayList<String> t = AgTokenizer.tokenize(string);

        assertEquals(s1, t.get(0));
        assertEquals(s2, t.get(1));
        assertEquals(s3, t.get(2));
    }

    @Test
    public void testNull() throws SQLException {
        String string = "[NULL,,NULL]";
        ArrayList<String> t = AgTokenizer.tokenize(string);

        assertNull(t.get(0));
        assertNull(t.get(1));
        assertNull(t.get(2));
    }

    @Test
    public void testEmpty() throws SQLException {
        String string = "[]";
        ArrayList<String> t = AgTokenizer.tokenize(string);

        assertEquals(1, t.size());
    }

}
