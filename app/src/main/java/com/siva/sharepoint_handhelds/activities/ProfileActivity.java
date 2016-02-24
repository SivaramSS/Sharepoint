package com.siva.sharepoint_handhelds.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.siva.sharepoint_handhelds.Post;
import com.siva.sharepoint_handhelds.R;
import com.siva.sharepoint_handhelds.User;
import com.siva.sharepoint_handhelds.adapters.PostAdapter;
import com.siva.sharepoint_handhelds.asynctasks.GetProfile;
import com.siva.sharepoint_handhelds.listeners.ProfileListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sivaram-pt862 on 24/02/16.
 */
public class ProfileActivity extends AppCompatActivity implements ProfileListener{
    View v;
    String userid;
    TextView name,dob,email,post_count;
    ListView lvprofile;
    List<Post> posts = new ArrayList<Post>();
    PostAdapter pa;
    Boolean flag = false;
    Intent data;

    @Override
    protected void onCreate(Bundle b) {

        super.onCreate(b);
        setContentView(R.layout.fragment_profile);
        userid = getIntent().getStringExtra("userid");

        lvprofile = (ListView) findViewById(R.id.lvprofile);
        getSupportActionBar().setTitle("Profile");
        View header = getLayoutInflater().inflate(R.layout.header, null);
        name = (TextView) header.findViewById(R.id.name);
        dob = (TextView) header.findViewById(R.id.dob);
        email = (TextView) header.findViewById(R.id.email);
        post_count = (TextView) header.findViewById(R.id.post_count);
        lvprofile.addHeaderView(header);
        pa = new PostAdapter(this,posts);
        lvprofile.setAdapter(pa);
        new GetProfile(this,userid,this).execute();

    }

    @Override
    public void onError(String error) {
        Log.d("Error in Profile ", error);
    }

    @Override
    public void onProfileRetrievedListener(User user,List<Post> postlist) {
        Log.d("User Fname ", user.getFname());
        name.setText(user.getFname() + " " + user.getLname());
        dob.setText(user.getDob());
        email.setText(user.getEmail());
        post_count.setText(user.getPostsCount());
        posts.addAll(postlist);
        pa.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pa.onActivityResult(requestCode, resultCode, data);
        this.data = data;
        flag = true;
    }

    @Override
    public void onBackPressed() {
        if(flag==true)
            setResult(1, data);
        else
            setResult(0,data);
        finish();
    }
}
