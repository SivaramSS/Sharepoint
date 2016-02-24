package com.siva.sharepoint_handhelds.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.siva.sharepoint_handhelds.JsonParser;
import com.siva.sharepoint_handhelds.Post;
import com.siva.sharepoint_handhelds.R;
import com.siva.sharepoint_handhelds.adapters.PostAdapter;
import com.siva.sharepoint_handhelds.asynctasks.ShareUrl;
import com.siva.sharepoint_handhelds.listeners.ArticleReceivedListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sivaram-pt862 on 12/02/16.
 */
public class Feed extends Fragment implements ArticleReceivedListener{

    ListView lvposts;
    List<Post> posts = new ArrayList<Post>();
    ProgressDialog pd;
    String userid;
    SharedPreferences spf;
    PostAdapter pa;
    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(v==null) {
            v = inflater.inflate(R.layout.fragment_feed, container,false);
            spf = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
            userid = spf.getString("userid", null);
            Log.d("userid from prefs ", userid);
            lvposts = (ListView) v.findViewById(R.id.lvposts);
            pa = new PostAdapter(getActivity(), posts);
            lvposts.setAdapter(pa);
            Log.d("OnCreateView ", " called");
            fetchData();
        }

        return v;
    }

    public void share(String urlstring)
    {
        Log.d("Url : ",urlstring);
        new ShareUrl(getActivity(),urlstring,userid,this).execute();
    }

    public void fetchData()
    {
        pd = new ProgressDialog(getActivity());
        new AsyncTask<String,String,String>() {
            int success = 0;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd.setMessage("Fetching Articles...");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected String doInBackground(String... params) {

                String urlstring = "http://172.22.97.2:8080/NewsGroup/mFeed";
                List<NameValuePair> lp = new ArrayList<NameValuePair>();
                lp.add(new BasicNameValuePair("userid", userid));

                String jsonString = new JsonParser().getJSONString(urlstring,lp);
                try {
                    if(jsonString!=null) {
                        JSONArray ja = new JSONArray(jsonString);
                        Log.d("Results ", ja.getJSONObject(0).getString("content"));
                        success = 1;
                        for(int c = 0 ; c<ja.length(); c++) {

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

                            if(p.getCount_likes()>0) {
                                if (p.getCount_likes() == 1 && p.isLiked() == 1)
                                    p.setCountlikestext("you like this");
                                else
                                    p.setCountlikestext(p.getCount_likes() + " like this");
                            }
                            else
                                p.setCountlikestext("");
                            posts.add(p);
                        }

                    }
                    else
                        Log.d("Problem with Json ", "YUP");
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
                {
                    pa.notifyDataSetChanged();
                }
            }

        }.execute();
    }

    @Override
    public void onArticleError(String error) {
        Log.d("Article Error",error);
    }

    @Override
    public void onArticleReceived(Post p) {
        posts.add(0,p);
        pa.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        super.onSaveInstanceState(b);
        int index = lvposts.getFirstVisiblePosition();
        View v = lvposts.getChildAt(0);
        int top = (v==null)? 0 : v.getTop();
        b.putInt("index",index);
        b.putInt("top",top);
    }

    @Override
    public void onActivityCreated(Bundle b){
        super.onActivityCreated(b);
        int index=-1,top=0;
        if(b!=null) {
            index = b.getInt("index",-1);
            top = b.getInt("top",0);
        }

        if(index!=-1)
            lvposts.setSelectionFromTop(index,top);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        pa.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void resetScrollPosition() {
        lvposts.setSelection(0);
    }
}
