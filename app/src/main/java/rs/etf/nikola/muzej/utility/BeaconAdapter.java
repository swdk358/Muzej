package rs.etf.nikola.muzej.utility;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rs.etf.nikola.muzej.R;

public class BeaconAdapter<T> extends RecyclerView.Adapter<BeaconAdapter.ViewHolder> {
    private final List<T> objects;
    private final Showpiece showpiece;

    public BeaconAdapter(List<T> objects, Showpiece showpiece) {
        this.objects = objects;
        this.showpiece = showpiece;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextView;

        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setFocusableInTouchMode(true);
                    v.requestFocus();
                    v.setFocusableInTouchMode(false);
                    Log.i("Beacon", "Onclick");
                    TextView text = (TextView) v;
                    String s = text.getText().toString();
                    String uuid = s.split(",")[0].split(":")[1];
                    showpiece.setBeaconUUID(uuid);
                    showpiece.setItemFocused(true);
                }
            });

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(BeaconAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(objects.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }
}
