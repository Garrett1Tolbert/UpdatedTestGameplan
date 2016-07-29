package com.example.garrett.updatedtestgameplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterActivity extends Activity {

    Button SignupButton;
    DBHelper helper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SignupButton = (Button) findViewById(R.id.SignUpButton);
    }

    public void onSignUpClick(View v) {
        EditText username = (EditText) findViewById(R.id.etUserName);
        EditText email = (EditText) findViewById(R.id.etEmail);
        EditText password = (EditText) findViewById(R.id.etPassword);
        EditText confirmpassword = (EditText) findViewById(R.id.etConfirmPassword);

        String STRusername = username.getText().toString();
        String STRemail = email.getText().toString();
        String STRpassword = password.getText().toString();
        String STRconfirmpassword = confirmpassword.getText().toString();

        if (!STRpassword.equals(STRconfirmpassword)) {
            //pop-up message
            Toast pass = Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT);
            pass.show();

        }else{
            //insert information into database
            User u = new User();
            u.setUsername(STRusername);
            u.setEmail(STRemail);
            u.setPassword(STRpassword);

            Logger.getLogger(getClass().getName()).log(Level.WARNING, "THIS IS A TEST1");

            helper.insertUser(u);

            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Run Successful1");


            //go to LoginActivity
            Intent PressIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            RegisterActivity.this.startActivity(PressIntent);
        }
    }
}

