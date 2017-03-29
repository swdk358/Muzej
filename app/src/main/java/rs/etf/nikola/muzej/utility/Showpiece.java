package rs.etf.nikola.muzej.utility;

import android.net.Uri;

import org.altbeacon.beacon.Beacon;

public class Showpiece {
    private String name;
    private String beaconUUID;
    private Uri image;
    private Uri text;
    private Uri sound;
    private boolean itemFocused;
    private boolean haveName;

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

    public boolean isItemFocused() {
        return itemFocused;
    }

    public void setItemFocused(boolean itemFocused) {
        this.itemFocused = itemFocused;
    }

    public boolean isHaveName() {
        return haveName;
    }

    public void setHaveName(boolean haveName) {
        this.haveName = haveName;
    }

    @Override
    public String toString() {
        return name;
    }
}
