package rs.etf.nikola.muzej.utility;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rs.etf.nikola.muzej.R;

public class MyAdapter<T> extends RecyclerView.Adapter<MyViewHolder> {
    protected final List<T> objects;

    public MyAdapter(List<T> objects) {
       this.objects = objects;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item1, parent, false);

        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextView.setText(objects.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }
}
