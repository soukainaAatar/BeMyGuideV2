package com.example.bemyguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bemyguide.controllers.SessionManager;
import com.example.bemyguide.controllers.SqlManager;
import com.example.bemyguide.models.User;
import com.example.bemyguide.models.Ville;

import java.util.List;

public class Login extends AppCompatActivity {

    public static final String LOGIN_TYPE =
            "com.example.bemyguide.login_type";



    private String type;
    private TextView goToRegister;
    private Button loging;
    private EditText email;
    private EditText password;
    private TextView errorEmail;
    private TextView error;
    private SqlManager myDb;
    public SessionManager mysession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        goToRegister = findViewById(R.id.goInscription);
        loging = findViewById(R.id.loginButton_LI);
        email = findViewById(R.id.emailEditText_LI);
        password = findViewById(R.id.passwordEditText_LI);
        errorEmail = findViewById(R.id.errorEmail_LI);
        error = findViewById(R.id.error_LI);

        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(getApplicationContext(),SignUp.class);
                startActivity(signUp);
            }
        });
        myDb = new SqlManager(getApplicationContext());
        mysession = new SessionManager(getApplicationContext());
        type = getIntent().getStringExtra(LOGIN_TYPE);
        if (type.equals("logout")){

            email.setText(mysession.getUserEmail());
            mysession.logoutUser();
        }

         List<Ville> villes = myDb.getVilles(1);
        loging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!myDb.checkUser(email.getText().toString())){
                    errorEmail.setText("email non existant !!");
                }else{
                    if(!myDb.checkUser(email.getText().toString(),password.getText().toString())){
                        error.setText("email ou mot de passe incorrect");
                    }else{

                        mysession.createLoginSession(myDb.getProfile(email.getText().toString()));
                        Log.e("test", " after" );
                        myDb.close();
                        Intent categories = new Intent(Login.this,Categories.class);
                        startActivity(categories);
                    }
                }
            }
        });









    }
}
