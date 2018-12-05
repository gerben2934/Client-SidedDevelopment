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

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.ArrayList;

import nl.ralphrouwen.locationawareapp.Activitys.DetailedParked_Activity;
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
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int position) {
        Parked parked = parkingHistory.get(position);
        recyclerViewHolder.streetName.setText(parked.getStreetName());

        //Format date in dd:month:yyyy
        DateTime date = parked.getStartTime();
        String dateString = date.toString("dd/MMM/yyyy");
        recyclerViewHolder.date.setText(R.string.date + " " + dateString);

        //Format parking length in dd:hh:mm
        DateTime start = parked.getStartTime();
        DateTime end = parked.getEndTime();
        Period timeSpan = new Period(start, end);
        String elapsed = "";
        if (timeSpan.getDays() > 0) {
            elapsed += R.string.days + " " + timeSpan.getDays();
        }
        if (timeSpan.getHours() > 0) {
            elapsed += timeSpan.getHours();
        }
        if (timeSpan.getMinutes() > 0) {
            elapsed += ":" + timeSpan.getMinutes();
        } else {
            elapsed = "ERROR parsing time!";
        }
        elapsed = R.string.timeParked + ": " + elapsed;
        recyclerViewHolder.timeParked.setText(elapsed);
    }

    @Override
    public int getItemCount() {
        return parkingHistory.size();
    }

    public void addAll(ArrayList<Parked> list) {
        parkingHistory.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        parkingHistory.clear();
        notifyDataSetChanged();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView streetName;
        TextView date;
        TextView timeParked;

        public RecyclerViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            streetName = itemView.findViewById(R.id.parkeditem_streetName);
            date = itemView.findViewById(R.id.parkeditem_dateParked);
            timeParked = itemView.findViewById(R.id.parkeditem_timeParked);

            itemView.setOnClickListener((View v) -> {
                Parked parked = parkingHistory.get(getAdapterPosition());
                Intent intent = new Intent(context, DetailedParked_Activity.class);
                intent.putExtra(PARKED_URL, (Parcelable) parked);
            });
        }
    }
}
