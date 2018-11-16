package nl.ralphrouwen.hue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class RecyclerViewItem extends AppCompatActivity {

    private TextView lightNameTextView;
    private SeekBar seekBar;
    private Switch toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_item);

        lightNameTextView = findViewById(R.id.recycleViewItem_NameTextview);
        seekBar = findViewById(R.id.recycleViewItem_SeekBar);
        toggleButton = findViewById(R.id.recycleViewItem_ToggleButton);
    }
}
