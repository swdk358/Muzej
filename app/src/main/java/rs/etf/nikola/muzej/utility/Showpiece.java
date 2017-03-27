package rs.etf.nikola.muzej.utility;

import android.net.Uri;

import org.altbeacon.beacon.Beacon;

public class Showpiece {
    private String name;
    private Beacon beacon;
    private Uri image;
    private Uri text;
    private Uri sound;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public Uri getText() {
        return text;
    }

    public void setText(Uri text) {
        this.text = text;
    }

    public Uri getSound() {
        return sound;
    }

    public void setSound(Uri sound) {
        this.sound = sound;
    }

    @Override
    public String toString() {
        return name;
    }
}
