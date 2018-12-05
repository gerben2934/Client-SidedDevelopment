package nl.ralphrouwen.locationawareapp.Adapters;

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

import nl.ralphrouwen.locationawareapp.DetailedParked_Activity;
import nl.ralphrouwen.locationawareapp.Models.Parked;
import nl.ralphrouwen.locationawareapp.R;
import nl.ralphrouwen.locationawareapp.Helper.RequestListener;

import static nl.ralphrouwen.locationawareapp.Activitys.MainActivity.PARKED_URL;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private ArrayList<Parked> parkingHistory;
    private Context context;
    RequestListener request;

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recyclerview_parkeditem, parent, false);

        return new RecyclerViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return parkingHistory.size();
    }

    public void addAll(ArrayList<Parked> list)
    {
        parkingHistory.addAll(list);
    }

    public void clear() {
        parkingHistory.clear();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView streetName;
        TextView time;

        public RecyclerViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            streetName = itemView.findViewById(R.id.parkeditem_streetName);
            time = itemView.findViewById(R.id.parkeditem_startTimeParked);

            itemView.setOnClickListener((View v) -> {
                Parked parked = parkingHistory.get(getAdapterPosition());
                Intent intent = new Intent(context, DetailedParked_Activity.class);
                intent.putExtra(PARKED_URL, (Parcelable) parked);
            });
        }
    }
}
