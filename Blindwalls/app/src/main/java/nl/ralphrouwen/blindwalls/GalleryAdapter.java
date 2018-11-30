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
        final ViewHolderItem holder;

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_listview_item,
                    parant,
                    false
            );
            holder = new ViewHolderItem();
            holder.author = convertView.findViewById(R.id.authorID);
            holder.thumbnail  = convertView.findViewById(R.id.imageViewID);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolderItem) convertView.getTag();
        }

        holder.author.setText(mural.getAuthor());

        String imageUrl = mural.getImageURL();
        Picasso.get().load(imageUrl).into(holder.thumbnail);

        return convertView;
    }

    static class ViewHolderItem {
        private TextView author;
        private ImageView thumbnail;
    }
}
