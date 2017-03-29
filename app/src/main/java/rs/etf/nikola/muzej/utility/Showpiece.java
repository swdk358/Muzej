package rs.etf.nikola.muzej.utility;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import rs.etf.nikola.muzej.R;

public class Showpiece implements Parcelable {
    private String name;
    private String beaconUUID;
    private Uri image;
    private Uri text;
    private Uri sound;
    private boolean itemFocused;
    private boolean haveName;

    public Showpiece() {
        image = Uri.parse("android.resource://rs.etf.nikola.muzej/" + R.raw.defaultimage);
        text = Uri.parse("android.resource://rs.etf.nikola.muzej/" + R.raw.defaulttext);
        sound = Uri.parse("android.resource://rs.etf.nikola.muzej/" + R.raw.defaultsound);
        itemFocused = false;
        haveName = false;
    }

    protected Showpiece(Parcel in) {
        name = in.readString();
        beaconUUID = in.readString();
        image = in.readParcelable(Uri.class.getClassLoader());
        text = in.readParcelable(Uri.class.getClassLoader());
        sound = in.readParcelable(Uri.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(beaconUUID);
        dest.writeParcelable(image, flags);
        dest.writeParcelable(text, flags);
        dest.writeParcelable(sound, flags);
        dest.writeByte((byte) (itemFocused ? 1 : 0));
        dest.writeByte((byte) (haveName ? 1 : 0));
    }
}
