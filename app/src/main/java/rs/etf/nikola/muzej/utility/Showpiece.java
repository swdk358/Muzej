package rs.etf.nikola.muzej.utility;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import rs.etf.nikola.muzej.R;

public class Showpiece implements Serializable, Parcelable {
    private String name;
    private String beaconUUID;
    private String image;
    private String text;
    private String sound;
    private boolean itemFocused;
    private boolean haveName;

    public Showpiece() {
        image = "android.resource://rs.etf.nikola.muzej/" + R.drawable.defaultimage;
        text = "android.resource://rs.etf.nikola.muzej/" + R.raw.defaulttext;
        sound = "android.resource://rs.etf.nikola.muzej/" + R.raw.defaultsound;
        itemFocused = false;
        haveName = false;
    }

    protected Showpiece(Parcel in) {
        name = in.readString();
        beaconUUID = in.readString();
        image = in.readString();
        text = in.readString();
        sound = in.readString();
        itemFocused = in.readByte() != 0;
        haveName = in.readByte() != 0;
    }

    public static final Creator<Showpiece> CREATOR = new Creator<Showpiece>() {
        @Override
        public Showpiece createFromParcel(Parcel in) {
            return new Showpiece(in);
        }

        @Override
        public Showpiece[] newArray(int size) {
            return new Showpiece[size];
        }
    };

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(beaconUUID);
        dest.writeString(image);
        dest.writeString(text);
        dest.writeString(sound);
        dest.writeByte((byte) (itemFocused ? 1 : 0));
        dest.writeByte((byte) (haveName ? 1 : 0));
    }
}