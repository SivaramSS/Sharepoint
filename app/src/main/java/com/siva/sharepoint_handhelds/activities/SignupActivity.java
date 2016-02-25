package com.siva.sharepoint_handhelds.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.siva.sharepoint_handhelds.R;
import com.siva.sharepoint_handhelds.User;
import com.siva.sharepoint_handhelds.asynctasks.CreateAccount;
import com.siva.sharepoint_handhelds.fragments.DatePickerFragment;
import com.siva.sharepoint_handhelds.listeners.AccountListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sivaram-pt862 on 25/02/16.
 */
public class SignupActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AccountListener{
    TextView labelfname,labellname,labelemail,labelpass,labelcp,labeldob;
    EditText etfname,etlname,etemail,etpassword,etcp,etdob;
    String dobstring;
    SharedPreferences spf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        spf = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        labelfname = (TextView) findViewById(R.id.labelfname);
        labellname = (TextView) findViewById(R.id.labellname);
        labelemail = (TextView) findViewById(R.id.labelemail);
        labelpass = (TextView) findViewById(R.id.labelpass);
        labelcp = (TextView) findViewById(R.id.labelcp);
        labeldob = (TextView) findViewById(R.id.labeldob);
        etfname = (EditText) findViewById(R.id.etfname);
        etlname = (EditText) findViewById(R.id.etlname);
        etemail = (EditText) findViewById(R.id.etemail);
        etpassword = (EditText) findViewById(R.id.etpassword);
        etcp = (EditText) findViewById(R.id.etcp);
        etdob = (EditText) findViewById(R.id.etdob);
        setWatchers();
    }

    public void setWatchers() {

        etfname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                labelfname.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                labelfname.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etfname.getText().toString().equalsIgnoreCase(""))
                    labelfname.setVisibility(View.VISIBLE);
                else
                    labelfname.setVisibility(View.INVISIBLE);
            }
        });

        etlname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                labellname.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                labellname.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etlname.getText().toString().equalsIgnoreCase(""))
                    labellname.setVisibility(View.VISIBLE);
                else
                    labellname.setVisibility(View.INVISIBLE);
            }
        });

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

        etcp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                labelcp.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                labelcp.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etcp.getText().toString().equalsIgnoreCase(""))
                    labelcp.setVisibility(View.VISIBLE);
                else
                    labelcp.setVisibility(View.INVISIBLE);
            }
        });

        etdob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                labeldob.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                labeldob.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etdob.getText().toString().equalsIgnoreCase(""))
                    labeldob.setVisibility(View.VISIBLE);
                else
                    labeldob.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.d("Date month year : ", dayOfMonth + " " + monthOfYear + " " + year);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
            dobstring = sdf.format(d);
            etdob.setText(dobstring);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setEtDobListener(View v) {
        Log.d("Called","et listener");
        DialogFragment df = new DatePickerFragment(SignupActivity.this);
        df.show(getSupportFragmentManager(), "datePicker");
    }

    public void showMsg(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }

    public void create(View v) {
        boolean flag = true;
        String fname = etfname.getText().toString();
        String lname = etlname.getText().toString();
        String email = etemail.getText().toString();
        String password = etpassword.getText().toString();
        String cp = etcp.getText().toString();

        if(fname.trim().isEmpty()) {
            showMsg("First name cannot be empty");
            flag = false;
        }

        if(lname.trim().isEmpty()) {
            showMsg("Last Name cannot be empty");
            flag = false;
        }

        if(password.trim().isEmpty()) {
            showMsg("Password cannot be empty");
            flag = false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showMsg("Invalid email format");
            flag = false;
        }

        if(!password.equalsIgnoreCase(cp)) {
            showMsg("Passwords do not match");
            flag = false;
        }

        if(flag==true) {
            User user = new User();
            user.setEmail(email);
            user.setFname(fname);
            user.setLname(lname);
            user.setPassword(password);
            user.setCp(cp);
            user.setDob(dobstring);

            new CreateAccount(SignupActivity.this,user,this).execute();
        }
    }

    @Override
    public void onAccountCreated(User user) {
        if(user.getUserid().equalsIgnoreCase("null"))
            showMsg("Something went wrong in creating account");
        else {
            Log.d("Signup Success", user.getFname());
            SharedPreferences.Editor edit = spf.edit();
            edit.putBoolean("loggedin", true);
            edit.putString("userid", user.getUserid() + "");
            edit.putString("fname", user.getFname());
            edit.commit();
            startActivity(new Intent(SignupActivity.this, HomeActivity.class));
            SignupActivity.this.finish();
            LoginActivity.login.finish();
        }
    }

    @Override
    public void onError(String error) {
        Log.d("Error in Creating account ",error);
    }
}
