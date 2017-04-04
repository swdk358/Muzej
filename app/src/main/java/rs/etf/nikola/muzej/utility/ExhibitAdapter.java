package rs.etf.nikola.muzej.utility;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import rs.etf.nikola.muzej.DemoExhibitActivity;


public class ExhibitAdapter<T> extends MyAdapter<T> {
    Activity activity;

    public ExhibitAdapter(List<T> objects, Activity activity) {
        super(objects);
        this.activity = activity;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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
    }
}
