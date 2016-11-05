package com.example.mom.mom.Presenter;

import android.content.Context;

import com.example.mom.mom.Model.FirebaseUserService;
import com.example.mom.mom.Model.User;
import com.example.mom.mom.View.Navigator;
import com.example.mom.mom.View.OnCreate;
import com.example.mom.mom.View.UserView;

/**
 * Created by Jesse on 3/14/2016.
 */
public class UserPresenter implements OnCreate {

    private UserView m_oView;
    private Navigator m_oNavigator;
    private Context m_oContext;
    private User m_oUser;

    public UserPresenter(UserView oView, Navigator oNavigator, Context oContext) {
        m_oView = oView;
        m_oNavigator = oNavigator;
        m_oContext = oContext;
        m_oUser = new User();
    }

    @Override
    public void onCreate() {
        FirebaseUserService.setUserView(m_oContext, m_oView, m_oView.getUsername());
    }

    /**
     * Occurs on ban click
     */
    public void onBanClick() {
        User oUser = m_oUser;
        oUser.removeStatus(User.Status.ACTIVE);
        oUser.addStatuses(User.Status.BANNED);
        FirebaseUserService.addUser(m_oContext, oUser);
    }

    /**
     * Occurs on unlock click
     */
    public void onUnlockClick() {
        User oUser = m_oUser;
        oUser.removeStatus(User.Status.LOCKED);
        oUser.addStatuses(User.Status.ACTIVE);
        FirebaseUserService.addUser(m_oContext, oUser);
    }
}
