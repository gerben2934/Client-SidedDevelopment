package nl.ralphrouwen.hue.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import nl.ralphrouwen.hue.Activitys.BridgeActivity;
import nl.ralphrouwen.hue.Models.Bridge;
import nl.ralphrouwen.hue.R;

import static nl.ralphrouwen.hue.Activitys.MainActivity.BRIDGE_URL;

public class BridgeRecyclerAdapter extends RecyclerView.Adapter<BridgeRecyclerAdapter.BridgeViewHolder> {

    private ArrayList<Bridge> dataset;
    private Context context;
    //private OnItemClickListener mListener;
    //public BridgeViewHolder viewHolder;

    public BridgeRecyclerAdapter(Context context, ArrayList<Bridge> bridges)
    {
        this.context = context;
        this.dataset = bridges;
    }

    //Create new views (used by the layout manager)
    @NonNull
    @Override
    public BridgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                               int viewType)
    {
        //create a new View here
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listviewbridgeitem, parent, false);

        return new BridgeViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(@NonNull BridgeViewHolder holder, int position)
    {
        Bridge bridge = dataset.get(position);
        holder.textview.setText(bridge.getName());
        //holder.mTextView.setText(dataset.get(position));
    }

    //Returns the size of the dataset
    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class BridgeViewHolder extends RecyclerView.ViewHolder {
        TextView textview;

        public BridgeViewHolder(View itemView, final Context ctx)
        {
            super(itemView);
            context = ctx;

            textview = itemView.findViewById(R.id.recycleView_BridgeName);

            //Listener toevoegen;
            itemView.setOnClickListener((View v) -> {
                Bridge bridge = dataset.get(getAdapterPosition());

                Intent intent = new Intent(context, BridgeActivity.class);
                intent.putExtra(BRIDGE_URL, (Parcelable) bridge);

                ctx.startActivity(intent);
            });
        }
    }
}
