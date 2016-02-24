package com.siva.sharepoint_handhelds.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.siva.sharepoint_handhelds.Post;
import com.siva.sharepoint_handhelds.R;
import com.siva.sharepoint_handhelds.asynctasks.GetCompleteArticle;
import com.siva.sharepoint_handhelds.asynctasks.LikeOrUnlike;
import com.siva.sharepoint_handhelds.listeners.ArticleReceivedListener;

/**
 * Created by sivaram-pt862 on 18/02/16.
 */
public class FullPostActivity extends AppCompatActivity implements ArticleReceivedListener{

    TextView fname,title,content, sharedon, readmore, countlikes;
    Button like, comment;
    SharedPreferences spf;
    String aid,userid;
    boolean flag = false;
    Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullpost);
        fname = (TextView) findViewById(R.id.fname);
        title =  (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        sharedon = (TextView) findViewById(R.id.sharedon);
        readmore = (TextView) findViewById(R.id.readmore);
        countlikes = (TextView) findViewById(R.id.countlikes);
        like = (Button) findViewById(R.id.like);
        comment = (Button) findViewById(R.id.comment);
        spf = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        aid = getIntent().getStringExtra("aid");
        userid = spf.getString("userid",null);
        new GetCompleteArticle(FullPostActivity.this,aid,userid,this).execute();
    }

    @Override
    public void onArticleError(String error) {
        Log.d("Error ",error);
    }

    @Override
    public void onArticleReceived(final Post p) {

        fname.setText(p.getFname());
        title.setText(p.getTitle());
        content.setText(p.getContent());
        sharedon.setText("Shared on " + p.getUldatetime() + "");
        readmore.setText("Go to Article Page");

        readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = p.getUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        if(p.isLiked()==1)
            like.setText("Unlike");
        else
            like.setText("Like");

        int temp = p.getCount_comments();

        if(temp==0)
            comment.setText("Comment");
        else if(temp==1)
            comment.setText(temp + " Comment");
        else
            comment.setText(temp + " Comments");

        countlikes.setText(p.getCountlikestext());

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CommentActivity.class);
                i.putExtra("aid", p.getAid());
                startActivityForResult(i, 5);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button like = (Button) v;
                String text = like.getText().toString();

                View section = (View) v.getParent().getParent();
                TextView countlikes = (TextView) section.findViewById(R.id.countlikes);

                if(text.equalsIgnoreCase("Like")) {
                    like.setText("Unlike");
                    p.setLiked(1);
                    int count = p.getCount_likes()+1;
                    p.setCount_likes(count);
                    Log.d("Count : ",count+"");
                    if(count==1 && p.isLiked()==1) {
                        countlikes.setText("you like this");
                        p.setCountlikestext("you like this");
                    }
                    else {
                        countlikes.setText(count + " like this");
                        p.setCountlikestext(count+" like this");
                    }
                }
                else {
                    like.setText("Like");
                    p.setLiked(0);
                    int count = p.getCount_likes()-1;
                    Log.d("Count",count+"");
                    p.setCount_likes(count);
                    if(count==0) {
                        p.setCountlikestext("");
                        countlikes.setText(p.getCountlikestext());
                    }
                    else {
                        countlikes.setText(count + " like this");
                        p.setCountlikestext(count+" like this");
                    }
                }

                new LikeOrUnlike(p.getAid().toString(),userid,FullPostActivity.this).execute();
            }
        });

        fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FullPostActivity.this, ProfileActivity.class);
                i.putExtra("userid",p.getUserid());
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== 5 && resultCode==1) {
            Bundle b = data.getExtras();
            String aid = b.getString("aid");
            int count = b.getInt("count");
            if(count==1)
                comment.setText(count + " Comment");
            else if(count>1)
                comment.setText(count + " Comments");
        }
        this.data = data;
        flag = true;
    }

    @Override
    public void onBackPressed() {
        if(flag == true)
            setResult(1,data);
        else
            setResult(0,data);
        finish();
    }
}
