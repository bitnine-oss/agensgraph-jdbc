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
            else if (0 == depth && ',' == c) {
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
