package com.example.mom.mom.Presenter;

import android.content.Context;

import com.example.mom.mom.Exception.AlreadyRegisteredException;
import com.example.mom.mom.LoginActivity;
import com.example.mom.mom.Model.AuthenticationInterface;
import com.example.mom.mom.Model.FirebaseUserService;
import com.example.mom.mom.Model.User;
import com.example.mom.mom.Exception.PasswordMismatchException;
import com.example.mom.mom.View.ClickListener;
import com.example.mom.mom.View.Navigator;
import com.example.mom.mom.View.RegisterView;

/**
 * Created by jesse on 2/29/16.
 */
public class RegisterPresenter implements ClickListener {
    private RegisterView m_oView;
    private AuthenticationInterface m_oModel;
    private Navigator m_oNavigator;
    private Context m_oContext;

    public RegisterPresenter(Context oContext, RegisterView oView, AuthenticationInterface oModel, Navigator oNavigator) {
        m_oContext = oContext;
        m_oView = oView;
        m_oModel = oModel;
        m_oNavigator = oNavigator;
    }

    public void onClick() {
        String szUsername = m_oView.getUsername();
        String szPassword = m_oView.getPassword();
        String szPasswordConfirm = m_oView.getPasswordConfirmation();

        //Todo: Check password match
        FirebaseUserService.createUser(m_oContext, szUsername, szPassword);
        User oUser = new User(szUsername, szPassword);
        FirebaseUserService.addUser(m_oContext, oUser);
    }

    /**
     * Forgot password
     */
    public void onForgotClick() {
        FirebaseUserService.forgotPassword(m_oContext, m_oView.getUsername());
    }
}
