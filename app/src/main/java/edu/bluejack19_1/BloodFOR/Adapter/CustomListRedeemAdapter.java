package edu.bluejack19_1.BloodFOR.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import edu.bluejack19_1.BloodFOR.R;

public class CustomListRedeemAdapter extends ArrayAdapter<String> {

    private Context c;
    private String[] itemReedem;
    private Uri[] uris;


    public CustomListRedeemAdapter(Context c, String[] itemReedem, Uri[] uris) {
        super(c, R.layout.listredeemactivity,itemReedem);
        this.c = c;
        this.itemReedem = itemReedem;
        this.uris = uris;
    }

    @NonNull
    public View getView(int position , View view, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(c);
        View rowView = inflater.inflate(R.layout.listredeemactivity, null, true);

        TextView textMenu = (TextView) rowView.findViewById(R.id.reedemName);
        ImageView imageMenu = (ImageView) rowView.findViewById(R.id.imageView);

        textMenu.setText(itemReedem[position]);
        Glide.with(getContext())
                .load(uris[position])
                .apply(new RequestOptions().override(400, 400))
                .into(imageMenu);

        return rowView;
    }
}
