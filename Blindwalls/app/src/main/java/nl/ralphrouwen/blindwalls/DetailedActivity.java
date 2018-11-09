package nl.ralphrouwen.blindwalls;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

public class DetailedActivity extends AppCompatActivity {

    TextView muralAuthorName;
    TextView muralDescription;
    TextView muralYear;
    ImageView muralImage;
    FloatingActionButton locationButton;
    Mural mural;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        muralAuthorName = findViewById(R.id.detailedActivity_muralName);
        muralDescription = findViewById(R.id.detailedActivity_muralDescription);
        muralYear = findViewById(R.id.detailedActivity_muralYear);
        muralImage = findViewById(R.id.detailedActivity_muralImage);
        locationButton = findViewById(R.id.locationButtonID);

        Intent intent = getIntent();
        //mural = (Mural) intent.getSerializableExtra("MURAL_OBJECT");
        mural = (Mural) intent.getParcelableExtra("MURAL_OBJECT");

        //System.out.println(mural.);

        String authorString = getString(R.string.author, mural.getAuthor());

        //Telefoon is in het Nederlands
        if(Locale.getDefault().getLanguage().equals("nl")) {
            muralDescription.setText(mural.getDescriptionNL());
        }

        //Telefoon is in het Engels
        if(Locale.getDefault().getLanguage().equals("en")) {
            muralDescription.setText(mural.getDescriptionENG());
        }

        muralDescription.setMovementMethod(new ScrollingMovementMethod());
        String yearString = getString(R.string.year, mural.getYear());

        muralAuthorName.setText(authorString);
        muralYear.setText(yearString);

        String imageUrl = mural.getImageURL();
        Picasso.get().load(imageUrl).into(muralImage);


        locationButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent2 = new Intent(getApplicationContext(), MapsActivity.class);
            intent2.putExtra("MURAL_OBJECT", mural);
            startActivity(intent2);
        }
    });

    }

}
