package rs.etf.nikola.muzej.utility;

import java.util.LinkedList;

public class Exhibit extends LinkedList<Showpiece> {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
