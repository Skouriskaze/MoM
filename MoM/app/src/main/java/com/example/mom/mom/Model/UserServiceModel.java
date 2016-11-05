package com.example.mom.mom.Model;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.mom.mom.View.UserView;

import java.util.List;

/**
 * Created by Jesse on 4/26/2016.
 */
public class UserServiceModel {
    public static void getUser (final Context oContext, final User oUser, final String szUsername) {
        FirebaseUserService.getUser(oContext, oUser, szUsername);
    }

    public static void setUserView(final Context oContext, final UserView view, final String szUsername) {
        FirebaseUserService.setUserView(oContext, view, szUsername);
    }

    public static void getLoginUser(final Context oContext, final User oUser, final String szUsername) {
        FirebaseUserService.getLoginUser(oContext, oUser, szUsername);
    }

    public static void addUser(Context oContext, final User oUser) {
        FirebaseUserService.addUser(oContext, oUser);
    }

    public static void createUser(final Context oContext, final String szUsername, final String szPassword) {
        FirebaseUserService.createUser(oContext, szUsername, szPassword);
    }

    public static void loginUser(final Context oContext, final String szUsername, final String szPassword) {
        FirebaseUserService.loginUser(oContext, szUsername, szPassword);
    }

    public static void forgotPassword(final Context oContext, final String szUsername) {
        FirebaseUserService.forgotPassword(oContext, szUsername);
    }

    public static void getAllUsers(final Context oContext, final List<User> aUsers, final ArrayAdapter<User> adapter) {
        FirebaseUserService.getAllUsers(oContext, aUsers, adapter);
    }
}
