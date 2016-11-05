package com.example.mom.mom.Model;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.mom.mom.AdminHomeActivity;
import com.example.mom.mom.HomeActivity;
import com.example.mom.mom.LoginActivity;
import com.example.mom.mom.View.Navigator;
import com.example.mom.mom.View.UserView;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;
import java.util.Map;

/**
 * Created by Jesse on 4/1/2016.
 */
public class FirebaseUserService {

    public static void getUser (final Context oContext, final User oUser, final String szUsername) {
        Firebase.setAndroidContext(oContext);
        Firebase ref = new Firebase("https://boiling-heat-5021.firebaseio.com/Users");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User oDataUser = dataSnapshot.child(szUsername == "" ? "admindummy" : szUsername).getValue(User.class);
                oDataUser.clone(oUser);

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void setUserView(final Context oContext, final UserView view, final String szUsername) {
        Firebase.setAndroidContext(oContext);
        Firebase ref = new Firebase("https://boiling-heat-5021.firebaseio.com/Users");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User oUser = dataSnapshot.child(szUsername == "" ? "admindummy" : szUsername).getValue(User.class);
                view.setUsername(oUser.getUsername());
                view.setBio(oUser.getBio());
                view.setStatuses(oUser.getStatuses());


                view.setUnlockEnabled(oUser.getStatuses().contains(User.Status.LOCKED));
                view.setBannedEnabled(!oUser.getStatuses().contains(User.Status.BANNED));

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void getLoginUser(final Context oContext, final User oUser, final String szUsername) {
        Firebase.setAndroidContext(oContext);
        Firebase ref = new Firebase("https://boiling-heat-5021.firebaseio.com/Users");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User oDataUser = dataSnapshot.child(szUsername == "" ? "admindummy" : oUser.getUsername()).getValue(User.class);
                oDataUser.clone(oUser);

                Navigator nav = new NavigatorModel(oContext);
                if (oDataUser.getStatuses().contains(User.Status.ADMIN)) {
                    nav.startNewTopActivity(AdminHomeActivity.class);
                } else {
                    nav.startNewTopActivity(HomeActivity.class);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void addUser(Context oContext, final User oUser) {
        Firebase.setAndroidContext(oContext);
        Firebase ref = new Firebase("https://boiling-heat-5021.firebaseio.com/Users");

        ref.child(oUser.getUsername() == "" ? "admindummy" : oUser.getUsername()).setValue(oUser);
    }

    public static void createUser(final Context oContext, final String szUsername, final String szPassword) {
        Firebase.setAndroidContext(oContext);
        Firebase ref = new Firebase("https://boiling-heat-5021.firebaseio.com");
        ref.createUser(szUsername + "@gatech.edu", szPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                Toast.makeText(oContext, "User Created", Toast.LENGTH_SHORT).show();
                Navigator nav = new NavigatorModel(oContext);
                nav.startNewTopActivity(LoginActivity.class);
            }

            @Override
            public void onError(FirebaseError firebaseError) {

            }
        });
    }

    public static void loginUser(final Context oContext, final String szUsername, final String szPassword) {
        Firebase.setAndroidContext(oContext);
        Firebase ref = new Firebase("https://boiling-heat-5021.firebaseio.com");
        ref.authWithPassword(szUsername + "@gatech.edu", szPassword, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                User oCurrent = new User(szUsername, szPassword);
                Session.setUser(oCurrent);
                FirebaseUserService.getLoginUser(oContext, oCurrent, szUsername);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                //Todo: Lock
                Toast.makeText(oContext, "Failed to log in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void forgotPassword(final Context oContext, final String szUsername) {
        Firebase.setAndroidContext(oContext);
        Firebase ref = new Firebase("https://boiling-heat-5021.firebaseio.com");

        ref.resetPassword(szUsername + "@gatech.edu", new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                Toast.makeText(oContext, "Reset email sent to " + szUsername + "@gatech.edu.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Toast.makeText(oContext, firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void getAllUsers(final Context oContext, final List<User> aUsers, final ArrayAdapter<User> adapter) {
        Firebase.setAndroidContext(oContext);
        Firebase ref = new Firebase("https://boiling-heat-5021.firebaseio.com/Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    User oUser = user.getValue(User.class);
                    aUsers.add(oUser);
                    adapter.add(oUser);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
}
