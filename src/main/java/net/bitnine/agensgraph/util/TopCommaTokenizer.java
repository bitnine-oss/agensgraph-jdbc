package net.bitnine.agensgraph.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopCommaTokenizer {

    private List<String> tokens;
    private int depth;

    public TopCommaTokenizer(String path) throws IllegalArgumentException {
        tokenize(path);
    }

    /*
     * XXX
     * id[:]{},:id[:]{},id[:]{},:id[:]{},id[:]{} 가정
     * multi label 의 경우 id를 label1,label2,label3 으로 하면 망함
     * id는 label1:label2:label3 가 좋을 듯
     */
    private void tokenize(String path) throws IllegalArgumentException {
        this.tokens = new ArrayList<String>();
        this.depth = 0;
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
