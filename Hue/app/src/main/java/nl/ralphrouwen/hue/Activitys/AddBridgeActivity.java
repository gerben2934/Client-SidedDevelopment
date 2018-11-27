package nl.ralphrouwen.hue.Activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
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

                String nameinput;
                String IPinput;
                String tokeninput;

                String ips = getIP(IP.getText().toString());

                if(name.getText().length() == 0 || ips.length() == 0 || Token.getText().length() == 0)
                {
                    Toast.makeText(getActivity(), "Not all fields are filled in correctly!", Toast.LENGTH_LONG).show();
                }


                else {
                    nameinput = name.getText().toString();
                    IPinput = ips;
                    tokeninput = Token.getText().toString();
                    bridgeFragmentListener.sendInput(nameinput, IPinput, tokeninput);
                    //System.out.println("Nameinput: " + nameinput + " IP INPUT: " + IPinput + " tokenInput: " + tokeninput);
                    dismiss();
                    Toast.makeText(getActivity(), "Added bridge: " + nameinput,
                            Toast.LENGTH_LONG).show();
                }
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

    public String getIP(String ip) {
        String IPADDRESS_PATTERN =
                "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(ip);
        if (matcher.find()) {
            //System.out.println("IP: " + ip);
            return matcher.group();
        } else {
            //System.out.println("FOUT IP");
            return "";
        }
    }
}
