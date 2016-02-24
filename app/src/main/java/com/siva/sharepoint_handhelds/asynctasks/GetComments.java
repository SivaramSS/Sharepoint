package com.siva.sharepoint_handhelds.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.siva.sharepoint_handhelds.Comment;
import com.siva.sharepoint_handhelds.JsonParser;
import com.siva.sharepoint_handhelds.listeners.OnCommentsReceivedListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetComments extends AsyncTask<String,String,String> {

    private ProgressDialog pd;
    Context context;
    OnCommentsReceivedListener listener;
    List<Comment> list = new ArrayList<>();
    String aid;
    int success = 0;

    public GetComments(String aid, Context c, OnCommentsReceivedListener ocrl) {
        this.aid = aid;
        this.context = c;
        this.listener = ocrl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setMessage("Getting comments...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected String doInBackground(String... params) {

        List<NameValuePair> lp = new ArrayList<NameValuePair>();
        lp.add(new BasicNameValuePair("aid", aid));
        String urlstring = "http://172.22.97.2:8080/NewsGroup/mGetComments";
        String jsonString = new JsonParser().getJSONString(urlstring, lp);
        if (jsonString != null) {
            try {
                success = 1;
                JSONArray ja = new JSONArray(jsonString);
                for (int d = 0; d < ja.length(); d++) {
                    JSONObject jb = ja.getJSONObject(d);
                    Comment c = new Comment();
                    c.setCid(jb.getString("cid"));
                    c.setCdate(jb.getString("cdate"));
                    c.setContent(jb.getString("content"));
                    c.setFname(jb.getString("fname"));
                    list.add(c);
                }
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        } else
            Log.d("Problem in getting comments", "Yup");
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        pd.dismiss();
        if (success == 1) {
            listener.OnCommentsReceived(list);
        }
        else
            Log.d("GetComments : ", "Success : "+success);
    }
};