package rs.etf.nikola.muzej;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import rs.etf.nikola.muzej.utility.BeaconAdapter;
import rs.etf.nikola.muzej.utility.MyAdapter;
import rs.etf.nikola.muzej.utility.Showpiece;

public class CreateShowpieceActivity extends AppCompatActivity implements BeaconConsumer, RangeNotifier {
    Showpiece showpiece = new Showpiece();
    private BeaconManager beaconManager;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    public static final int BLUETOOTH_ENABLE_REQUEST_ID = 6;

//    static double sumRSSI = 0;
//    static double avRSSI = 0;
//    static int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_showpiece);

        setTitle(R.string.activity_create_showpiece_title);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, BLUETOOTH_ENABLE_REQUEST_ID);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.beaconList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        BeaconAdapter adapter = new BeaconAdapter<>(list, showpiece);
        recyclerView.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
        }

        beaconManager = BeaconManager.getInstanceForApplication(this);
//        beaconManager.getBeaconParsers().add(new BeaconParser().
//                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
//        beaconManager.getBeaconParsers().add(new BeaconParser().
//                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.setForegroundScanPeriod(300l);
        beaconManager.setForegroundBetweenScanPeriod(200l);

        beaconManager.setBackgroundScanPeriod(300l);
        beaconManager.setBackgroundBetweenScanPeriod(200l);

        beaconManager.bind(this);

        EditText editText = (EditText) findViewById(R.id.inputNaziv);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String name = v.getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(!name.isEmpty()) {
                    showpiece.setName(v.getText().toString());
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    showpiece.setHaveName(true);
                    return true;
                }
                else {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    showpiece.setHaveName(false);
                    return false;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_ENABLE_REQUEST_ID) {
            if (resultCode == RESULT_OK) {
                // Request granted - bluetooth is turning on...
            }
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
            }
        }
    }

    public void onBeaconServiceConnect() {

//        beaconManager.addMonitorNotifier(new MonitorNotifier() {
//            @Override
//            public void didEnterRegion(Region region) {
//                Log.i(TAG, "I just saw an beacon for the first time!");
//            }
//
//            @Override
//            public void didExitRegion(Region region) {
//                Log.i(TAG, "I no longer see an beacon");
//            }
//
//            @Override
//            public void didDetermineStateForRegion(int state, Region region) {
//                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
//            }
//        });

//        beaconManager.addRangeNotifier(new RangeNotifier() {
//            @Override
//            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
//                if (beacons.size() > 0) {
//                    Log.i(TAG, "The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");
//                }
//            }
//        });

        Region region = new Region("all-beacons-region", null, null, null);
        try {
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        beaconManager.addRangeNotifier(this);

        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
//            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId",
//                    Identifier.parse("e2c56db5-dffb-48d2-b060-d0f5a71096e0"), Identifier.parse("0"), Identifier.parse("0")));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

//    private class Item {
//        int rssi = 0;
//        long timestamp = 0;
//
//        public Item(int rssi, long timestamp){
//            this.rssi = rssi;
//            this.timestamp = timestamp;
//        }
//    }

    private class MyBeacon implements Comparable<MyBeacon> {
        private Beacon beacon;
        private boolean distance;
        private long timestamp;
//        private List<Item> items;

        public MyBeacon(Beacon beacon,boolean distance) {
            this.beacon = beacon;
            this.distance = distance;
            this.timestamp = System.currentTimeMillis();
//            this.items = new LinkedList<>();
        }

        public Beacon getBeacon() {
            return beacon;
        }

//        public List<Item> getItems() {
//            return items;
//        }

        public boolean isTimeExceeded() {

            // More then 3 seconds
            return (System.currentTimeMillis() - this.timestamp) > 10000;
        }

        public void refresh() {
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            if(!distance)
                return "id:" + beacon.getId1() + ", distance > 1m";
            else
                return "id:" + beacon.getId1() + ", distance < 1m";
        }

        @Override
        public int compareTo(@NonNull MyBeacon o) {
            return this.getBeacon().getId1().compareTo(o.getBeacon().getId1());
        }

        @Override
        public boolean equals(Object obj) {
            return this.compareTo((MyBeacon) obj) == 0;
        }
    }

    private List<MyBeacon> list = new LinkedList<>();

    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        final List<MyBeacon> list1 = new LinkedList<>();

        Log.i("Beacon", "Beacons: " + beacons.size());

        for (Beacon beacon: beacons) {
            Log.i("Beacon", "Beacons: " + beacon.getId1() + ", rssi: " + beacon.getRssi()) ;
//            Identifier namespaceId = beacon.getId1();
//            Identifier instanceId = beacon.getId2();

//            long millis = System.currentTimeMillis();
//
//            final Item item = new Item(beacon.getRssi(), millis);
//
//            items.add(item);
//
//            int i = 0;
//            double rssiSum = 0;
//            while(i < items.size()) {
//                if(items.get(i).timestamp < millis - 3000) {
//                    items.remove(i);
//                } else {
//                    rssiSum += items.get(i).rssi;
//                    i++;
//                }
//            }
//
//            double avgRssi = rssiSum/ items.size();

//            sumRSSI += beacon.getRssi();
//            i++;
//            avRSSI = sumRSSI/i;
//
//            double A = 0.000008;
//            double B = 46.449411;
//            double C = 0.9975586;
//            double distance = A * Math.pow((avRSSI*1.00/beacon.getTxPower()),B) + C;
//
//            double ratio_db = 52.0 - avRSSI;
//            double ratio_linear = Math.pow(10, ratio_db / 10);
//
//            double r = Math.sqrt(ratio_linear);
//
//
//            final String log = "id: "+namespaceId+
//                    " instance: "+instanceId+
//                    " din: "+beacon.getDistance()*2.2+"m\n" +
//                    "rssi:  " + beacon.getRssi() + "\n" +
//                    "tx:    " + beacon.getTxPower() + "\n" +
//                    "1: " + beacon.getId1() + "\n" +
//                    "2: " + beacon.getId2() + "\n" +
//                    "aRSSI: " + avRSSI + "\n" +
//                    "i:     " + i + "\n" +
//                    "distance:  " + distance + "\n" +
//                    "distance2: " + r + "\n\n" +
//                    "average: " + avgRssi;


            list1.add(new MyBeacon(beacon, beacon.getTxPower() < beacon.getRssi()));

            // Do we have telemetry data?
//            if (beacon.getExtraDataFields().size() > 0) {
//                long telemetryVersion = beacon.getExtraDataFields().get(0);
//                long batteryMilliVolts = beacon.getExtraDataFields().get(1);
//                long pduCount = beacon.getExtraDataFields().get(3);
//                long uptime = beacon.getExtraDataFields().get(4);
//
//            }
        }



        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                    TextView textView = ((TextView)CreateShowpieceActivity.this.findViewById(R.id.text));
//                    textView.setText(log);
//
//                    if (averageRSSI > tx) {
//                        textView.setTextColor(Color.GREEN);
//                    } else {
//                        textView.setTextColor(Color.MAGENTA);
//                    }
//                    logs.clear();

                boolean hasChanges = false;


                for (int i = 0; i < list.size(); ) {
                    MyBeacon item = list.get(i);
                    int index = list1.indexOf(item);
                    if (index == -1) {
                        if (item.isTimeExceeded()) {
                            hasChanges = true;
                            list.remove(i);
                            continue;
                        }
                    }
                    i++;
                }

                for (MyBeacon newItem: list1) {
                    int index = list.indexOf(newItem);
                    if (index == -1) {
                        hasChanges = true;
                        list.add(newItem);
                    } else {
                        MyBeacon item = list.get(index);
                        item.refresh();
                        if (item.distance != newItem.distance) {
                            item.distance = newItem.distance;
                            hasChanges = true;
                        }
                    }
                }

                if (hasChanges) {
                    Collections.sort(list);
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.beaconList);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }

                if(showpiece.isItemFocused() && showpiece.isHaveName())
                    findViewById(R.id.kreirajEksponat).setEnabled(true);
                else
                    findViewById(R.id.kreirajEksponat).setEnabled(false);
            }
        });
    }

}
