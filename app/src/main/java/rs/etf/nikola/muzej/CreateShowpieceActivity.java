package rs.etf.nikola.muzej;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
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
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ar.com.daidalos.afiledialog.FileChooserDialog;
import rs.etf.nikola.muzej.utility.BeaconAdapter;
import rs.etf.nikola.muzej.utility.MyBeacon;
import rs.etf.nikola.muzej.utility.Showpiece;

public class CreateShowpieceActivity extends AppCompatActivity implements BeaconConsumer, RangeNotifier {
    private final Showpiece showpiece = new Showpiece();
    private BeaconManager beaconManager;
    private boolean exit = false;

    private static final int PERMISSION_REQUEST = 1;
    private static final int BLUETOOTH_ENABLE_REQUEST_ID = 6;

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

        EditText editText = (EditText) findViewById(R.id.inputNazivEksponata);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String name = v.getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if(!name.isEmpty()) {
                    showpiece.setName(name);
                    showpiece.setHaveName(true);
                    return true;
                }
                else {
                    showpiece.setHaveName(false);
                    return false;
                }
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    showpiece.setItemFocused(false);
                else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.beaconList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final BeaconAdapter adapter = new BeaconAdapter(list, showpiece, editText);
        recyclerView.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setForegroundScanPeriod(800l);
        beaconManager.setForegroundBetweenScanPeriod(200l);

        beaconManager.setBackgroundScanPeriod(800l);
        beaconManager.setBackgroundBetweenScanPeriod(200l);

        beaconManager.bind(this);

        findViewById(R.id.kreirajEksponat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("showpiece", (Parcelable) showpiece);

                setResult(RESULT_OK, intent);
                exit = true;
                finish();
            }
        });

        findViewById(R.id.dodajUriImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooserDialog dialog = new FileChooserDialog(CreateShowpieceActivity.this);

                dialog.addListener(CreateShowpieceActivity.this.onImageSelectedListener);

                dialog.loadFolder(Environment.getExternalStorageDirectory().getPath());

                dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");

                dialog.setCanCreateFiles(false);

                dialog.show();
            }
        });

        findViewById(R.id.dodajUriText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooserDialog dialog = new FileChooserDialog(CreateShowpieceActivity.this);

                dialog.addListener(CreateShowpieceActivity.this.onTextSelectedListener);

                dialog.loadFolder(Environment.getExternalStorageDirectory().getPath());

                dialog.setFilter(".*txt");

                dialog.setCanCreateFiles(false);

                dialog.show();
            }
        });

        findViewById(R.id.dodajUriSound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooserDialog dialog = new FileChooserDialog(CreateShowpieceActivity.this);

                dialog.addListener(CreateShowpieceActivity.this.onSoundSelectedListener);

                Log.i("Beacon", "Path: " + Environment.getExternalStorageDirectory().getPath());

                dialog.loadFolder(Environment.getExternalStorageDirectory().getPath());

                dialog.setFilter(".*3gp|.*mp4|.*m4a|.*mp3|.*mkv|.*wav|.*ogg|.*amr");

                dialog.setCanCreateFiles(false);

                dialog.show();
            }
        });
    }

    private final FileChooserDialog.OnFileSelectedListener onImageSelectedListener = new FileChooserDialog.OnFileSelectedListener() {
        public void onFileSelected(Dialog source, File file) {
            source.dismiss();
            String path = file.getAbsolutePath();
            showpiece.setImage(path);
            EditText et = (EditText) CreateShowpieceActivity.this.findViewById(R.id.uriImage);
            et.setText(path);
        }
        public void onFileSelected(Dialog source, File folder, String name) {
            source.hide();
        }
    };

    private final FileChooserDialog.OnFileSelectedListener onTextSelectedListener = new FileChooserDialog.OnFileSelectedListener() {
        public void onFileSelected(Dialog source, File file) {
            source.dismiss();
            String path = file.getAbsolutePath();
            showpiece.setText(path);
            EditText et = (EditText) CreateShowpieceActivity.this.findViewById(R.id.uriText);
            et.setText(path);
        }
        public void onFileSelected(Dialog source, File folder, String name) {
            source.hide();
        }
    };

    private final FileChooserDialog.OnFileSelectedListener onSoundSelectedListener = new FileChooserDialog.OnFileSelectedListener() {
        public void onFileSelected(Dialog source, File file) {
            source.dismiss();
            String path = file.getAbsolutePath();
            showpiece.setSound(path);
            EditText et = (EditText) CreateShowpieceActivity.this.findViewById(R.id.uriSound);
            et.setText(path);
        }
        public void onFileSelected(Dialog source, File folder, String name) {
            source.hide();
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
        if(!exit) {
            Intent intent = new Intent();
            intent.putExtra("showpiece", (Parcelable) showpiece);

            setResult(RESULT_CANCELED, intent);
        }
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
        beaconManager.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
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
                if (grantResults.length > 1
                        && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since storage access has not been granted, this app will not be able to access storage.");
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

    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        final List<MyBeacon> list1 = new LinkedList<>();

        for (Beacon beacon: beacons)
            list1.add(new MyBeacon(beacon.getId1().toString(), beacon.getTxPower(), beacon.getRssi()));

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

                if (hasChanges) {
                    Collections.sort(list);
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.beaconList);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }

                if(list.isEmpty())
                    showpiece.setItemFocused(false);

                if (showpiece.isItemFocused() && showpiece.isHaveName())
                    findViewById(R.id.kreirajEksponat).setEnabled(true);
                else
                    findViewById(R.id.kreirajEksponat).setEnabled(false);
            }
        });
    }

}
