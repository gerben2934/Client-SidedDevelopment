package nl.ralphrouwen.hue.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import nl.ralphrouwen.hue.Helper.RequestListener;
import nl.ralphrouwen.hue.Helper.VolleyHelper;
import nl.ralphrouwen.hue.Models.Bridge;
import nl.ralphrouwen.hue.Activitys.BridgeActivity;
import nl.ralphrouwen.hue.Activitys.LightDetailedActivity;
import nl.ralphrouwen.hue.Models.Bridge;
import nl.ralphrouwen.hue.Models.Light;
import nl.ralphrouwen.hue.Models.Response;
import nl.ralphrouwen.hue.R;

import static nl.ralphrouwen.hue.Activitys.MainActivity.BRIDGE_URL;
import static nl.ralphrouwen.hue.Activitys.MainActivity.LIGHT_URL;
public class LightRecyclerAdapter extends RecyclerView.Adapter<LightRecyclerAdapter.LightViewHolder> implements RequestListener {

    private ArrayList<Light> lights;
    private Context context;
    private VolleyHelper api;
    private Bridge bridge;
    RequestListener request;
    int lastBrightness;

    public LightRecyclerAdapter(Context context, ArrayList<Light> lights, Bridge bridge) {
        this.context = context;
        this.lights = lights;
        this.bridge = bridge;
        request = this;

    }

    public void clear() {
        lights.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(ArrayList<Light> list) {
        lights.addAll(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public LightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycleviewlightitem, parent, false);

        return new LightViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final LightViewHolder viewHolder, int position) {
        final Light light = lights.get(position);
        viewHolder.lightName.setText(light.getName());

//        viewHolder.cardView.setCardBackgroundColor(Color.BLUE);

        viewHolder.lightSeekBar.setMin(0);
        viewHolder.lightSeekBar.setMax(254);
        viewHolder.lightSeekBar.setThumbOffset(0);
        //viewHolder.lightSeekBar.setProgress(light.brightness);
        viewHolder.lightSwitch.setEnabled(true);
        viewHolder.lightSwitch.setChecked(light.isOn());

        api = VolleyHelper.getInstance(context);

        if(!viewHolder.lightSwitch.isChecked())
        {
            viewHolder.lightSeekBar.setEnabled(false);
        }

        viewHolder.lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lastBrightness = viewHolder.lightSeekBar.getVerticalScrollbarPosition();
                api.changeLight(bridge, light, request, lastBrightness, light.getHue(), light.getSaturation(), isChecked);
                viewHolder.lightSeekBar.setEnabled(isChecked);
            }
        });

        viewHolder.lightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                api.changeLight(bridge, light, request, progress, light.getHue(), light.getSaturation(), true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    //Returns the size of the dataset
    @Override
    public int getItemCount() {
        return lights.size();
    }

    @Override
    public void onRequestObjectAvailible(JSONObject response, Response responsetype) {

    }

    @Override
    public void onRequestArrayAvailible(JSONArray response, Response responsetype) {

    }

    @Override
    public void onRequestError(Error error) {

    }


    public class LightViewHolder extends RecyclerView.ViewHolder {

        TextView lightName;
        SeekBar lightSeekBar;
        Switch lightSwitch;
        CardView cardView;

        public LightViewHolder(View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            cardView = itemView.findViewById(R.id.recyclerViewItem_CardView);
            lightName = itemView.findViewById(R.id.recycleViewItem_NameTextview);
            lightSeekBar = itemView.findViewById(R.id.recycleViewItem_SeekBar);
            lightSwitch = itemView.findViewById(R.id.recycleViewItem_ToggleButton);

            //Listener toevoegen;
            itemView.setOnClickListener((View v) -> {
                Light light = lights.get(getAdapterPosition());

                Intent intent = new Intent(context, LightDetailedActivity.class);
                intent.putExtra(LIGHT_URL, (Parcelable) light);
                intent.putExtra(BRIDGE_URL, (Parcelable) bridge);

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
