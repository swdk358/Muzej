package rs.etf.nikola.muzej.utility;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTextView;

    public MyViewHolder(TextView v) {
        super(v);
        mTextView = v;
    }
}