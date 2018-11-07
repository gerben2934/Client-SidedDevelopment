package nl.ralphrouwen.blindwalls;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedActivity extends AppCompatActivity {

    TextView muralAuthorName;
    TextView muralDescription;
    TextView muralYear;
    ImageView muralImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        muralAuthorName = findViewById(R.id.detailedActivity_muralName);
        muralDescription = findViewById(R.id.detailedActivity_muralDescription);
        muralYear = findViewById(R.id.detailedActivity_muralYear);
        muralImage = findViewById(R.id.detailedActivity_muralImage);

        Intent intent = getIntent();
        Mural mural = (Mural) intent.getSerializableExtra("MURAL_OBJECT");
        //System.out.println(mural.);

        String authorString = getString(R.string.author, mural.getAuthor());
        muralAuthorName.setText(authorString);
        muralDescription.setText(mural.getDescriptionENG());
        muralYear.setText(mural.getYear());

        int resId = this.getResources().getIdentifier(
                mural.getImageURL(),
                "drawable",
                this.getPackageName());
        muralImage.setImageResource(resId);
    }
}
