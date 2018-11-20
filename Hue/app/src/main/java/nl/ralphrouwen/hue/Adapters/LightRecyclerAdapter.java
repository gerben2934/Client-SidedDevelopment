package nl.ralphrouwen.hue.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import nl.ralphrouwen.hue.Activitys.BridgeActivity;
import nl.ralphrouwen.hue.Activitys.LightDetailedActivity;
import nl.ralphrouwen.hue.Models.Bridge;
import nl.ralphrouwen.hue.Models.Light;
import nl.ralphrouwen.hue.R;

import static nl.ralphrouwen.hue.Activitys.MainActivity.BRIDGE_URL;
import static nl.ralphrouwen.hue.Activitys.MainActivity.LIGHT_URL;

public class LightRecyclerAdapter extends RecyclerView.Adapter<LightRecyclerAdapter.LightViewHolder> {

    private ArrayList<Light> lights;
    private Context context;

    public LightRecyclerAdapter(Context context, ArrayList<Light> lights) {
        this.context = context;
        this.lights = lights;
    }

    @NonNull
    @Override
    public LightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycleviewlightitem, parent, false);

        return new LightViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(@NonNull LightViewHolder viewHolder, int position) {
        Light light = lights.get(position);
        viewHolder.lightName.setText(light.getName());
        //viewHolder.lightSeekBar.
        viewHolder.lightSwitch.setEnabled(light.on);
    }

    //Returns the size of the dataset
    @Override
    public int getItemCount() {
        return lights.size();
    }

    public class LightViewHolder extends RecyclerView.ViewHolder {

        TextView lightName;
        SeekBar lightSeekBar;
        Switch lightSwitch;

        public LightViewHolder(View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            lightName = itemView.findViewById(R.id.recycleViewItem_NameTextview);
            lightSeekBar = itemView.findViewById(R.id.recycleViewItem_SeekBar);
            lightSwitch = itemView.findViewById(R.id.recycleViewItem_ToggleButton);

            //Listener toevoegen;
            itemView.setOnClickListener((View v) -> {
                Light light = lights.get(getAdapterPosition());

                Intent intent = new Intent(context, LightDetailedActivity.class);
                intent.putExtra(LIGHT_URL, (Parcelable) light);

                ctx.startActivity(intent);
            });

/*            itemView.setOnClickListener((View v) -> {
                Light light = lights.get(getAdapterPosition());

                Intent intent = new Intent(context, LightActivity.class);
                intent.putExtra(EXTRA_URL, (Parcelable) light);

                ctx.startActivity(intent);
            });*/

            //clicklistener

            //tv
        }
    }
}
