package nl.ralphrouwen.hue.Activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import nl.ralphrouwen.hue.R;


public class AddBridgeActivity extends DialogFragment {

    public interface BridgeFragmentListener {
        void sendInput(String name, String ip, String token);
    }

    private BridgeFragmentListener bridgeFragmentListener;

    private static final String TAG = "MyCustomDialog";

    private EditText name;
    private EditText IP;
    private EditText Token;
    private Button addButton;

    public AddBridgeActivity() {
        // Required empty public constructor
    }

    public static AddBridgeActivity newInstance(Context context) {
        AddBridgeActivity fragment = new AddBridgeActivity();
        Bundle args = new Bundle();
        fragment.onAttach(context);
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

        View view = inflater.inflate(R.layout.fragment_add_bridge, container, false);
        name = view.findViewById(R.id.fragmentNameID);
        IP = view.findViewById(R.id.fragmentIPID);
        Token = view.findViewById(R.id.fragmentTokenID);
        addButton = view.findViewById(R.id.fragmentButtonID);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameinput = name.getText().toString();
                String IPinput = IP.getText().toString();
                String tokeninput = Token.getText().toString();
                
                bridgeFragmentListener.sendInput(nameinput, IPinput, tokeninput);
                dismiss();

                Toast.makeText(getActivity(), "Added bridge: " + nameinput,
                        Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            bridgeFragmentListener = (BridgeFragmentListener) context;
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );
        }
    }
}
