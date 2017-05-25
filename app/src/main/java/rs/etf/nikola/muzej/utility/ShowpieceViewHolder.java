package rs.etf.nikola.muzej.utility;


import android.view.View;
import android.widget.ImageButton;

import rs.etf.nikola.muzej.R;

public class ShowpieceViewHolder extends MyViewHolder {
    public final ImageButton mImageButtonObrisi;

    public ShowpieceViewHolder(View v) {
        super(v);
        mImageButtonObrisi = (ImageButton) v.findViewById(R.id.obrisi);
    }
}
