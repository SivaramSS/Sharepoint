package com.siva.sharepoint_handhelds.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.siva.sharepoint_handhelds.activities.CommentActivity;
import com.siva.sharepoint_handhelds.Post;
import com.siva.sharepoint_handhelds.R;
import com.siva.sharepoint_handhelds.activities.FullPostActivity;
import com.siva.sharepoint_handhelds.activities.ProfileActivity;
import com.siva.sharepoint_handhelds.asynctasks.LikeOrUnlike;

import java.util.List;

/**
 * Created by sivaram-pt862 on 16/02/16.
 */

public class PostAdapter extends ArrayAdapter<Post>
{
    Context feed_context;
    List<Post> posts;
    LayoutInflater li;
    ViewHolder vh;
    SharedPreferences spf;
    String userid;

    public PostAdapter(Context c,List<Post> list)
    {
        super(c, R.layout.eachpost, list);
        posts = list;
        feed_context = c;
        spf = c.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        userid = spf.getString("userid",null);
        li = LayoutInflater.from(feed_context);
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        final Post current = posts.get(position);

        if(v==null) {
            v = li.inflate(R.layout.eachpost, parent, false);
            vh = new ViewHolder();
            vh.fname = (TextView) v.findViewById(R.id.fname);
            vh.content = (TextView) v.findViewById(R.id.content);
            vh.sharedon = (TextView) v.findViewById(R.id.sharedon);
            vh.title = (TextView) v.findViewById(R.id.title);
            vh.readmore = (TextView) v.findViewById(R.id.readmore);
            vh.like = (Button) v.findViewById(R.id.like);
            vh.comment = (Button) v.findViewById(R.id.comment);
            vh.countlikes = (TextView) v.findViewById(R.id.countlikes);
            v.setTag(vh);
        }
        else
            vh = (ViewHolder) v.getTag();

        vh.fname.setText(current.getFname());
        vh.title.setText(current.getTitle());
        vh.content.setText(current.getContent().substring(0,400));
        vh.sharedon.setText("Shared on " + current.getUldatetime() + "");

        vh.readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = current.getUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                feed_context.startActivity(i);
            }
        });

        if(current.isLiked()==1)
            vh.like.setText("Unlike");
        else
            vh.like.setText("Like");

        int temp = current.getCount_comments();

        if(temp==0)
            vh.comment.setText("Comment");
        else if(temp==1)
            vh.comment.setText(temp + " Comment");
        else
            vh.comment.setText(temp + " Comments");

        vh.countlikes.setText(current.getCountlikestext());

        vh.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(feed_context.getApplicationContext(), CommentActivity.class);
                i.putExtra("aid", current.getAid());
                ((AppCompatActivity) feed_context).startActivityForResult(i, 5);
            }
        });

        vh.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button like = (Button) v;
                String text = like.getText().toString();

                View section = (View) v.getParent().getParent();
                TextView countlikes = (TextView) section.findViewById(R.id.countlikes);

                if(text.equalsIgnoreCase("Like")) {
                    like.setText("Unlike");
                    current.setLiked(1);
                    int count = current.getCount_likes()+1;
                    current.setCount_likes(count);
                    Log.d("Count : ",count+"");
                    if(count==1 && current.isLiked()==1) {
                        countlikes.setText("you like this");
                        current.setCountlikestext("you like this");
                    }
                    else {
                        countlikes.setText(count + " like this");
                        current.setCountlikestext(count+" like this");
                    }
                }
                else {
                    like.setText("Like");
                    current.setLiked(0);
                    int count = current.getCount_likes()-1;
                    Log.d("Count",count+"");
                    current.setCount_likes(count);
                    if(count==0) {
                        current.setCountlikestext("");
                        countlikes.setText(current.getCountlikestext());
                    }
                    else {
                        countlikes.setText(count + " like this");
                        current.setCountlikestext(count+" like this");
                    }
                }

                new LikeOrUnlike(current.getAid().toString(),userid,feed_context).execute();
            }
        });

        vh.fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(feed_context, ProfileActivity.class);
                i.putExtra("userid",current.getUserid());
                ((AppCompatActivity) feed_context).startActivityForResult(i,5);
            }
        });

        vh.sharedon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(feed_context, FullPostActivity.class);
                i.putExtra("aid",current.getAid());
                ((AppCompatActivity)feed_context).startActivityForResult(i, 5);
            }
        });
        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("In activity result of Post Adapter", "Yup ResultCode : "+resultCode+" RequestCode : "+requestCode);

        if(requestCode== 5 && resultCode==1) {
            Bundle b = data.getExtras();
            String aid = b.getString("aid");
            int count = b.getInt("count");
            Log.d("Aid",aid+" Count : "+count);
            if(!aid.isEmpty() && aid!=null) {
                for(int c=0;c<posts.size();c++) {
                    if(posts.get(c).getAid().equals(aid)) {
                        posts.get(c).setCount_comments(count);
                        Log.d("Found"," Post : "+c+" Count : "+count);
                        break;
                    }
                }
                notifyDataSetChanged();
            }
        }
    }

    class ViewHolder
    {
        TextView fname,title,content, sharedon, readmore, countlikes;
        Button like, comment;
    }
}