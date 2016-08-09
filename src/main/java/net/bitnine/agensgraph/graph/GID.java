package net.bitnine.agensgraph.graph;

public class GID {
    /**
     * ID of base object
     */
    public int oid;

    /**
     * ID in the base object
     */
    public int id;

    public GID(int oid, int id) {
        this.oid = oid;
        this.id = id;
    }

    public GID(String oid, String id) {
        this(Integer.parseInt(oid), Integer.parseInt(id));
    }

    public String toString() {
        return "[" + oid + ":" + id + "]";
    }
}
