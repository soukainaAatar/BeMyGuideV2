package com.example.bemyguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bemyguide.controllers.SyncroTask;
import com.example.bemyguide.models.User;


public class Categories extends AppCompatActivity {

    private Button carteButton;
    private Button villesButton;
    private Button placesButton;

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
        setContentView(R.layout.activity_categories);

        carteButton = findViewById(R.id.carteButton);
        placesButton = findViewById(R.id.placeButton);
        villesButton = findViewById(R.id.villeButton);

        carteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map = new Intent(Categories.this,MyMap.class);
                map.putExtra(MyMap.MAP_TYPE,"all");
                startActivity(map);
            }
        });
        placesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent places = new Intent(Categories.this,Places.class);
                places.putExtra(Places.PLACES_TYPE,"all");
                startActivity(places);
            }
        });
        villesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent villes = new Intent(Categories.this,Villes.class);
                startActivity(villes);
            }
        });

    }
}
