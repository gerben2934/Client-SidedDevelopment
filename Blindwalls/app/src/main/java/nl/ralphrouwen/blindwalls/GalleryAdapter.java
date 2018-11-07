package nl.ralphrouwen.blindwalls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryAdapter extends ArrayAdapter<Mural> {

    public GalleryAdapter(Context context, ArrayList<Mural> items)
    {
        super(context,0,items);
    }

    public View getView(int position, View convertView, ViewGroup parant)
    {
        Mural mural = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_listview_item,
                    parant,
                    false
            );
        }

        TextView author = convertView.findViewById(R.id.authorID);
        final ImageView thumbnail = convertView.findViewById(R.id.imageViewID);

        author.setText(mural.getAuthor());

        String imageUrl = mural.getImageURL();
        Picasso.get().load(imageUrl).into(thumbnail);

        return convertView;
    }
}
