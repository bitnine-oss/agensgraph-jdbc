/*
 * Copyright (c) 2014-2016, Bitnine Inc.
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

import java.util.ArrayList;
import java.util.List;

public class TopCommaTokenizer {

    private List<String> tokens;

    public TopCommaTokenizer(String path) throws IllegalArgumentException {
        tokenize(path);
    }

    private void tokenize(String path) throws IllegalArgumentException {
        this.tokens = new ArrayList<>();
        int depth = 0;
        int i;
        int s;
        boolean inGID = false;

        if (null == path || 0 == path.length()) {
            return;
        }

        for (i = 0, s = 0; i < path.length(); ++i) {
            char c = path.charAt(i);

            if ('{' == c) {
                depth++;
            }
            else if ('}' == c) {
                depth--;
            }
            else if (0 == depth && '[' == c) { // for GID
                inGID = true;
            }
            else if (inGID && ']' == c) {
                inGID = false;
            }
            else if (0 == depth && !inGID && ',' == c) {
                tokens.add(path.substring(s, i));
                s = i + 1;
            }

            if (depth < 0) {
                throw new IllegalArgumentException();
            }
        }
        /* add a last token */
        tokens.add(path.substring(s, i));
    }

    public String getToken(int n) {
        return tokens.get(n);
    }

    public int getSize() {
        return tokens.size();
    }
}
