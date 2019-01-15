package nl.ralphrouwen.locationawareapp.Activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import nl.ralphrouwen.locationawareapp.Fragments.HistoryFragment;
import nl.ralphrouwen.locationawareapp.Fragments.MapFragment;
import nl.ralphrouwen.locationawareapp.Helper.InputFilterMinMax;
import nl.ralphrouwen.locationawareapp.Helper.Constants;
import nl.ralphrouwen.locationawareapp.Models.Parked;
import nl.ralphrouwen.locationawareapp.Notification.NotificationHelper;
import nl.ralphrouwen.locationawareapp.R;

public class MainActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener {

    public static final String PARKEDLIST_URL = "parkedListURL";

    public static final String PARKED_URL = "parkedURL";
    private ArrayList<Parked> parkeds = new ArrayList<>();
    private Parked currentParked;

    private ImageButton parkbutton;
    private boolean parkButtonPressed;

    private static final int RC_SIGN_IN = 123;
    //private static final long PRETIME = 900000; //15 minuten
    //For testing:
    private static final long PRETIME = 30000; //30seconden

    //Textfields/textviews
    private EditText editDays;
    private EditText editHours;
    private EditText editMinutes;
    private TextView textDays;
    private TextView textHours;
    private TextView textMinutes;

    private RecyclerView.LayoutManager mLayoutManager;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationHelper notificationHelper;
    private static Geocoder geocoder;
    private Context mContext;
    private CountDownTimer countDownTimer1;
    private CountDownTimer countDownTimer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildHistoryFragment();
        createSignInIntent();
        mContext = getBaseContext();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        parkButtonPressed = false;
        bindComponents();
        fillComponents();

        notificationHelper = new NotificationHelper(this);
        parkbutton = findViewById(R.id.parkbutton);
        mLayoutManager = new LinearLayoutManager(this);
        geocoder = new Geocoder(mContext, Locale.getDefault());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void createSignInIntent() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()

        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.parkbutton3)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                //Succesvol ingelogd
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                generateParkeds();
            } else {
                createSignInIntent();
                //Inloggen is mislukt
            }
        }
    }

    public static String getAddress(LatLng location) {
        List<Address> addressList;
        String addressStr = "";

        try {
            addressList = geocoder.getFromLocation(location.latitude, location.longitude, 2);
            if (addressList != null && addressList.size() != 0) {
                addressStr = addressList.get(0).getThoroughfare() + " ";
                addressStr += addressList.get(0).getSubThoroughfare() + ", ";
                addressStr += addressList.get(0).getLocality();
            } else {
                Log.i("NULL or count() = 0", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressStr;
    }


    public void parkButtonPressed(View view) {
        if (!parkButtonPressed) {
            AlertDialog.Builder setLocationBuilder = new AlertDialog.Builder(this);
            setLocationBuilder.setTitle(getApplication().getString(R.string.setCarLocationTitle));
            setLocationBuilder.setMessage(getApplication().getString(R.string.setCarLocationText));
            setLocationBuilder.setPositiveButton(getApplication().getString(R.string.yes), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    Log.i("Dialog: ", "Clicked YES, closing dialog!");
                    parkbutton.setImageResource(R.drawable.parkbutton3);
                    parkButtonPressed = true;
                    int UniqueID = parkeds.size() + 1;
                    LatLng currentLocation = MapFragment.getMyLocation();
                    ArrayList<Integer> values = getValues();

                    DateTime begin = DateTime.now();
                    DateTime end = begin.plusDays(values.get(0));
                    end = end.plusHours(values.get(1));
                    end = end.plusMinutes(values.get(2));

                    currentParked = new Parked(UniqueID, (float) currentLocation.longitude, (float) currentLocation.latitude, begin, end, true, getAddress(currentLocation));
                    editDays.setText("");
                    editHours.setText("");
                    editMinutes.setText("");
                    Log.e("arraylistsize", String.valueOf(UniqueID));

                    //Firebase deel:
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    Log.e("userLogin!", uid);
                    DatabaseReference restaurantRef = FirebaseDatabase
                            .getInstance()
                            .getReference(Constants.FIREBASE_CHILD_PARKS)
                            .child(uid);
                    Parked firebaseParked = new Parked(UniqueID, (float) currentLocation.longitude, (float) currentLocation.latitude, begin.getMillis(), end.getMillis(), true, getAddress(currentLocation));
                    restaurantRef.child("parks").child(String.valueOf(firebaseParked.getId())).setValue(firebaseParked);

                    MapFragment.setParkedMarker(currentParked);
                    dialog.dismiss();

                    long millis = end.getMillis() - begin.getMillis();
                    //create notification when the parking is not valid anymore:
                    createNofitication(millis, false);

                    //create 15 min before end reminder notification:
                    if (millis > PRETIME) {
                        millis -= PRETIME;
                    }
                    createNofitication(millis, true);

                    Toast.makeText(mContext, R.string.notificationReminderTitle1, 3);
                }
            });

            setLocationBuilder.setNegativeButton(getApplication().getString(R.string.no), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("Dialog: ", "Clicked NO, closing dialog!");
                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alertSet = setLocationBuilder.create();
            alertSet.show();

        } else {
            AlertDialog.Builder removeLocationBuilder = new AlertDialog.Builder(this);
            removeLocationBuilder.setTitle(getApplication().getString(R.string.removeCarLocationTitle));
            removeLocationBuilder.setMessage(getApplication().getString(R.string.removeCarLocationText));
            removeLocationBuilder.setPositiveButton(getApplication().getString(R.string.yes), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    parkbutton.setImageResource(R.drawable.parkbutton5);
                    parkButtonPressed = false;
                    // Do nothing but close the dialog
                    Log.i("Dialog: ", "Clicked YES, closing dialog!");
                    MapFragment.removeParkedMarker();
                    countDownTimer1.cancel();
                    countDownTimer2.cancel();
                    dialog.dismiss();
                }
            });

            removeLocationBuilder.setNegativeButton(getApplication().getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("Dialog: ", "Clicked NO, closing dialog!");
                    // Do nothing
                    dialog.dismiss();
                }
            });
            AlertDialog alertSet = removeLocationBuilder.create();
            alertSet.show();
        }
    }

    //First: check if currentLocation != null
    //Create the notification
    public void createNofitication(Long millis, boolean reminder) {
        if (reminder) //reminder notification 15 min before end
        {
            String title = getResources().getString(R.string.notificationReminderTitle1);
            String body = getResources().getString(R.string.notificationReminderText2) + " " + (PRETIME/60000) + " " + getResources().getString(R.string.notificationReminderText3);

            countDownTimer1 = new CountDownTimer(millis, 1000) {
                @Override
                public void onTick(long timeLeft) {
                    //Log.i("TIME", "Time left: " + timeLeft);
                }

                @Override
                public void onFinish() {
                    //Log.e("REMINDER", "Time lefft!!");
                    notificationHelper.postNotification(1, title, body, Optional.empty());
                    notificationHelper.getNotification1(title, body, Optional.empty());
                }
            }.start();


        } else { // actual notification!
            String title = getResources().getString(R.string.notificationFinalTitle);
            String body = getResources().getString(R.string.notificationFinalText);

            countDownTimer2 = new CountDownTimer(millis, 1000) {
                @Override
                public void onTick(long timeLeft) {
                    Log.i("TIME", "Time left: " + timeLeft);
                }

                @Override
                public void onFinish() {
                    Log.e("FINISHED", "Time is over!");
                    Parked parked = parkeds.get(0);
                    notificationHelper.postNotification(2, title, body, Optional.ofNullable(parked));
                    notificationHelper.getNotification1(title, body, java.util.Optional.ofNullable(parked));

                    //set parked object: valid to false, since the parked is not valid anymore, because the timer ran out.
                    Parked lastParked = parkeds.get(0);
                    lastParked.setValid(false);

                    LatLng l = new LatLng((double)lastParked.getLatitude(), (double)lastParked.getLongitude());

                    //1 nu lastParked omzetten naar een lastFireBaseParked object!
                    //2 het object vervangen meth et nieuwe object (valid = false);

                    Parked firebaseParked = new Parked(parkeds.size(), (float) l.longitude, (float) l.latitude, lastParked.getStarttimelong(), lastParked.getEndtimelong(), false, lastParked.getStreetName());

                    //Log.e("newFireBaseParked: ", "New firebase parked Object: " + firebaseParked.toString());

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    //Log.e("userLogin!", uid);
                    DatabaseReference restaurantRef = FirebaseDatabase
                            .getInstance()
                            .getReference(Constants.FIREBASE_CHILD_PARKS)
                            .child(uid);

                    restaurantRef.child("parks").child(String.valueOf(lastParked.getId())).setValue(firebaseParked);
                    //Log.e("NEW PARKED OBJECT: ", ": " + restaurantRef.child("parks").child(String.valueOf(lastParked.getId()).toString()));

                    parkbutton.setImageResource(R.drawable.parkbutton5);
                    parkButtonPressed = false;
                    MapFragment.removeParkedMarker();
                    MapFragment.setMarker(lastParked);
                    System.out.println("new object ?" + restaurantRef.child("parks").child(String.valueOf(firebaseParked.getId())).toString());
                }
            }.start();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void bindComponents() {
        parkbutton = findViewById(R.id.parkbutton);
        editDays = findViewById(R.id.editText_days);
        editHours = findViewById(R.id.editText_hours);
        editMinutes = findViewById(R.id.editText_minutes);
        textDays = findViewById(R.id.textView_days);
        textHours = findViewById(R.id.textView_hours);
        textMinutes = findViewById(R.id.textView_minutes);
    }

    public void fillComponents() {
        textDays.setText(getResources().getString(R.string.days));
        textHours.setText(getResources().getString(R.string.hours));
        textMinutes.setText(getResources().getString(R.string.minutes));
        editDays.setFilters(new InputFilter[]{new InputFilterMinMax(0, 7), new InputFilter.LengthFilter(1)});
        editHours.setFilters(new InputFilter[]{new InputFilterMinMax(0, 23), new InputFilter.LengthFilter(2)});
        editMinutes.setFilters(new InputFilter[]{new InputFilterMinMax(0, 59), new InputFilter.LengthFilter(2)});
    }

    public ArrayList<Integer> getValues() {
        ArrayList<Integer> values = new ArrayList<>();

        values.add(getInt(editDays.getText().toString(), 0));
        values.add(getInt(editHours.getText().toString(), 0));
        values.add(getInt(editMinutes.getText().toString(), 0));
        return values;
    }

    public void generateParkeds() {

        //login:
        // sign in with email:
        // rasrouwe@avans.nl
        // ralph123

        //login:
        // pedooren@avans.nl
        // kaaskaas (?)

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference reference = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_PARKS)
                .child(uid)
                .child("parks");

////
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HistoryFragment.refreshRecylcerView();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Parked parked = dataSnapshot1.getValue(Parked.class);
                    parked.convertEndTime();
                    parked.convertStartTime();
                    HistoryFragment.updateRecyclerView(parked, true);
                    //Log.e("datasnapshot", parked.toString());
                }
                //Log.i("PARKED SIZE: HF ", "PArked: " + parkeds.size());
                for (int i = 0; i < parkeds.size(); i++) {
                    Parked parked = parkeds.get(i);
                    if (parked.equals(currentParked)) {
                        MapFragment.setParkedMarker(parked);
                    }
                    else {
                        //MapFragment.setMarker(parked);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.e("mapppp", String.valueOf(databaseError));
            }
        });
    }

    private void buildHistoryFragment() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        HistoryFragment hf = HistoryFragment.newInstance(parkeds);
        fragmentManager.beginTransaction().replace(R.id.fragment_history, hf).commit();
    }

    public int getInt(String edtValue, int defaultValue) {
        int value = defaultValue;

        if (edtValue != null) {
            try {
                value = Integer.parseInt(edtValue);
            } catch (NumberFormatException e) {
                value = defaultValue;
            }
        }
        return value;
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
