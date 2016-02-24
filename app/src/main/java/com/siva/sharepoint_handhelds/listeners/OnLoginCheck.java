package com.siva.sharepoint_handhelds.listeners;

import com.siva.sharepoint_handhelds.User;

/**
 * Created by sivaram-pt862 on 17/02/16.
 */
public interface OnLoginCheck {

    public void onLoginSuccess(User user);
    public void onLoginFailed(String error);
}
