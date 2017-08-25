package rs.etf.nikola.muzej;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

import ar.com.daidalos.afiledialog.FileChooserDialog;
import rs.etf.nikola.muzej.utility.Museum;


public class ExportDialogFragment extends DialogFragment {
    View view;

    public ExportDialogFragment() {
        // Required empty public constructor
    }

    public static ExportDialogFragment newInstance() {
        return new ExportDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_export_dialog, null);
        builder.setView(view);

        final Dialog dialog = builder.create();

        ((TextView) view.findViewById(R.id.naslov)).setText("Izvezi Muzej");

        view.findViewById(R.id.izaberiLokaciju).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooserDialog fileChooserDialog = new FileChooserDialog(getActivity());

                fileChooserDialog.addListener(ExportDialogFragment.this.onFolderSelectedListener);

                fileChooserDialog.loadFolder(Environment.getExternalStorageDirectory().getPath());

                fileChooserDialog.setFolderMode(true);

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

        view.findViewById(R.id.izvezi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = ((EditText) view.findViewById(R.id.path)).getText().toString();
                if(!path.isEmpty()) {
                    Museum.saveToDisk(path);
                    dialog.dismiss();
                }
            }
        });

        return dialog;
    }

    private final FileChooserDialog.OnFileSelectedListener onFolderSelectedListener = new FileChooserDialog.OnFileSelectedListener() {
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
