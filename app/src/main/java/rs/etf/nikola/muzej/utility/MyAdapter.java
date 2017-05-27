package rs.etf.nikola.muzej.utility;

import android.support.v7.widget.RecyclerView;

import java.util.List;

abstract class MyAdapter<T1, T2 extends MyViewHolder> extends RecyclerView.Adapter<T2> {
    final List<T1> objects;

    MyAdapter(List<T1> objects) {
       this.objects = objects;
    }

    public void onBindViewHolder(final T2 holder, int position) {
        holder.mTextView.setText(objects.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }
}
