package com.siva.sharepoint_handhelds.listeners;

import com.siva.sharepoint_handhelds.Post;

/**
 * Created by sivaram-pt862 on 19/02/16.
 */
public interface ArticleReceivedListener {

    public void onArticleReceived(Post p);
    public void onArticleError(String error);
}
