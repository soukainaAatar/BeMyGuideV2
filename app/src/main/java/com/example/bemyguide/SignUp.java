package com.example.bemyguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bemyguide.controllers.SessionManager;
import com.example.bemyguide.controllers.SqlManager;
import com.example.bemyguide.models.User;

public class SignUp extends AppCompatActivity {

    private TextView goToLogin;
    private Button register;
    private EditText email;
    private EditText password;
    private EditText nom;
    private EditText prenom;
    private TextView error;
    private SqlManager myDb;
    public SessionManager mysession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        goToLogin = findViewById(R.id.goLogin);
        register = findViewById(R.id.singUp);
        email = findViewById(R.id.emailEditText_SU);
        password = findViewById(R.id.passwordEditText_SU);
        nom = findViewById(R.id.nomText_SU);
        prenom = findViewById(R.id.prenomText_SU);
        error = findViewById(R.id.error_SU);

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(),Login.class);
                login.putExtra(Login.LOGIN_TYPE,"login" );
                startActivity(login);
            }
        });

        myDb = new SqlManager(getApplicationContext());
        mysession = new SessionManager(getApplicationContext());

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(myDb.checkUser(email.getText().toString())){
                    error.setText("email existe déja !!");
                }else{

                        User user = new User(prenom.getText().toString()+"_"+nom.getText().toString(),email.getText().toString(),password.getText().toString(),prenom.getText().toString(),nom.getText().toString(),1);
                        myDb.addModel(user);
                        mysession.createLoginSession(user);
                         myDb.close();
                        Intent categories = new Intent(SignUp.this,Categories.class);
                        startActivity(categories);

                }
            }
        });



    }
}
