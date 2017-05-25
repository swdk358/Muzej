package rs.etf.nikola.muzej.utility;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import rs.etf.nikola.muzej.R;

public class BeaconAdapter extends MyAdapter<MyBeacon, MyViewHolder> {
    private final Showpiece showpiece;
    private int focusPos;
    private EditText editText;

    public BeaconAdapter(List<MyBeacon> objects, Showpiece showpiece, EditText editText) {
        super(objects);
        this.showpiece = showpiece;
        this.focusPos = 0;
        this.editText = editText;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_beacon, parent, false);

        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if(focusPos >= getItemCount())
            focusPos = getItemCount() - 1;

        if(focusPos == position && !editText.isFocused()) {
            holder.mTextView.setFocusableInTouchMode(true);
            holder.mTextView.requestFocus();
            holder.mTextView.setFocusableInTouchMode(false);
            showpiece.setItemFocused(true);
        }

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setFocusableInTouchMode(true);
                v.requestFocus();
                v.setFocusableInTouchMode(false);
                Log.i("Beacon", "Onclick");
                TextView text = (TextView) v;
                String s = text.getText().toString();
                String uuid = s.split("\n")[0].split(":")[1];
                showpiece.setBeaconUUID(uuid);
                showpiece.setItemFocused(true);
                focusPos = holder.getAdapterPosition();
            }
        });
    }
}
