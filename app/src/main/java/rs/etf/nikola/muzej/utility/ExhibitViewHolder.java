package rs.etf.nikola.muzej.utility;

import android.view.View;
import android.widget.ImageButton;

import rs.etf.nikola.muzej.R;

public class ExhibitViewHolder extends ShowpieceViewHolder {
    public final ImageButton mImageButtonIzmeni;

    public ExhibitViewHolder(View v) {
        super(v);
        mImageButtonIzmeni = (ImageButton) v.findViewById(R.id.izmeni);
    }
}
