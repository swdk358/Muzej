package rs.etf.nikola.muzej.utility;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rs.etf.nikola.muzej.R;

public class MyAdapter<T> extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private final List<T> objects;

    public MyAdapter(List<T> objects) {
       this.objects = objects;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(objects.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }
}
