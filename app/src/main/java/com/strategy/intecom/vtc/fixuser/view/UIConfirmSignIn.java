package com.strategy.intecom.vtc.fixuser.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.service.RegistrationService;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;


/**
 * Created by Mr. Ha on 5/16/16.
 */
public class UIConfirmSignIn extends AppCore implements View.OnClickListener {

    private View viewRoot;

    private Button btn_login;
    private Button btn_signin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.ui_confirm_signin, container, false);

        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intentService = new Intent(getActivity(), RegistrationService.class);

        if (!Utils.checkPlayServices(getActivity())) {
            getActivity().stopService(intentService);
        }

        getActivity().startService(intentService);

        initControl(view);
    }

    private void initControl(View view) {

        btn_login = (Button) view.findViewById(R.id.btn_login);
        btn_signin = (Button) view.findViewById(R.id.btn_signin);

        btn_login.setOnClickListener(this);
        btn_signin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_login:
                AppCore.CallFragmentSection(Const.UI_MY_LOGIN, true, false);
                break;
            case R.id.btn_signin:
                AppCore.CallFragmentSection(Const.UI_MY_REGISTER, true, false);
                break;
            default:
                return;
        }
    }

}
