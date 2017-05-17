package rs.etf.nikola.muzej.utility;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rs.etf.nikola.muzej.DemoExhibitActivity;
import rs.etf.nikola.muzej.EditExhibitActivity;
import rs.etf.nikola.muzej.R;


public class ExhibitAdapter<T> extends RecyclerView.Adapter<ExhibitViewHolder> {
    private Activity activity;
    protected final List<T> objects;

    public ExhibitAdapter(List<T> objects, Activity activity) {
        this.objects = objects;
        this.activity = activity;
    }

    @Override
    public ExhibitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item2, parent, false);

        return new ExhibitViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ExhibitViewHolder holder, int position) {
        holder.mTextView.setText(objects.get(position).toString());

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text = (TextView) v;
                Intent intent = new Intent(activity, DemoExhibitActivity.class);
                intent.putExtra("exhibitName", text.getText().toString());
                activity.startActivity(intent);
            }
        });

        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objects.remove(holder.getAdapterPosition());
                Museum.saveToDisk();
                ExhibitAdapter.this.notifyDataSetChanged();
            }
        });

        holder.mImageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditExhibitActivity.class);
                intent.putExtra("exhibitName", objects.get(holder.getAdapterPosition()).toString());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }
}
