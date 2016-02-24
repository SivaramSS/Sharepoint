package com.siva.sharepoint_handhelds.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.siva.sharepoint_handhelds.JsonParser;
import com.siva.sharepoint_handhelds.Post;
import com.siva.sharepoint_handhelds.User;
import com.siva.sharepoint_handhelds.listeners.ProfileListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sivaram-pt862 on 18/02/16.
 */
public class GetProfile extends AsyncTask<String,String,String> {
    int success = 0;
    String userid;
    ProgressDialog pd;
    ProfileListener listener;
    User user;
    List<Post> posts = new ArrayList<Post>();
    Context context;

    public GetProfile(Context c, String userid, ProfileListener pl)
    {
        this.userid = userid;
        context = c;
        listener = pl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setMessage("Fetching profile...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected String doInBackground(String... params) {
        List<NameValuePair> lp = new ArrayList<>();
        lp.add(new BasicNameValuePair("userid",userid));
        String jsonString = new JsonParser().getJSONString("http://172.22.97.2:8080/NewsGroup/mProfile",lp);
        if(jsonString!=null) {
            try {
                success = 1;
                Log.d("Json String of Profile : ", jsonString);
                JSONObject job = new JSONObject(jsonString);
                JSONObject userjb = job.getJSONObject("user");
                JSONArray ja = job.getJSONArray("articlelist");

                user = new User();
                user.setUserid(userjb.getString("userid"));
                user.setFname(userjb.getString("fname"));
                user.setEmail(userjb.getString("email"));
                user.setLname(userjb.getString("lname"));
                user.setPostsCount(userjb.getString("postsCount"));
                user.setUserid(userjb.getString("userid"));
                user.setDob(userjb.getString("dob"));

                for (int c = 0; c < ja.length(); c++) {
                    JSONObject jb = ja.getJSONObject(c);
                    Post p = new Post();
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
                    content = content.substring(0, 600);
                    p.setContent(content);

                    if (p.getCount_likes() > 0) {
                        if (p.getCount_likes() == 1 && p.isLiked() == 1)
                            p.setCountlikestext("you like this");
                        else
                            p.setCountlikestext(p.getCount_likes() + " like this");
                    } else
                        p.setCountlikestext("");
                    posts.add(p);
                }
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
            listener.onProfileRetrievedListener(user,posts);
        else
            listener.onError("Error in Retrieving Profile");
    }
}
