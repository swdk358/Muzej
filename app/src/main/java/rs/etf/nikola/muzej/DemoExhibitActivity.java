package rs.etf.nikola.muzej;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import rs.etf.nikola.muzej.utility.Exhibit;
import rs.etf.nikola.muzej.utility.Museum;
import rs.etf.nikola.muzej.utility.MyBeacon;
import rs.etf.nikola.muzej.utility.Showpiece;

public class DemoExhibitActivity extends AppCompatActivity implements BeaconConsumer, RangeNotifier {
    private BeaconManager beaconManager;
    private Exhibit exhibit;
    private Showpiece currentSP;

    private Button brewind, bpause, bplay, bforward;
    private SeekBar seekbar;
    private static MediaPlayer mediaPlayer;

    private int currentTime = 0;
    private int finalTime = 0;

    private final Handler myHandler = new Handler();
    private final int forwardTime = 5000;
    private final int backwardTime = 5000;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int BLUETOOTH_ENABLE_REQUEST_ID = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_exhibit);

        setTitle(R.string.activity_demo_exhibit_title);

        brewind = (Button) findViewById(R.id.button);
        bpause = (Button) findViewById(R.id.button2);
        bplay = (Button) findViewById(R.id.button3);
        bforward = (Button) findViewById(R.id.button4);

        seekbar = (SeekBar) findViewById(R.id.seekBar);
        brewind.setEnabled(false);
        bpause.setEnabled(false);
        bplay.setEnabled(false);
        bforward.setEnabled(false);

        bplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                bpause.setEnabled(true);
                bplay.setEnabled(false);
            }
        });

        bpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                bpause.setEnabled(false);
                bplay.setEnabled(true);
            }
        });

        bforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = currentTime;

                if((temp + forwardTime) <= finalTime)
                    currentTime = currentTime + forwardTime;
                else
                    currentTime = finalTime;
                mediaPlayer.seekTo(currentTime);
            }
        });

        brewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = currentTime;

                if((temp - backwardTime) > 0)
                    currentTime = currentTime - backwardTime;
                else
                    currentTime = 0;
                mediaPlayer.seekTo(currentTime);
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    if (mediaPlayer != null && fromUser) {
                        mediaPlayer.seekTo(progress);
                        currentTime = mediaPlayer.getCurrentPosition();
                        seekbar.setProgress(currentTime);
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ImageView iw = (ImageView) findViewById(R.id.imageView);
        iw.setImageResource(R.drawable.noimage);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, BLUETOOTH_ENABLE_REQUEST_ID);
        }

        String exhibitName = getIntent().getExtras().getString("exhibitName");

        exhibit = Museum.getExhibitByName(exhibitName);
        if(exhibit == null)
            finish();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
        }

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setForegroundScanPeriod(800l);
        beaconManager.setForegroundBetweenScanPeriod(200l);

        beaconManager.setBackgroundScanPeriod(800l);
        beaconManager.setBackgroundBetweenScanPeriod(200l);

        beaconManager.bind(this);

    }

    private final Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if(mediaPlayer != null) {
                currentTime = mediaPlayer.getCurrentPosition();
                seekbar.setProgress(currentTime);
                myHandler.postDelayed(this, 100);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_ENABLE_REQUEST_ID) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Region region = new Region("all-beacons-region", null, null, null);
        try {
            beaconManager.stopMonitoringBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        beaconManager.removeAllRangeNotifiers();
        try {
            beaconManager.stopRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        beaconManager.removeAllRangeNotifiers();
        beaconManager.unbind(this);
        if(mediaPlayer != null)
            mediaPlayer.release();
        mediaPlayer = null;
        myHandler.removeCallbacks(UpdateSongTime);
        super.onDestroy();
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


    @Override
    public void onBeaconServiceConnect() {
        Region region = new Region("all-beacons-region", null, null, null);
        try {
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        beaconManager.addRangeNotifier(this);

        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private final List<MyBeacon> list = new LinkedList<>();

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        final List<MyBeacon> list1 = new LinkedList<>();
        for (Beacon beacon: beacons)
            list1.add(new MyBeacon(beacon.getId1().toString(), beacon.getTxPower(), beacon.getRssi()));

        final AppCompatActivity activity = this;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
                        boolean distance = item.isInRangeActivate();
                        item.addRssi(newItem.getTxPower(), newItem.getRssiList());
                        if (item.isInRangeActivate() != distance)
                            hasChanges = true;
                    }
                }

                if (hasChanges)
                    Collections.sort(list);

                boolean isSame = false;
                boolean isFound = false;
                double minDistance = 1.5;
                for (MyBeacon item: list)
                    if(item.isInRangeDeactivate() && item.getDistance() < minDistance) {
                        minDistance = item.getDistance();
                        String uuid = item.getBeaconUUID();
                        Showpiece sp = exhibit.getShowpieceByUUID(uuid);
                        if(sp != null)
                            if(sp.equals(currentSP))
                                isSame = true;
                            else {
                                if(currentSP != null)
                                    currentSP.setProgress(mediaPlayer.getCurrentPosition());
                                currentSP = sp;
                                isSame = false;
                                isFound = true;
                            }
                    }

                if(!isSame)
                    if(!isFound) {
                        ImageView iw = (ImageView) findViewById(R.id.imageView);
                        iw.setImageResource(R.drawable.noimage);

                        TextView tw = (TextView) findViewById(R.id.textView);
                        tw.setText(null);

                        if(currentSP != null)
                            currentSP.setProgress(mediaPlayer.getCurrentPosition());
                        currentSP = null;

                        if(mediaPlayer != null) {
                            mediaPlayer.reset();
                            myHandler.removeCallbacks(UpdateSongTime);

                            brewind.setEnabled(false);
                            bpause.setEnabled(false);
                            bplay.setEnabled(false);
                            bforward.setEnabled(false);

                            seekbar.setProgress(0);
                        }
                    }
                    else {
                        ImageView iw = (ImageView) findViewById(R.id.imageView);
                        iw.setImageURI(Uri.parse(currentSP.getImage()));

                        InputStream is;
                        try {
                            is = getContentResolver().openInputStream(Uri.parse(currentSP.getText()));
                            byte[] b = new byte[is.available()];
                            if (is.available() == is.read(b)) {
                                TextView tw = (TextView) findViewById(R.id.textView);
                                tw.setText(new String(b));
                                tw.setMovementMethod(new ScrollingMovementMethod());
                            }
                            is.close();
                        } catch (IOException e) {
                            try{
                                is = getContentResolver().openInputStream(Uri.fromFile(new File(currentSP.getText())));
                                byte[] b = new byte[is.available()];
                                if (is.available() == is.read(b)) {
                                    TextView tw = (TextView) findViewById(R.id.textView);
                                    tw.setText(new String(b));
                                    tw.setMovementMethod(new ScrollingMovementMethod());
                                }
                                is.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }

                        if(mediaPlayer != null) {
                            mediaPlayer.reset();
                            try {
                                mediaPlayer.setDataSource(activity, Uri.parse(currentSP.getSound()));
                                mediaPlayer.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            mediaPlayer = MediaPlayer.create(activity, Uri.parse(currentSP.getSound()));
                        }
                        mediaPlayer.setLooping(true);
                        mediaPlayer.seekTo(currentSP.getProgress());

                        finalTime = mediaPlayer.getDuration();
                        currentTime = mediaPlayer.getCurrentPosition();
                        seekbar.setMax(finalTime);
                        seekbar.setProgress(currentTime);

                        myHandler.postDelayed(UpdateSongTime, 100);

                        brewind.setEnabled(true);
                        bforward.setEnabled(true);
                        bplay.setEnabled(true);
                        bpause.setEnabled(false);
                    }
            }
        });
    }
}
