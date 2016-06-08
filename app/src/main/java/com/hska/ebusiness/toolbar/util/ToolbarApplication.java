package com.hska.ebusiness.toolbar.util;

import android.app.Application;

import com.hska.ebusiness.toolbar.model.User;

public class ToolbarApplication extends Application {

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

}


