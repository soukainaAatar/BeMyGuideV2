package com.example.bemyguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import com.example.bemyguide.adapters.VilleAdapter;
import com.example.bemyguide.controllers.SessionManager;
import com.example.bemyguide.controllers.SqlManager;
import com.example.bemyguide.controllers.SyncroTask;
import com.example.bemyguide.models.Ville;

import java.util.ArrayList;
import java.util.List;


public class Villes extends Activity {

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

    private ListView villes_list;
    private SqlManager myDb;
    public SessionManager mysession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_villes);


        myDb = new SqlManager(getApplicationContext());
        mysession = new SessionManager(getApplicationContext());
        Log.e("active",  " here villes_get");
        final List<Ville> villes = myDb.getVilles(mysession.getUserId());
        Log.e("active",  " here villes_get");
        villes_list = findViewById(R.id.ville_list);
        Log.e("active",  " here villes");
        VilleAdapter villeAdapter = new VilleAdapter(this,R.layout.ville_row,villes);
        Log.e("active",  " there villes");
        villes_list.setAdapter(villeAdapter);



    }
}
