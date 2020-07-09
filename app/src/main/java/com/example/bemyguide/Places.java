package com.example.bemyguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bemyguide.adapters.PlaceAdapter;
import com.example.bemyguide.adapters.VilleAdapter;
import com.example.bemyguide.controllers.SessionManager;
import com.example.bemyguide.controllers.SqlManager;
import com.example.bemyguide.controllers.SyncroTask;
import com.example.bemyguide.models.Place;

import java.util.ArrayList;
import java.util.List;


public class Places extends AppCompatActivity {

    private ListView places_list;
    private SqlManager myDb;
    public SessionManager mysession ;
    List<Place> places;

    public static final String PLACES_TYPE =
            "com.example.bemyguide.places_type";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater m = new MenuInflater(this);
        m.inflate(R.menu.share_menu,menu);

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.m_logout ){
            Intent login = new Intent(getApplicationContext(),Login.class);
            login.putExtra(Login.LOGIN_TYPE,"logout" );
            mysession.logoutUser();
            startActivity(login);
        }

        if(item.getItemId() == R.id.m_syncro ){
            SyncroTask sc= new SyncroTask(getApplicationContext());
            sc.execute(this.getApplicationContext().getString(R.string.url_all));
        }

        if(item.getItemId() == R.id.m_profile ){
            Intent profile = new Intent(getApplicationContext(), Profile.class);
            profile.putExtra(Profile.USER_TYPE,"profile" );
            startActivity(profile);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);


        myDb = new SqlManager(getApplicationContext());
        mysession = new SessionManager(getApplicationContext());

         Intent in = getIntent();
        String  type = in.getStringExtra(PLACES_TYPE);

        if (type.equals("all")){
            places = myDb.getAllPlaces(mysession.getUserId());
        }
        if (type.equals("many")){

            places = myDb.getPlaces(in.getIntExtra("ville_id",0),mysession.getUserId());

        }



        places_list = findViewById(R.id.places_list);
      PlaceAdapter placeAdapter = new PlaceAdapter(this,R.layout.place_row,places,places_list,null);
        places_list.setAdapter(placeAdapter);

    }
}
