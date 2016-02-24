package com.siva.sharepoint_handhelds.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.siva.sharepoint_handhelds.JsonParser;
import com.siva.sharepoint_handhelds.User;
import com.siva.sharepoint_handhelds.listeners.OnLoginCheck;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sivaram-pt862 on 17/02/16.
 */
public class CheckLogin extends AsyncTask<String,String,String> {
    String email, password;
    OnLoginCheck listener;
    int success = 0;
    User user;

    public CheckLogin(String e,String p, OnLoginCheck olc)
    {
        this.email = e;
        this.password = p;
        this.listener = olc;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        List<NameValuePair> lp = new ArrayList<NameValuePair>();
        lp.add(new BasicNameValuePair("email", email));
        lp.add(new BasicNameValuePair("password",password));
        String urlstring = "http://172.22.97.2:8080/NewsGroup/mLogin";
        String jsonString = new JsonParser().getJSONString(urlstring,lp);
        Log.d("json Login", jsonString + "");

        if(jsonString!=null) {
            try {
                success = 1;
                JSONObject jb = new JSONObject(jsonString);
                user = new User();
                user.setUserid(jb.getString("userid"));
                user.setFname(jb.getString("fname"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            Log.d("Login Problem", "Yup");
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(success==1)
            listener.onLoginSuccess(user);
        else
            listener.onLoginFailed("Login Failure");
    }
}
