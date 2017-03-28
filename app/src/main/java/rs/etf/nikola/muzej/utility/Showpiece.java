package rs.etf.nikola.muzej.utility;

import android.net.Uri;

import org.altbeacon.beacon.Beacon;

public class Showpiece {
    private String name;
    private String beaconUUID;
    private Uri image;
    private Uri text;
    private Uri sound;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeaconUUID() {
        return beaconUUID;
    }

    public void setBeaconUUID(String uuid) {
        this.beaconUUID = uuid;
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
