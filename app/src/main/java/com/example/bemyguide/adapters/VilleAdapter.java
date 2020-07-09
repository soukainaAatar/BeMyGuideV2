package com.example.bemyguide.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bemyguide.Places;
import com.example.bemyguide.R;
import com.example.bemyguide.models.Ville;

import java.util.List;

public class VilleAdapter extends ArrayAdapter<Ville> {
    Context context;

    List<Ville> villes;
    public VilleAdapter(Context context, int layoutToBeInflated, List<Ville> villes) {
        super(context, R.layout.ville_row, villes);
        this.context = context;

        this.villes = villes;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         final int indice = position;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.ville_row, null);

        TextView villeText = row.findViewById(R.id.villeText);
        ImageView ville_image = row.findViewById(R.id.ville_image);
        TextView placesCount =  row.findViewById(R.id.v_placesCount);
        TextView visitsCount =  row.findViewById(R.id.v_visitsCount);
        Button visit =  row.findViewById(R.id.v_visit);
        TextView likesCount =  row.findViewById(R.id.v_likesCount);
        Button like =  row.findViewById(R.id.v_like);

        villeText.setText(villes.get(position).getNom());
        placesCount.setText(villes.get(position).getPlaces()+"P");
        visitsCount.setText(villes.get(position).getVisits()+"V");
        likesCount.setText(villes.get(position).getLikes()+"L");
        ville_image.setBackgroundResource(context.getResources().getIdentifier(villes.get(position).getPhoto(), "drawable", context.getPackageName()));

        ville_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent places = new Intent(context, Places.class);
                places.putExtra(Places.PLACES_TYPE,"many");
                places.putExtra("ville_id",villes.get(indice).getId());
                context.startActivity(places);
            }
        });
        //new BitmapWorkerTask2(icon,context).execute(thumbnails[position]);
        // icon.setImageResource(thumbnails[position]);
        return (row);
    }
}// CustomAdapter
