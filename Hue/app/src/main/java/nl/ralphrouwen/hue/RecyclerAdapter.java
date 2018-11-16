package nl.ralphrouwen.hue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import nl.ralphrouwen.hue.Models.Bridge;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.BridgeViewHolder> {

    private ArrayList<Bridge> dataset;
    private Context context;
    //private OnItemClickListener mListener;
    //public BridgeViewHolder viewHolder;

/*    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }*/

    public RecyclerAdapter(Context context, ArrayList<Bridge> bridges)
    {
        this.context = context;
        this.dataset = bridges;
    }

    //Returns the size of the dataset
    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class BridgeViewHolder extends RecyclerView.ViewHolder {
        public View view;
        private Bridge bridge;
        public TextView textview;

        public BridgeViewHolder(View itemView, final Context ctx)
        {
            super(itemView);
            context = ctx;

            textview = itemView.findViewById(R.id.textViewtest);

            //Listener toevoegen;
        }


/*        public BridgeViewHolder(View tv) {
            super(tv);
            Log.i("View: ", tv.toString());
            view = tv;

            TextView textView;
            textView = tv.findViewById(R.id.textViewtest);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }*/

        public void bindItem(Bridge bridge) {
            TextView t = view.findViewById(R.id.textViewtest);
            Log.i("textview", t.toString());
            t.setText(bridge.getName());
        }
    }

        //Constructor depends on dataSet
        public RecyclerAdapter(ArrayList<Bridge> arrayList)
        {
            this.dataset = arrayList;
            //this.context = context;
        }

        //Create new views (used by the layout manager)
        @Override
        public RecyclerAdapter.BridgeViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType)
        {
            //create a new View here
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_recycler_view_item, parent, false);

            return new BridgeViewHolder(v, context);
        }

        @Override
        public void onBindViewHolder(BridgeViewHolder holder, int position)
        {
            Bridge bridge = dataset.get(position);
            holder.textview.setText(bridge.getName());
            //holder.mTextView.setText(dataset.get(position));
        }
}
