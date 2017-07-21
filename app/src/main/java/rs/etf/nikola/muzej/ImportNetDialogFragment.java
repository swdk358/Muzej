package rs.etf.nikola.muzej;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import rs.etf.nikola.muzej.utility.DownloadService;
import rs.etf.nikola.muzej.utility.Museum;

public class ImportNetDialogFragment extends DialogFragment {
    View view;
    ProgressDialog mProgressDialog;
    Dialog dialog;
    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    public ImportNetDialogFragment() {
        // Required empty public constructor
    }

    public static ImportNetDialogFragment newInstance() {
        return new ImportNetDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Fragment fragment = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_import_net_dialog, null);
        builder.setView(view);

        dialog = builder.create();

        ((TextView) view.findViewById(R.id.naslov)).setText("Uvezi Muzej pomoću URL-a");

        view.findViewById(R.id.skenirajKod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = IntentIntegrator.forFragment(fragment);
                intentIntegrator.setCaptureActivity(MyCaptureActivity.class);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
            }
        });

        view.findViewById(R.id.odustani).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.uvezi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ((EditText) view.findViewById(R.id.url)).getText().toString();
                if(!url.isEmpty()) {
                    mProgressDialog = new ProgressDialog(getActivity());
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

                    mProgressDialog.show();
                    Intent intent = new Intent(getActivity(), DownloadService.class);
                    intent.putExtra("url", url);
                    intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                    getActivity().startService(intent);
                }
            }
        });

        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                ((EditText) view.findViewById(R.id.url)).setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class DownloadReceiver extends ResultReceiver {
        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == DownloadService.UPDATE_PROGRESS) {
                int progress = resultData.getInt("progress");
                mProgressDialog.setProgress(progress);
                if (progress == 100) {
                    int state = resultData.getInt("state");
                    if(state == 2) {
                        Toast toast = Toast.makeText(getActivity(), "Uvoz Muzeja nije uspeo", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (state == 1) {
                        Museum.instance = Museum.loadFromDisk(MyApplication.getAppContext().getFilesDir() + "/museum_new.dat");
                        Museum.saveToDisk();
                    }
                    mProgressDialog.dismiss();
                    dialog.dismiss();
                }
            }
        }
    }
}
