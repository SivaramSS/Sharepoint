package com.siva.sharepoint_handhelds.listeners;

import com.siva.sharepoint_handhelds.Post;
import com.siva.sharepoint_handhelds.User;

import java.util.List;

/**
 * Created by sivaram-pt862 on 23/02/16.
 */
public interface ProfileListener {
    public void onProfileRetrievedListener(User user,List<Post> posts);
    public void onError(String error);
}
