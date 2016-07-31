package com.example.garrett.updatedtestgameplan;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    TextView title;
    Button LoginButton, RegisterButton;
    EditText etUsername, etPassword;
    DBHelper helper = new DBHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton = (Button) findViewById(R.id.LogInButton);
        RegisterButton = (Button) findViewById(R.id.Registerbutton);
        title = (TextView) findViewById(R.id.title);
        Typeface myTypeFace = Typeface.createFromAsset(getAssets(),"fonts/Lobster_1.3.otf");
        title.setTypeface(myTypeFace);

    }

    public void onButtonClick(View v) {
        EditText a = (EditText)findViewById(R.id.etUsername);
        String aString = a.getText().toString();
        EditText b = (EditText)findViewById(R.id.etPassword);
        String pass = b.getText().toString();


        User signin = helper.searchPass(aString);
        String password = signin.getPassword();


        if (pass.equals(password)) {
            Intent PressIntent = new Intent(LoginActivity.this,MainActivity.class);
            PressIntent.putExtra("userId",signin.getId());
            LoginActivity.this.startActivity(PressIntent);
        }else{
            Toast passTwo = Toast.makeText(LoginActivity.this, "Username and/or password are incorrect. Try again.", Toast.LENGTH_SHORT);
            passTwo.show();

        }
    }
    public void onRegisterClick(View v) {
        RegisterButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent PressIntentTwo = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(PressIntentTwo);
            }
        });
    }}
