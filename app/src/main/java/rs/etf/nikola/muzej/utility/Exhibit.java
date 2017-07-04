package rs.etf.nikola.muzej.utility;

import android.support.annotation.NonNull;

import java.util.Collection;
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

    public Showpiece getShowpieceByUUID(String uuid) {
        for(Showpiece showpiece:this) {
            if(showpiece.getBeaconUUID().equals(uuid))
                return showpiece;
        }
        return null;
    }

    public Exhibit(@NonNull Collection<? extends Showpiece> c, String name) {
        super(c);
        this.name = name;
    }

    public Exhibit() {

    }
}
