package com.siva.sharepoint_handhelds.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.siva.sharepoint_handhelds.JsonParser;
import com.siva.sharepoint_handhelds.Post;
import com.siva.sharepoint_handhelds.listeners.ArticleReceivedListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sivaram-pt862 on 24/02/16.
 */
public class GetCompleteArticle extends AsyncTask<String,String,String> {
    int success=0;
    Post p;
    ArticleReceivedListener listener;
    Context context;
    ProgressDialog pd;
    String aid,userid;

    public GetCompleteArticle(Context c, String aid, String userid, ArticleReceivedListener arl)
    {
        this.aid = aid;
        this.userid = userid;
        this.listener = arl;
        this.context = c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setMessage("Fetching content...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected String doInBackground(String... params) {

        List<NameValuePair> lp = new ArrayList<NameValuePair>();
        lp.add(new BasicNameValuePair("aid",aid));
        String urlstring = "http://172.22.97.2:8080/NewsGroup/mFullPost";
        try {
            String jsonstring = new JsonParser().getJSONString(urlstring, lp);

            if(jsonstring!=null) {
                success=1;
                JSONObject jb = new JSONObject(jsonstring);
                p = new Post();
                p.setAid(jb.getString("aid"));
                p.setUserid(jb.getString("userid"));
                p.setUrl(jb.getString("url"));
                p.setCount_likes(jb.getInt("count_likes"));
                p.setCount_comments(jb.getInt("count_comments"));
                p.setFname(jb.getString("fname"));
                p.setLname(jb.getString("lname"));
                p.setUldatetime(jb.getString("uldatetime"));
                p.setLiked(jb.getInt("liked"));
                p.setTitle(jb.getString("title"));
                String content = jb.getString("content");
                p.setContent(content);

                if(p.getCount_likes()>0) {
                    if (p.getCount_likes() == 1 && p.isLiked() == 1)
                        p.setCountlikestext("you like this");
                    else
                        p.setCountlikestext(p.getCount_likes() + " like this");
                }
                else
                    p.setCountlikestext("");
            }
            Log.d("JsonString", jsonstring);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pd.dismiss();
        if(success==1)
            listener.onArticleReceived(p);
        else
            listener.onArticleError("Problem in getting post");
    }
}
