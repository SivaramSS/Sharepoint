package com.siva.sharepoint_handhelds.asynctasks;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sivaram-pt862 on 17/02/16.
 */
public class SendComment {
    int success = 0;
    String aid,userid,content;
    Context context;

    public SendComment(String aid,String userid, String content, Context c)
    {
        this.aid = aid;
        this.userid = userid;
        this.content = content;
        this.context = c;
    }

    public void execute()
    {
        RequestParams rp =  new RequestParams();
        rp.add("aid",aid);
        rp.add("userid",userid);
        rp.add("comment",content);
        String urlstring = "http://172.22.97.2:8080/NewsGroup/mInsertComment";

        new AsyncHttpClient().post(context, urlstring, rp, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                success =1;
                try {
                    String result = new String(bytes, "UTF-8");
                    Log.d("SendComment : Response", result);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.d("Send Comment : ", "not sent");
            }
        });
    }
}
