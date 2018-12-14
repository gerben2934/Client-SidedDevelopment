package nl.ralphrouwen.locationawareapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import nl.ralphrouwen.locationawareapp.Activitys.MainActivity;
import nl.ralphrouwen.locationawareapp.Adapters.ParkedAdapter;
import nl.ralphrouwen.locationawareapp.Models.Parked;
import nl.ralphrouwen.locationawareapp.R;

public class HistoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<Parked> parkeds;
    private ParkedAdapter mAdapter;
    private OnFragmentInteractionListener mListener;
    private Context mcontext;

    public HistoryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        generateParkeds();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        mRecyclerView = view.findViewById(R.id.fragment_history_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(parkeds != null)
        {
            mAdapter = new ParkedAdapter(mcontext, parkeds);
            mRecyclerView.setAdapter(mAdapter);

        }
        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext = context;
        if (mcontext instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) mcontext;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void generateParkeds()
    {
        parkeds = new ArrayList<>();
        parkeds.add(new Parked(1, 4.7927f, 51.5857f, new DateTime(2018, 10, 22, 0, 0),
                new DateTime(2018, 10, 23, 5, 10), false, "Lijsterbesstraat 15"));
        parkeds.add(new Parked(2, 4.6721458f, 51.86096769f, new DateTime(2018, 11, 10, 0, 12),
                new DateTime(2018, 11, 10, 10, 12), false, "Straat 2"));
    }

    public ArrayList<Parked> getParkeds() {
        return parkeds;
    }
}
