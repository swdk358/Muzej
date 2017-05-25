package rs.etf.nikola.muzej.utility;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import rs.etf.nikola.muzej.R;
import rs.etf.nikola.muzej.ShowpieceDialogFragment;


public class ShowpieceAdapter extends MyAdapter<Showpiece, ShowpieceViewHolder> {
    private Activity activity;

    public ShowpieceAdapter(List<Showpiece> objects, Activity activity) {
        super(objects);
        this.activity = activity;
    }

    @Override
    public ShowpieceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_showpiece, parent, false);

        return new ShowpieceViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ShowpieceViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowpieceDialogFragment newFragment = ShowpieceDialogFragment.newInstance(
                        (Showpiece) objects.get(holder.getAdapterPosition()));
                newFragment.show(activity.getFragmentManager(),
                        ((Showpiece) objects.get(holder.getAdapterPosition())).getName());
            }
        });

        holder.mImageButtonObrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objects.remove(holder.getAdapterPosition());
                ShowpieceAdapter.this.notifyDataSetChanged();
            }
        });
    }
}
