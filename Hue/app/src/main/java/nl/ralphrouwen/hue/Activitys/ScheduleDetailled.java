package nl.ralphrouwen.hue.Activitys;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nl.ralphrouwen.hue.Models.Schedule;
import nl.ralphrouwen.hue.R;


public class ScheduleDetailled extends DialogFragment {



    private OnFragmentInteractionListener mListener;

    public ScheduleDetailled() {
        // Required empty public constructor
    }

    public static final String SCHEDULES = "schedules";
    private TextView title;
    private TextView lamp;
    private TextView time;
    private TextView description;


    // TODO: Rename and change types and number of parameters
    public static ScheduleDetailled newInstance(Schedule schedules) {
        ScheduleDetailled fragment = new ScheduleDetailled();
        Bundle args = new Bundle();
        args.putParcelable(SCHEDULES, schedules);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_detailled_schedules, container, false);
        title = view.findViewById(R.id.schedulesname);
        lamp = view.findViewById(R.id.schedulesLamp);
        time = view.findViewById(R.id.schedulesTime);
        description = view.findViewById(R.id.schedulesdescription);

        Schedule schedule = getArguments().getParcelable(SCHEDULES);
        title.setText(schedule.getName());
        lamp.setText(schedule.getLamp());
        time.setText(schedule.getTime());
        description.setText(schedule.getDescription());

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
