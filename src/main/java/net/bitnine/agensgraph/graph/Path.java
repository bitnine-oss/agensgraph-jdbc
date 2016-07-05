package net.bitnine.agensgraph.graph;

import org.postgresql.util.PGobject;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;

public class Path extends PGobject implements Serializable, Closeable {

    public Path() {
        setType("path");
    }

    @Override
    public void close() throws IOException {

    }
}
