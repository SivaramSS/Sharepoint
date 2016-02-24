package com.siva.sharepoint_handhelds.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by sivaram-pt862 on 15/02/16.
 */
public class Profile extends Fragment implements ProfileListener{
    View v;
    String userid;
    Context context;
    TextView name,dob,email,post_count;
    ListView lvprofile;
    List<Post> posts = new ArrayList<Post>();
    boolean created = false;
    PostAdapter pa;

    public Profile(String userid, Context c)
    {
        this.userid = userid;
        context = c;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        created = true;
        if(v==null) {
            v = inflater.inflate(R.layout.fragment_profile, container,false);
            lvprofile = (ListView) v.findViewById(R.id.lvprofile);
            View header = inflater.inflate(R.layout.header,null);
            name = (TextView) header.findViewById(R.id.name);
            dob = (TextView) header.findViewById(R.id.dob);
            email = (TextView) header.findViewById(R.id.email);
            post_count = (TextView) header.findViewById(R.id.post_count);
            lvprofile.addHeaderView(header);
            pa = new PostAdapter(getActivity(),posts);
            lvprofile.setAdapter(pa);
            new GetProfile(getActivity(),userid,Profile.this).execute();
        }

        return v;
    }

    @Override
    public void onError(String error) {
        Log.d("Error in Profile ",error);
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
        pa.onActivityResult(requestCode,resultCode,data);
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public void resetScrollPosition() {
        lvprofile.setSelection(0);
    }
}
