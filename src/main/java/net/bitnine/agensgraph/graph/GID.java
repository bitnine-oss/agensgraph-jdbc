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

    public String toString() {
        return "[" + oid + ":" + id + "]";
    }
}
