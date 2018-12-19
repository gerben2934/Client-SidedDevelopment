package nl.ralphrouwen.locationawareapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nl.ralphrouwen.locationawareapp.Adapters.ParkedAdapter;
import nl.ralphrouwen.locationawareapp.Models.Parked;
import nl.ralphrouwen.locationawareapp.R;

public class HistoryFragment extends Fragment {

    private static RecyclerView mRecyclerView;
    private static ArrayList<Parked> parkeds;
    private static ParkedAdapter mAdapter;
    private OnFragmentInteractionListener mListener;
    private Context mcontext;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(ArrayList<Parked> list) {
        HistoryFragment fragment = new HistoryFragment();
        parkeds = list;
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

    @Override
    public void onStart(){
        super.onStart();
    }

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

    public static ArrayList<Parked> getParkeds() {
        return parkeds;
    }

    public void refreshRecylcerView()
    {
        mAdapter.notifyDataSetChanged();
    }

    public static void updateRecyclerView(Parked parked, boolean addToView) {
        if(addToView)
        {
            parkeds.add(0, parked);
            mAdapter.notifyItemInserted(0);
            mRecyclerView.scrollToPosition(0);
        }
        else {
            parkeds.remove(parked);
            mAdapter.notifyItemRemoved(0);
        }

    }
}
