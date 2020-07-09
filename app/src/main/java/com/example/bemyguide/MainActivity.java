package com.example.bemyguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import com.example.bemyguide.controllers.SyncroTask;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                    SyncroTask sc= new SyncroTask(getApplicationContext());
                    sc.execute(this.getApplicationContext().getString(R.string.url_all));



        Intent login = new Intent(MainActivity.this,Login.class);
        login.putExtra(Login.LOGIN_TYPE,"login" );
        startActivity(login);
    }



}
