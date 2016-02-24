package com.siva.sharepoint_handhelds.asynctasks;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sivaram-pt862 on 18/02/16.
 */
public class LikeOrUnlike {
    String aid,userid;
    Context context;

    public LikeOrUnlike(String aid,String userid,Context c)
    {
        this.aid = aid;
        this.userid = userid;
        this.context = c;
    }

    public void execute() {
        RequestParams rp = new RequestParams();
        rp.add("aid",aid);
        rp.add("userid", userid);
        String urlstring = "http://172.22.97.2:8080/NewsGroup/mLike";
        new AsyncHttpClient().post(context, urlstring, rp, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String result = new String(bytes, "UTF-8");
                    Log.d("LikeOrUnlike :",result);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    String result = new String(bytes, "UTF-8");
                    Log.d("LikeOrUnlike :",result);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
