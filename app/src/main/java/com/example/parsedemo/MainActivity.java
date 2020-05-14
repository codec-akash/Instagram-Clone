package com.example.parsedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener , View.OnKeyListener{

    public void showUserList(){
        Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            loginClicked(v);
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logInTextView){
           if (signUpModeActive){
               signUpModeActive = false;
               signUpButton.setText("Log-in");
               logInTextView.setText("New User ? Sign-Up");
           } else {
               signUpModeActive = true;
               signUpButton.setText("Sign-Up");
               logInTextView.setText("Have an Acc , Log-In");
           }
        } else if (v.getId() == R.id.logoImageView || v.getId() == R.id.backgroundLayout){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    EditText usernameEditText;
    EditText passwordEditText;
    TextView logInTextView;
    Button signUpButton;
    Boolean signUpModeActive = true;
    ImageView logoImageView;
    ConstraintLayout constraintLayout;


    public void loginClicked(View view){
        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
            Toast.makeText(this, "Username and Password are required", Toast.LENGTH_SHORT).show();
        } else {
            if (signUpModeActive) {
                ParseUser user = new ParseUser();
                user.put("username", usernameEditText.getText().toString());
                user.put("password", passwordEditText.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Signed up", "Success");
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                //LogIN
                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null){
                            Log.i("Logged in","Successful");
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = (EditText) findViewById(R.id.userNameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        logInTextView = (TextView) findViewById(R.id.logInTextView);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        logoImageView = (ImageView) findViewById(R.id.logoImageView);
        constraintLayout = (ConstraintLayout) findViewById(R.id.backgroundLayout);

        logoImageView.setOnClickListener(this);
        constraintLayout.setOnClickListener(this);
        logInTextView.setOnClickListener(this);
        passwordEditText.setOnKeyListener(this);

        if (ParseUser.getCurrentUser() != null){
            Toast.makeText(this, "Passed", Toast.LENGTH_SHORT).show();
            showUserList();
        }



        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

}
