package com.siva.sharepoint_handhelds.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.siva.sharepoint_handhelds.R;
import com.siva.sharepoint_handhelds.User;
import com.siva.sharepoint_handhelds.asynctasks.CheckLogin;
import com.siva.sharepoint_handhelds.listeners.OnLoginCheck;

/**
 * Created by sivaram-pt862 on 12/02/16.
 */
public class LoginActivity extends AppCompatActivity implements OnLoginCheck{
    EditText etemail, etpassword;
    TextView labelemail, labelpass;
    public static AppCompatActivity login;
    SharedPreferences spf;

    @Override
    protected void onCreate(Bundle b)
    {
        super.onCreate(b);
        setContentView(R.layout.activity_login);
        login = this;
        spf = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        Button loginbtn = (Button) findViewById(R.id.loginbtn);
        etemail = (EditText) findViewById(R.id.etemail);
        etpassword = (EditText) findViewById(R.id.etpassword);
        labelemail = (TextView) findViewById(R.id.labelemail);
        labelpass = (TextView) findViewById(R.id.labelpass);
        setTextWatcher();
        loginbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(etemail.getText().toString()).matches())
                    new CheckLogin(etemail.getText().toString(), etpassword.getText().toString(), LoginActivity.this).execute();
                else
                    Toast.makeText(LoginActivity.this, "Inavalid email format", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLoginFailed(String error) {
        Log.d("Login failed", error);
        Toast.makeText(LoginActivity.this,"Invalid Email/Password",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginSuccess(User user) {
        if(user.getUserid().equalsIgnoreCase("null"))
            Toast.makeText(LoginActivity.this,"Invalid Email/Password",Toast.LENGTH_SHORT).show();
        else {
            Log.d("Login Success", user.getFname());
            SharedPreferences.Editor edit = spf.edit();
            edit.putBoolean("loggedin", true);
            edit.putString("userid", user.getUserid() + "");
            edit.putString("fname", user.getFname());
            edit.commit();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            LoginActivity.this.finish();
        }
    }

    public void setTextWatcher() {
        etemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                labelemail.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                labelemail.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etemail.getText().toString().equalsIgnoreCase(""))
                    labelemail.setVisibility(View.VISIBLE);
                else
                    labelemail.setVisibility(View.INVISIBLE);
            }
        });

        etpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                labelpass.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                labelemail.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etpassword.getText().toString().equalsIgnoreCase(""))
                    labelpass.setVisibility(View.VISIBLE);
                else
                    labelpass.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void signUp(View v) {
        startActivity(new Intent(LoginActivity.this,SignupActivity.class));
    }
}
