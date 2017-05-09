package rs.etf.nikola.muzej.utility;

import android.support.annotation.NonNull;


public class MyBeacon implements Comparable<MyBeacon> {
    private final String beaconUUID;
    private boolean distance;
    private long timestamp;
//        private List<Item> items;

    public MyBeacon(String beaconUUID, boolean distance) {
        this.beaconUUID = beaconUUID;
        this.distance = distance;
        this.timestamp = System.currentTimeMillis();
//            this.items = new LinkedList<>();
    }

    public String getBeaconUUID() {
        return beaconUUID;
    }

    public boolean getDistance() {
        return distance;
    }

    public void setDistance(boolean distance) {
        this.distance = distance;
    }

    //        public List<Item> getItems() {
//            return items;
//        }

    public boolean isTimeExceeded() {

        // More then 3 seconds
        return (System.currentTimeMillis() - this.timestamp) > 3000;
    }

    public void refresh() {
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        if(!distance)
            return "id:" + beaconUUID + ", distance > 1m";
        else
            return "id:" + beaconUUID + ", distance < 1m";
    }

    @Override
    public int compareTo(@NonNull MyBeacon o) {
        return this.getBeaconUUID().compareTo(o.getBeaconUUID());
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == MyBeacon.class && this.compareTo((MyBeacon) obj) == 0;
    }
}
