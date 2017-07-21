package rs.etf.nikola.muzej;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

import ar.com.daidalos.afiledialog.FileChooserDialog;
import rs.etf.nikola.muzej.utility.Museum;

public class ImportDiskDialogFragment extends DialogFragment {
    View view;
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

    public ImportDiskDialogFragment() {
        // Required empty public constructor
    }

    public static ImportDiskDialogFragment newInstance() {
        return new ImportDiskDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_import_disk_dialog, null);
        builder.setView(view);

        final Dialog dialog = builder.create();

        ((TextView) view.findViewById(R.id.naslov)).setText("Uvezi Muzej iz skladišta podataka");

        view.findViewById(R.id.izaberiFajl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooserDialog fileChooserDialog = new FileChooserDialog(getActivity());

                fileChooserDialog.addListener(ImportDiskDialogFragment.this.onFileSelectedListener);

                fileChooserDialog.loadFolder(Environment.getExternalStorageDirectory().getPath());

                fileChooserDialog.setFilter(".*dat");

                fileChooserDialog.setCanCreateFiles(false);

                fileChooserDialog.show();
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
                String path = ((EditText) view.findViewById(R.id.path)).getText().toString();
                if(!path.isEmpty()) {
                    Museum.instance = Museum.loadFromDisk(path);
                    Museum.saveToDisk();
                    dialog.dismiss();
                }
            }
        });

        return dialog;
    }

    private final FileChooserDialog.OnFileSelectedListener onFileSelectedListener = new FileChooserDialog.OnFileSelectedListener() {
        public void onFileSelected(Dialog source, File file) {
            source.dismiss();
            String path = file.getAbsolutePath();
            EditText et = (EditText) view.findViewById(R.id.path);
            et.setText(path);
        }
        public void onFileSelected(Dialog source, File folder, String name) {
            source.dismiss();
        }
    };
}
