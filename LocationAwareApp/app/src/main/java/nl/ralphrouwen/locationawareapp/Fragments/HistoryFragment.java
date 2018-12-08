package nl.ralphrouwen.locationawareapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import nl.ralphrouwen.locationawareapp.Activitys.MainActivity;
import nl.ralphrouwen.locationawareapp.Adapters.RecyclerViewAdapter;
import nl.ralphrouwen.locationawareapp.Models.Parked;
import nl.ralphrouwen.locationawareapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<Parked> parkeds;
    private RecyclerViewAdapter mAdapter;
    private OnFragmentInteractionListener mListener;
    private Context context;


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        generateParkeds();
        //context = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        mRecyclerView = view.findViewById(R.id.fragment_history_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.setLayoutManager(new FrameLayout(getActivity()));
       /* mAdapter = new RecyclerViewAdapter(this, parkeds);
        mRecyclerView.setAdapter(mAdapter);*/

        return view;
    }

    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
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
        parkeds.add(new Parked(1, 4.7927f, 51.5857f, new DateTime(2018, 10, 22, 0, 0),
                new DateTime(2018, 10, 23, 0, 0), false, "Lijsterbesstraat 15"));
    }

    public ArrayList<Parked> getParkeds() {
        return parkeds;
    }
}
