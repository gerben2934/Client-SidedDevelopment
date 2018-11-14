package nl.ralphrouwen.hue;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private String[] mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public MyViewHolder(TextView tv) {
            super(tv);
            mTextView = tv;
        }
    }

        //Constructor depends on dataSet
        public MyAdapter(String[] myDataset)
        {
            mDataset = myDataset;
        }

        //Create new views (used by the layout manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType)
        {
            //create a new View here
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_main, parent, false);

            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            holder.mTextView.setText(mDataset[position]);
        }

        //Returns the size of the dataset
        @Override
        public int getItemCount() {
            return mDataset.length;
        }
}
