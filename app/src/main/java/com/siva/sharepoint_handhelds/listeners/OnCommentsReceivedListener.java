package com.siva.sharepoint_handhelds.listeners;

import com.siva.sharepoint_handhelds.Comment;

import java.util.List;

/**
 * Created by sivaram-pt862 on 16/02/16.
 */
public interface OnCommentsReceivedListener {
    public void OnCommentsReceived(List<Comment> list);

    public void OnCommentsError(String error);
}
