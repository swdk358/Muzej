package rs.etf.nikola.muzej.utility;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import rs.etf.nikola.muzej.R;

public class ExhibitViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTextView;
    public final ImageButton mImageButton;
    public final ImageButton mImageButton1;

    public ExhibitViewHolder(View v) {
        super(v);
        mTextView = (TextView) v.findViewById(R.id.text1);
        mImageButton = (ImageButton) v.findViewById(R.id.obrisi);
        mImageButton1 = (ImageButton) v.findViewById(R.id.izmeni);
    }
}
