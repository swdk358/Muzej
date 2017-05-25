package rs.etf.nikola.muzej.utility;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rs.etf.nikola.muzej.DemoExhibitActivity;
import rs.etf.nikola.muzej.EditExhibitActivity;
import rs.etf.nikola.muzej.R;


public class ExhibitAdapter extends MyAdapter<Exhibit, ExhibitViewHolder> {
    private Activity activity;

    public ExhibitAdapter(List<Exhibit> objects, Activity activity) {
        super(objects);
        this.activity = activity;
    }

    @Override
    public ExhibitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_exhibit, parent, false);

        return new ExhibitViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ExhibitViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text = (TextView) v;
                Intent intent = new Intent(activity, DemoExhibitActivity.class);
                intent.putExtra("exhibitName", text.getText().toString());
                activity.startActivity(intent);
            }
        });

        holder.mImageButtonObrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objects.remove(holder.getAdapterPosition());
                Museum.saveToDisk();
                ExhibitAdapter.this.notifyDataSetChanged();
            }
        });

        holder.mImageButtonIzmeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditExhibitActivity.class);
                intent.putExtra("exhibitName", objects.get(holder.getAdapterPosition()).toString());
                activity.startActivity(intent);
            }
        });
    }
}
