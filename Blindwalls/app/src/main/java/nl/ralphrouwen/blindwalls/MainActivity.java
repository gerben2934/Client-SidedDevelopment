package nl.ralphrouwen.blindwalls;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ApiListener {

    ApiManager manager;
    ListView muralListView;
    ArrayList<Mural> murals;
    ArrayAdapter<Mural> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        muralListView = findViewById(R.id.muralsListViewID);
        murals = new ArrayList<>();

//        adapter = new ArrayAdapter<Mural>(
//                this,
//                android.R.layout.simple_list_item_1,
//                murals
//        );

        adapter = new GalleryAdapter(this, murals);
        muralListView.setAdapter(adapter);

        manager = new ApiManager(getApplicationContext(), this);
        manager.getData();
    }

    @Override
    public void onMuralAvailable(Mural mural) {
        murals.add(mural);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMuralError(Error error) {

    }
}
