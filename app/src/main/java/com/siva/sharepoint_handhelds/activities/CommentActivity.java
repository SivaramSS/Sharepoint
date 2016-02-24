package com.siva.sharepoint_handhelds.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.siva.sharepoint_handhelds.Comment;
import com.siva.sharepoint_handhelds.R;
import com.siva.sharepoint_handhelds.adapters.CommentAdapter;
import com.siva.sharepoint_handhelds.asynctasks.GetComments;
import com.siva.sharepoint_handhelds.asynctasks.SendComment;
import com.siva.sharepoint_handhelds.listeners.OnCommentsReceivedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sivaram-pt862 on 17/02/16.
 */
public class CommentActivity extends AppCompatActivity implements OnCommentsReceivedListener {

    String aid, userid,fname;
    SharedPreferences spf;
    CommentAdapter ca;
    List<Comment> commentlist = new ArrayList<Comment>();
    EditText etcomment;
    ListView lv;
    boolean inserted = false;

    public void comment(View v)
    {
        DateFormat df = new SimpleDateFormat("yyy/MM/dd HH:mm:ss");
        Date now = new Date();
        Comment c = new Comment();
        c.setFname(fname);
        c.setContent(etcomment.getText().toString());
        c.setCdate(df.format(now).toString());
        commentlist.add(c);
        ca.notifyDataSetChanged();

        new SendComment(aid,userid,etcomment.getText().toString(), CommentActivity.this).execute();
        inserted = true;
        etcomment.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        aid = getIntent().getStringExtra("aid");
        spf = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        userid = spf.getString("userid",null);
        if(userid!=null) {
            etcomment = (EditText) findViewById(R.id.etcomment);
            lv = (ListView) findViewById(R.id.lvcomments);
            fname = spf.getString("fname",null);
            ca = new CommentAdapter(getApplicationContext(),commentlist);
            lv.setAdapter(ca);
            new GetComments(aid,CommentActivity.this,this).execute();
        }
    }

    @Override
    public void OnCommentsError(String error) {
        Log.d("Error ", "CommentActivity");
    }

    @Override
    public void OnCommentsReceived(List<Comment> list) {
        commentlist.addAll(list);
        Log.d("OnCommentsReceived", list.size()+"");
        ca.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Log.d("Back Pressed", "Yup");
        Bundle b = new Bundle();
        b.putString("aid", aid);
        b.putInt("count", commentlist.size());
        Intent result = new Intent();
        result.putExtras(b);
        setResult(1, result);
        finish();
    }
}
