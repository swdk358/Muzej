package rs.etf.nikola.muzej.utility;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import rs.etf.nikola.muzej.R;
import rs.etf.nikola.muzej.ShowpieceDialogFragment;


public class ShowpieceAdapter<T> extends RecyclerView.Adapter<ExhibitViewHolder> {
    private Activity activity;
    protected final List<T> objects;

    public ShowpieceAdapter(List<T> objects, Activity activity) {
        this.objects = objects;
        this.activity = activity;
    }

    @Override
    public ExhibitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ExhibitViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ExhibitViewHolder holder, int position) {
        holder.mTextView.setText(objects.get(position).toString());

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowpieceDialogFragment newFragment = ShowpieceDialogFragment.newInstance(
                        (Showpiece) objects.get(holder.getAdapterPosition()));
                newFragment.show(activity.getFragmentManager(),
                        ((Showpiece) objects.get(holder.getAdapterPosition())).getName());
            }
        });

        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objects.remove(holder.getAdapterPosition());
                ShowpieceAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }
}
