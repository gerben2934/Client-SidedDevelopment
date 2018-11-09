package nl.ralphrouwen.blindwalls;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

        muralListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.i("TAG_7november2018","" + position);

                Mural mural = murals.get(position);

                Intent intent = new Intent(getApplicationContext(), DetailedActivity.class);
                intent.putExtra("MURAL_OBJECT", mural);
                startActivity(intent);

            }
        });

        manager = new ApiManager(getApplicationContext(), this);
        manager.getData();
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putParcelableArrayList("MURALS", murals);
//    }
//
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        murals = savedInstanceState.getParcelableArrayList("MURALS");
//        //murals = (ArrayList<Mural>) savedInstanceState.getParcelableArrayList("MURALS");
//        adapter.notifyDataSetChanged();
//    }

    @Override
    public void onMuralAvailable(Mural mural) {
        murals.add(mural);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMuralError(Error error) {

    }
}
