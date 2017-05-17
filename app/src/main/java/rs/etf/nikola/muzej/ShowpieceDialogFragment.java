package rs.etf.nikola.muzej;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import rs.etf.nikola.muzej.utility.Showpiece;


public class ShowpieceDialogFragment extends DialogFragment {
    private static final String ARG_PARAM1 = "showpiece";

    private Showpiece showpiece;


    public ShowpieceDialogFragment() {
        // Required empty public constructor
    }

    public static ShowpieceDialogFragment newInstance(Showpiece param1) {
        ShowpieceDialogFragment fragment = new ShowpieceDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            showpiece = getArguments().getParcelable(ARG_PARAM1);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_showpiece_dialog, null);
        builder.setView(view);

        Dialog dialog = builder.create();

        ((TextView) view.findViewById(R.id.beaconUUID)).setText(showpiece.getBeaconUUID());
        ((TextView) view.findViewById(R.id.slika)).setText(showpiece.getImage());
        ((TextView) view.findViewById(R.id.tekst)).setText(showpiece.getText());
        ((TextView) view.findViewById(R.id.zvuk)).setText(showpiece.getSound());

        ((TextView) view.findViewById(R.id.naslov)).setText(showpiece.getName());

        return dialog;
    }
}
