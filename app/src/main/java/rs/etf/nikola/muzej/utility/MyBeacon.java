package rs.etf.nikola.muzej.utility;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.LinkedList;

import static java.lang.Math.pow;


public class MyBeacon implements Comparable<MyBeacon> {
    private final String beaconUUID;
    private double distance;
    private LinkedList<Integer> rssiList;
    private int txPower;
    private long timestamp;

    public MyBeacon(String beaconUUID, int txPower, int rssi) {
        this.beaconUUID = beaconUUID;
        this.timestamp = System.currentTimeMillis();
        rssiList = new LinkedList<>();
        for(int i = 0; i < 2; i++)
            rssiList.add(txPower);
        rssiList.add(rssi);
        this.txPower = txPower;
        double average = 0;
        for(int i:rssiList)
            average += i;
        average /= rssiList.size();
        this.distance = pow(10, (this.txPower - average) / (10 * 3));
    }

    public String getBeaconUUID() {
        return beaconUUID;
    }

    public double getDistance() {
        return distance;
    }

    public boolean isInRangeActivate() {
        return distance < 1;
    }

    public boolean isInRangeDeactivate() {
        return distance < 1.5;
    }

    public LinkedList<Integer> getRssiList() {
        return rssiList;
    }

    public int getTxPower() {
        return txPower;
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
        if(distance > 1)
            return "ID:" + beaconUUID + "\nRazdaljina:Van dometa";
        else
            return "ID:" + beaconUUID + "\nRazdaljina:U dometu";
    }

    @Override
    public int compareTo(@NonNull MyBeacon o) {
        return this.getBeaconUUID().compareTo(o.getBeaconUUID());
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == MyBeacon.class && this.compareTo((MyBeacon) obj) == 0;
    }

    public void addRssi(int txPower, LinkedList<Integer> rssiList) {
        if(this.txPower == txPower) {
            this.rssiList.remove();
            this.rssiList.add(rssiList.getLast());
            double average = 0;
            for(int i:this.rssiList)
                average += i;
            average /= this.rssiList.size();
            this.distance = pow(10, (this.txPower - average) / (10 * 3));
        } else {
            this.txPower = txPower;
            this.rssiList = rssiList;
            double average = 0;
            for(int i:this.rssiList)
                average += i;
            average /= this.rssiList.size();
            this.distance = pow(10, (this.txPower - average) / (10 * 3));
        }
    }
}
