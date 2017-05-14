package rs.etf.nikola.muzej.utility;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import rs.etf.nikola.muzej.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTextView;

    public MyViewHolder(View v) {
        super(v);
        mTextView = (TextView) v.findViewById(R.id.text1);
    }
}
