package com.example.bemyguide.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bemyguide.MyMap;
import com.example.bemyguide.Place_detail;
import com.example.bemyguide.Profile;
import com.example.bemyguide.R;
import com.example.bemyguide.controllers.SessionManager;
import com.example.bemyguide.controllers.SqlManager;
import com.example.bemyguide.models.Place;
import com.example.bemyguide.models.Reaction;

import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class PlaceAdapter extends ArrayAdapter<Place> {
    Context context;

    List<Place> places;
    ListView place_list;
    TextView profile_visitsValue;
    public PlaceAdapter(Context context, int layoutToBeInflated, List<Place> places,ListView place_list,TextView profile_visitsValue) {
        super(context, R.layout.ville_row, places);
        this.context = context;
        this.place_list = place_list;
        this.places = places;
        this.profile_visitsValue = profile_visitsValue;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.place_row, null);

        TextView ps_userText = row.findViewById(R.id.ps_userText);
        ImageView ps_icon = row.findViewById(R.id.ps_icon);
        TextView ps_description =  row.findViewById(R.id.ps_description);
        TextView ps_VilleText =  row.findViewById(R.id.ps_VilleText);
        ImageButton ps_goVisit =  row.findViewById(R.id.ps_goVisit);
        final TextView ps_visitsCount =  row.findViewById(R.id.ps_visitsCount);
        Button ps_visits =  row.findViewById(R.id.ps_visits);
        final TextView ps_likesCount =  row.findViewById(R.id.ps_likesCount);
        Button ps_likes =  row.findViewById(R.id.ps_likes);


        final SessionManager mysession = new SessionManager(context);
        final SqlManager myDb = new SqlManager(context);
        ps_userText.setText(places.get(position).getOwner().getNom()+" "+ places.get(position).getOwner().getPrenom());
        ps_description.setText(places.get(position).getDescription());
        ps_VilleText.setText("à : " +myDb.getVille(places.get(position).getVille_id(),mysession.getUserId()).getNom());
        ps_visitsCount.setText(places.get(position).getVisits()+"V");
        ps_likesCount.setText(places.get(position).getLikes()+"L");

        if(places.get(position).isVisit_by_me()){

            ps_visits.setBackgroundResource(android.R.drawable.checkbox_on_background);
        }else{
            ps_visits.setBackgroundResource(android.R.drawable.checkbox_off_background);
        }
        if(places.get(position).isLike_by_me()){
            ps_likes.setBackgroundResource(R.drawable.like_on);
        }else{
            ps_likes.setBackgroundResource(R.drawable.like_off);
        }
        Log.e("pho" ,""+ places.get(position));
        ps_icon.setBackgroundResource(context.getResources().getIdentifier(places.get(position).getPicture().getPhoto().toLowerCase(), "drawable", context.getPackageName()));


        ps_userText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user = new Intent(context, Profile.class);
                user.putExtra(Profile.USER_TYPE,"user" );
                user.putExtra("user_id",places.get(position).getUser_id());
                context.startActivity(user);
            }
        });
        ps_goVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent map = new Intent(context, MyMap.class);
                map.putExtra(MyMap.MAP_TYPE,"one");
                map.putExtra("place_id",places.get(position).getId());
                context.startActivity(map);
            }
        });

        ps_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent detail = new Intent(context, Place_detail.class);
                detail.putExtra("place_id",places.get(position).getId());
                context.startActivity(detail);
            }
        });
        ps_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent detail = new Intent(context, Place_detail.class);
                detail.putExtra("place_id",places.get(position).getId());
                context.startActivity(detail);
            }
        });

        ps_visits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reaction r = myDb.getReaction(mysession.getUserId(), places.get(position).getId(), "visit");
                if (r == null) {
                    r = new Reaction(mysession.getUserId(), places.get(position).getId(), "visit", 1);
                    myDb.addModel(r);
                    places.get(position).setVisit_by_me(true);
                    places.get(position).setVisits(places.get(position).getVisits()+1);
                    ps_visitsCount.setText(places.get(position).getVisits()+"V");
                    if (profile_visitsValue != null){
                        profile_visitsValue.setText(myDb.countUserReaction(places.get(position).getUser_id(),"visit"));
                    }
                    v.setBackgroundResource(android.R.drawable.checkbox_on_background);

                } else {
                    if (r.getSync() == 2) {
                        r.setSync(1);
                        myDb.editModel(r);
                        places.get(position).setVisit_by_me(true);
                        places.get(position).setVisits(places.get(position).getVisits()+1);
                        ps_visitsCount.setText(places.get(position).getVisits()+"V");
                        if (profile_visitsValue != null){
                            profile_visitsValue.setText(myDb.countUserReaction(places.get(position).getUser_id(),"visit"));
                        }
                        v.setBackgroundResource(android.R.drawable.checkbox_on_background);

                    }else{
                        r.setSync(2);
                        myDb.editModel(r);
                        places.get(position).setVisit_by_me(false);
                        places.get(position).setVisits(places.get(position).getVisits()-1);
                        ps_visitsCount.setText(places.get(position).getVisits()+"V");
                        if (profile_visitsValue != null){
                            profile_visitsValue.setText(myDb.countUserReaction(places.get(position).getUser_id(),"visit"));
                        }
                        v.setBackgroundResource(android.R.drawable.checkbox_off_background);

                    }
                }
                place_list.invalidateViews();
            }
        });

        ps_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reaction r = myDb.getReaction(mysession.getUserId(), places.get(position).getId(), "like");
                if (r == null) {
                    r = new Reaction(mysession.getUserId(), places.get(position).getId(), "like", 1);
                    myDb.addModel(r);
                    places.get(position).setLike_by_me(true);
                    places.get(position).setLikes(places.get(position).getLikes()+1);
                    ps_likesCount.setText(places.get(position).getLikes()+"L");
                    v.setBackgroundResource(R.drawable.like_on);

                } else {
                    if (r.getSync() == 2) {
                        r.setSync(1);
                        myDb.editModel(r);
                        places.get(position).setLike_by_me(true);
                        places.get(position).setLikes(places.get(position).getLikes()+1);
                        ps_likesCount.setText(places.get(position).getLikes()+"L");
                        v.setBackgroundResource(R.drawable.like_on);

                    }else{
                        r.setSync(2);
                        myDb.editModel(r);
                        places.get(position).setLike_by_me(false);
                        places.get(position).setLikes(places.get(position).getLikes()-1);
                        ps_likesCount.setText(places.get(position).getLikes()+"L");
                        v.setBackgroundResource(R.drawable.like_off);
                    }
                }
                place_list.invalidateViews();
            }
        });




        //new BitmapWorkerTask2(icon,context).execute(thumbnails[position]);
        // icon.setImageResource(thumbnails[position]);
        return (row);
    }
}// CustomAdapter
