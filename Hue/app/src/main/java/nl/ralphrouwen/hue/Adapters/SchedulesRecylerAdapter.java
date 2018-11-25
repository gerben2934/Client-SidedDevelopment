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

import nl.ralphrouwen.hue.Models.Bridge;
import nl.ralphrouwen.hue.Models.Light;
import nl.ralphrouwen.hue.Models.Schedule;
import nl.ralphrouwen.hue.R;


public class SchedulesRecylerAdapter extends RecyclerView.Adapter<SchedulesRecylerAdapter.ScheduleViewHolder> {
    private  Bridge bridge;
    private  ArrayList<Light> lights;
    private ArrayList<Schedule> dataset;
    private Context context;

    public SchedulesRecylerAdapter(Context context, ArrayList<Schedule> schedules, ArrayList<Light> lights, Bridge bridge)
    {
        this.lights = lights;
        this.bridge = bridge;
        this.context = context;
        this.dataset = schedules;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //create a new View here
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedules_item, parent, false);

        return new SchedulesRecylerAdapter.ScheduleViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = dataset.get(position);
        holder.textview.setText(schedule.getName());
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ScheduleViewHolder extends RecyclerView.ViewHolder {
        //public View view;
        //private Bridge bridge;
        TextView textview;

        public ScheduleViewHolder(View itemView, final Context ctx)
        {
            super(itemView);
            context = ctx;

//            textview = itemView.findViewById(R.id.recycleView_BridgeName);

            //Listener toevoegen;
            itemView.setOnClickListener((View v) -> {
                Schedule schedule = dataset.get(getAdapterPosition());

//                Intent intent = new Intent(context, BridgeActivity.class);
//                intent.putExtra(BRIDGE_URL, (Parcelable) bridge);

//                ctx.startActivity(intent);
            });
        }

    }
}
