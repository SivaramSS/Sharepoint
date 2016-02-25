package com.siva.sharepoint_handhelds.listeners;

import com.siva.sharepoint_handhelds.User;

/**
 * Created by sivaram-pt862 on 25/02/16.
 */
public interface AccountListener {
    public void onAccountCreated(User user);
    public void onError(String error);
}
