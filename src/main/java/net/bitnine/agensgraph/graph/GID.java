package net.bitnine.agensgraph.graph;

class GID {
    /**
     * ID of base object
     */
    private int oid;

    /**
     * ID in the base object
     */
    private int id;

    GID(int oid, int id) {
        this.oid = oid;
        this.id = id;
    }

    GID(String oid, String id) {
        this(Integer.parseInt(oid), Integer.parseInt(id));
    }

    public String toString() {
        return "[" + oid + "." + id + "]";
    }

    public int getOid() {
        return oid;
    }

    public int getId() {
        return id;
    }
}
