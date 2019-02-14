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

package net.bitnine.agensgraph.util;

import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class is used to tokenize the text output.
 */
public class AgTokenizer {
    private AgTokenizer() {
    }

    /**
     * This create tokens from a new string.
     *
     * @param string containing tokens
     * @return The tokens value
     * @throws SQLException if something wrong happens
     */
    public static ArrayList<String> tokenize(String string) throws SQLException {
        ArrayList<String> tokens = new ArrayList<>();

        // ignore wrapping '[' and ']' characters
        int pos = 1;
        int len = string.length() -1;

        int start = pos;
        int depth = 0;
        // id of vertex or edge
        boolean veid = false;
        String buffer = null;

        while (pos < len) {
            char c = string.charAt(pos);

            switch (c) {
                case '"':
                    if (depth > 0) {
                        // Parse "string".
                        // Leave pos unchanged if unmatched right " were found.
                        boolean escape = false;
                        for (int i = pos + 1; i < len; i++) {
                            c = string.charAt(i);
                            if (c == '\\') {
                                escape = !escape;
                            } else if (c == '"') {
                                if (escape)
                                    escape = false;
                                else {
                                    pos = i;
                                    break;
                                }
                            } else {
                                escape = false;
                            }
                        }
                    }
                    break;
                case '[':
                    if (depth == 0)
                        veid = true;
                    break;
                case ']':
                    if (depth == 0)
                        veid = false;
                    break;
                case '{':
                    depth++;
                    break;
                case '}':
                    depth--;
                    if (depth < 0) {
                        throw new PSQLException("Parsing graphpath failed", PSQLState.DATA_ERROR);
                    }
                    break;
                case ',':
                    if (depth == 0 && !veid) {
                        buffer = string.substring(start, pos);
                        // Add null for "" and "NULL" because AgTokenizer can be used to parse array elements
                        // and in this case, array index is important.
                        if (buffer.isEmpty() || "NULL".equals(buffer)) {
                            tokens.add(null);
                        } else {
                            tokens.add(buffer);
                        }
                        start = pos + 1;
                    }
                    break;
                default:
                    break;
            }

            pos++;
        }

        /* add the last token */
        buffer = string.substring(start, pos);
        if (buffer.isEmpty() || "NULL".equals(buffer)) {
            tokens.add(null);
        } else {
            tokens.add(buffer);
        }

        return tokens;
    }
}
