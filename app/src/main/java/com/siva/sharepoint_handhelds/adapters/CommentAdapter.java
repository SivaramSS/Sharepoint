package com.siva.sharepoint_handhelds.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.siva.sharepoint_handhelds.Comment;
import com.siva.sharepoint_handhelds.R;

import java.util.List;

/**
 * Created by sivaram-pt862 on 16/02/16.
 */
public class CommentAdapter extends ArrayAdapter<Comment> {

    Context context;
    LayoutInflater li;
    List<Comment> commentlist;

    public CommentAdapter(Context c,List<Comment> list)
    {
        super(c, R.layout.eachcomment, list);
        context = c;
        commentlist = list;
        li = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        CommentViewHolder cvh;
        final Comment currentcomment = commentlist.get(position);
        if(v==null) {
            v = li.inflate(R.layout.eachcomment,parent,false);
            cvh = new CommentViewHolder();
            cvh.fname = (TextView) v.findViewById(R.id.fname);
            cvh.content = (TextView) v.findViewById(R.id.content);
            cvh.cdate = (TextView) v.findViewById(R.id.cdate);
            v.setTag(cvh);
        }
        else
            cvh = (CommentViewHolder) v.getTag();
        cvh.fname.setText(currentcomment.getFname());
        cvh.content.setText(currentcomment.getContent());
        cvh.cdate.setText(currentcomment.getCdate());
        return v;
    }
}

class CommentViewHolder {
    TextView fname,content, cdate;
}