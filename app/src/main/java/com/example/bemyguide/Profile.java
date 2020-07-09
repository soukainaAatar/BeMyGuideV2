package com.example.bemyguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bemyguide.adapters.PlaceAdapter;
import com.example.bemyguide.controllers.SessionManager;
import com.example.bemyguide.controllers.SqlManager;
import com.example.bemyguide.controllers.SyncroTask;
import com.example.bemyguide.models.Place;
import com.example.bemyguide.models.User;

import java.util.List;

public class Profile extends AppCompatActivity {

    public static final String USER_TYPE =
            "com.example.bemyguide.user_type";

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

    private ListView places_list;
    private SqlManager myDb;
    public SessionManager mysession;
    List<Place> places;
    User user;

    ImageView profile_Avatar;
    TextView profile_nom_prenom;
    TextView profile_Email;
    TextView profile_commentsValue;
    TextView profile_placeValue;
    TextView profile_visitsValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile_Avatar = findViewById(R.id.profile_Avatar);
        profile_nom_prenom = findViewById(R.id.profile_nom_prenom);
        profile_Email = findViewById(R.id.profile_Email);
        profile_commentsValue = findViewById(R.id.profile_commentsValue);
        profile_placeValue = findViewById(R.id.profile_placeValue);
        profile_visitsValue = findViewById(R.id.profile_visitsValue);

        myDb = new SqlManager(getApplicationContext());
        mysession = new SessionManager(getApplicationContext());

        Intent in = getIntent();
        String  type = in.getStringExtra(USER_TYPE);

        if (type.equals("profile")){
            places = myDb.getMyPlaces(mysession.getUserId());
            user = myDb.getUser(mysession.getUserId());
        }
        if (type.equals("user")){

            places = myDb.getMyPlaces(in.getIntExtra("user_id",0));
            user = myDb.getUser(in.getIntExtra("user_id",0));

        }

        profile_nom_prenom.setText(user.getNom()+" "+user.getPrenom());
        profile_Email.setText(user.getEmail());
        profile_commentsValue.setText(myDb.countUserComments(user.getId()));
        profile_placeValue.setText(myDb.countMyPlaces(user.getId()));
        profile_visitsValue.setText(myDb.countUserReaction(user.getId(),"visit"));
        Log.e("usr",""+this.getResources().getIdentifier(user.getAvatar(), "drawable", this.getPackageName()));
        profile_Avatar.setBackgroundResource(this.getResources().getIdentifier(user.getAvatar(), "drawable", this.getPackageName()));




        places_list = findViewById(R.id.my_list);
        PlaceAdapter placeAdapter = new PlaceAdapter(this,R.layout.place_row,places,places_list,profile_visitsValue);
        places_list.setAdapter(placeAdapter);


    }
}
