package com.siva.sharepoint_handhelds.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.siva.sharepoint_handhelds.JsonParser;
import com.siva.sharepoint_handhelds.User;
import com.siva.sharepoint_handhelds.listeners.AccountListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sivaram-pt862 on 25/02/16.
 */
public class CreateAccount extends AsyncTask<String,String,String> {
    int success=0;
    User user;
    Context context;
    AccountListener listener;
    ProgressDialog pd;

    public CreateAccount(Context c, User user, AccountListener al){
        this.context = c;
        this.user = user;
        listener = al;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setMessage("Creating your account...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected String doInBackground(String... params) {
        List<NameValuePair> lp = new ArrayList<>();
        lp.add(new BasicNameValuePair("fname",user.getFname()));
        lp.add(new BasicNameValuePair("lname",user.getLname()));
        lp.add(new BasicNameValuePair("email",user.getEmail()));
        lp.add(new BasicNameValuePair("password",user.getPassword()));
        lp.add(new BasicNameValuePair("dob",user.getDob()));
        String urlstring = "http://172.22.97.2:8080/NewsGroup/mSignup";
        String jsonString = new JsonParser().getJSONString(urlstring,lp);
        if(jsonString!=null) {
            try {
                success = 1;
                JSONObject jb = new JSONObject(jsonString);
                user.setUserid(jb.getString("userid"));
                user.setEmail(jb.getString("email"));
                user.setFname(jb.getString("fname"));
                user.setLname(jb.getString("lname"));
                user.setDob(jb.getString("dob"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pd.dismiss();
        if(success==1)
            listener.onAccountCreated(user);
        else
            listener.onError("Error");

    }
}
