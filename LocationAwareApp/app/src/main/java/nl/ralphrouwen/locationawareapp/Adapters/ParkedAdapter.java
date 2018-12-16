package nl.ralphrouwen.locationawareapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;

import nl.ralphrouwen.locationawareapp.Activitys.DetailedParked_Activity;
import nl.ralphrouwen.locationawareapp.Models.Parked;
import nl.ralphrouwen.locationawareapp.R;

import static nl.ralphrouwen.locationawareapp.Activitys.MainActivity.PARKED_URL;

public class ParkedAdapter extends RecyclerView.Adapter<ParkedAdapter.ParkedViewHolder> {

    private ArrayList<Parked> parkingHistory;
    private static Context context;
    private LayoutInflater mInflater;

    public ParkedAdapter(Context context, ArrayList<Parked> parkeds)
    {
        this.context = context;
        this.parkingHistory = parkeds;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ParkedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_parkeditem, parent, false);
        return new ParkedViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkedViewHolder recyclerViewHolder, int position) {
        Parked parked = parkingHistory.get(position);
        recyclerViewHolder.streetName.setText(context.getResources().getString(R.string.street) + " " + parked.getStreetName());

        //Format date in dd:month:yyyy
        DateTime date = parked.getStartTime();
        String dateString = date.toString("dd/MMM/yyyy");
        recyclerViewHolder.date.setText(context.getResources().getString(R.string.date) + " " + " " + dateString);

        String elapsed = context.getResources().getString(R.string.timeParked) + parked.getParkedTime(context);;
        recyclerViewHolder.timeParked.setText(elapsed);
    }

    @Override
    public int getItemCount() {
        return parkingHistory.size();
    }

    public void addAll(ArrayList<Parked> list) {
        parkingHistory.addAll(list);
    }

    public void clear() {
        parkingHistory.clear();
    }

    public class ParkedViewHolder extends RecyclerView.ViewHolder {

        TextView streetName;
        TextView date;
        TextView timeParked;

        public ParkedViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            streetName = itemView.findViewById(R.id.parkeditem_streetName);
            date = itemView.findViewById(R.id.parkeditem_dateParked);
            timeParked = itemView.findViewById(R.id.parkeditem_timeParked);

            itemView.setOnClickListener((View v) -> {
                Log.i("CLICKED: ", "ID" + parkingHistory.get(getAdapterPosition()).getId() + ".");
                Parked parked = parkingHistory.get(getAdapterPosition());
                Intent intent = new Intent(context, DetailedParked_Activity.class);
                intent.putExtra(PARKED_URL, (Parcelable) parked);
                context.getApplicationContext().startActivity(intent);
            });
        }
    }
}
