package com.siva.sharepoint_handhelds.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.siva.sharepoint_handhelds.R;
import com.siva.sharepoint_handhelds.fragments.Feed;
import com.siva.sharepoint_handhelds.fragments.Profile;

public class HomeActivity extends AppCompatActivity {
    SharedPreferences spf;
    FragmentManager fm;
    Feed feed;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fm = getSupportFragmentManager();
        spf = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean loggedin = spf.getBoolean("loggedin",false);

        if(!loggedin)
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        else
        {
            String userid = spf.getString("userid",null);
            feed = new Feed();
            profile = new Profile(userid, getApplicationContext());
            fm.beginTransaction().replace(R.id.contentframe,feed).addToBackStack("feed").commit();
        }
    }

    public void buttonClick(View v)
    {
        switch(v.getId())
        {
            case R.id.feed :
                feed.resetScrollPosition();
                fm.beginTransaction().replace(R.id.contentframe,feed).addToBackStack("feed").commit();
                break;

            case R.id.share:
                showAlert();
                break;

            case R.id.profile :
                if(profile.isCreated())
                    profile.resetScrollPosition();
                getSupportActionBar().setTitle("Profile");
                fm.beginTransaction().replace(R.id.contentframe,profile).addToBackStack("profile").commit();
                break;
            case R.id.logout:
                SharedPreferences.Editor edit =spf.edit();
                edit.putBoolean("loggedin",false);
                edit.putString("userid", "null");
                edit.commit();
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                break;
        }
    }

    void displayMsg(String msg)
    {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    void showAlert()
    {
        final Dialog dialog = new Dialog(HomeActivity.this,R.style.Dialog);
        View v = getLayoutInflater().inflate(R.layout.dialog_share,null);
        dialog.setContentView(v);
        final EditText etshareurl = (EditText) v.findViewById(R.id.etshareurl);
        Button sharebtn =   (Button) v.findViewById(R.id.sharebtn);

        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(Patterns.WEB_URL.matcher(etshareurl.getText().toString()).matches())
                    feed.share(etshareurl.getText().toString());
                else
                    Toast.makeText(HomeActivity.this,"Not a Valid Url",Toast.LENGTH_SHORT).show();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window w = dialog.getWindow();
        lp.copyFrom(w.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        w.setAttributes(lp);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ResultCode from Home : ", resultCode + "");
        if(resultCode==1) {
            feed.onActivityResult(requestCode, resultCode, data);
            if (profile.isCreated())
                profile.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.homemenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.refresh) {
            feed.fetchData();
        }
        return true;
    }

}
