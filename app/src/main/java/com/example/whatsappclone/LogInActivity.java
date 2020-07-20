package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtEmailLogInActivity,edtPasswordLogInActivity;
    Button btnLogInLogInActivity, btnSignUpLogInActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setTitle("LogIn : What's App");
        setTitleColor(Color.YELLOW);

        edtEmailLogInActivity = findViewById(R.id.edtEmailLogInActivity);
        edtPasswordLogInActivity = findViewById(R.id.edtPassordLogInActivity);
        edtPasswordLogInActivity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == keyEvent.KEYCODE_ENTER){
                    onClick(btnLogInLogInActivity);
                }
                return false;
            }
        });


        btnLogInLogInActivity = findViewById(R.id.btnLogInLogInActivity);
        btnSignUpLogInActivity = findViewById(R.id.btnSignUpLogInActivity);

        btnLogInLogInActivity.setOnClickListener(this);
        btnSignUpLogInActivity.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null){
            transactionToMainActivity();
            finish();
        }


    }

    private void transactionToMainActivity() {
        String activityName = "";
        String resumeName = "";
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser.get("is_Moderator").toString().equals("true")){
            activityName = "ModeratorsActivity";
            resumeName = ModeratorsActivity.class.getCanonicalName();
        } else {
            resumeName = WhatsAppUsers.class.getCanonicalName();
            activityName = "WhatsAppUsers";
        }
        try {
            Class newClass = Class.forName(resumeName);
            Intent resume = new Intent(LogInActivity.this, newClass);
            startActivity(resume);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        FancyToast.makeText(LogInActivity.this,activityName + "",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
    }

    @Override
    public void onClick(View view) {
        rootLayoutTapped(view);
        switch (view.getId()){
            case R.id.btnLogInLogInActivity:
                if (edtEmailLogInActivity.getText().toString().equals("") ||
                        edtPasswordLogInActivity.getText().toString().equals("")) {
                    FancyToast.makeText(LogInActivity.this, "Email, Password is required!" ,
                            FancyToast.LENGTH_SHORT, FancyToast.INFO,true).show();
                } else {
                    ParseUser.logInInBackground(edtEmailLogInActivity.getText().toString(), edtPasswordLogInActivity.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(user != null && e == null) {
                                FancyToast.makeText(LogInActivity.this, user.getUsername() + " is Logged In successfully",
                                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                finish();
                                transactionToMainActivity();
                            } else {
                                FancyToast.makeText(LogInActivity.this, "There was an error: " + e.getMessage(),
                                        FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }
                        }
                    });
                }
                break;
            case R.id.btnSignUpLogInActivity:
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }



    // hide KeyBoard :

    public void rootLayoutTapped(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}