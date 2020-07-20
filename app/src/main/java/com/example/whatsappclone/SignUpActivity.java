package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmailSignUpActivity, edtUsernameSignUpActivity, edtPasswordSignUpActivity, edtPasswprdVerified;
    private Button btnSignUpActivity, btnLoginSignUpActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("SignUp: What's App");
        setTitleColor(Color.RED);
        edtEmailSignUpActivity = findViewById(R.id.edtEmailSignUpActivity);
        edtUsernameSignUpActivity = findViewById(R.id.edtUsernameSignUpActivity);
        edtPasswordSignUpActivity = findViewById(R.id.edtPassordSignUpActivity);
        edtPasswprdVerified = findViewById(R.id.edtPasswordVerified);
        edtPasswprdVerified.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == keyEvent.KEYCODE_ENTER){
                    onClick(btnSignUpActivity);
                }
                return false;
            }
        });

        btnLoginSignUpActivity = findViewById(R.id.btnLogInSignUpActivity);
        btnSignUpActivity = findViewById(R.id.btnSignUpActivity);

        btnLoginSignUpActivity.setOnClickListener(this);
        btnSignUpActivity.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null){

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
            Intent resume = new Intent(SignUpActivity.this, newClass);
            startActivity(resume);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        FancyToast.makeText(SignUpActivity.this,activityName + "",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
    }

    @Override
    public void onClick(View view) {
        rootLayoutTapped(view);
        switch (view.getId()){
            case R.id.btnSignUpActivity:
                if (edtUsernameSignUpActivity.getText().toString().equals("")
                        || edtEmailSignUpActivity.getText().toString().equals("")
                        || edtPasswordSignUpActivity.getText().toString().equals("")
                        || edtPasswprdVerified.getText().toString().equals("")) {

                    FancyToast.makeText(this,"Please fill required fields: email,username,Password",FancyToast.LENGTH_SHORT,FancyToast.WARNING,true).show();
                } else {
                    String pwd2 = edtPasswprdVerified.getText().toString();
                    String pwd1 = edtPasswordSignUpActivity.getText().toString();
                    if (!pwd1.equals(pwd2)) {
                        FancyToast.makeText(this,"Password not match , Please Enter Match Password",FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
                        finish();
                    } else {
                        final ParseUser parseUser = new ParseUser();
                        parseUser.setEmail(edtEmailSignUpActivity.getText().toString());
                        parseUser.setUsername(edtUsernameSignUpActivity.getText().toString());
                        parseUser.setPassword(edtPasswordSignUpActivity.getText().toString());
                        parseUser.put("is_Staff",true);
                        parseUser.put("is_Moderator", false);
                        parseUser.saveInBackground();

                        final ProgressDialog progressDialog = new ProgressDialog(this);
                        progressDialog.setTitle("Signing Up ...");
                        progressDialog.setMessage("Please Wait while registering finished.");
                        progressDialog.show();

                        parseUser.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    FancyToast.makeText(SignUpActivity.this,parseUser.getUsername() + " is Signing Up Successfully.",
                                            FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                                    finish();
                                    transactionToMainActivity();
                                } else {
                                    FancyToast.makeText(SignUpActivity.this, "There was an error: " + e.getMessage(),
                                            FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
                    }

                }
                break;
            case R.id.btnLogInSignUpActivity:
                Intent intent = new Intent(SignUpActivity.this,LogInActivity.class);
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