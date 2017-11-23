package com.strategy.intecom.vtc.fixuser.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.MainScreen;
import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixuser.model.VtcModelUser;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.ParserJson;
import com.strategy.intecom.vtc.fixuser.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;
import com.strategy.intecom.vtc.fixuser.view.base.AppCoreBase;


/**
 * Created by Mr. Ha on 5/23/16.
 */

public class UIRegister extends AppCore implements View.OnClickListener {

    private View viewRoot;
    private EditText edt_full_name;
    private EditText edt_email;
    private EditText edt_phone_num;
    private EditText edt_password;
    private Button btn_submit;
    private ImageView btn_back;
    private TextView tv_phone_state;
    private ImageView img_showPassword;
    private TextView tv_description;
    private String policyURL = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.ui_register, container, false);

        policyURL = AppCore.getPreferenceUtil(getCurrentActivity()).getValueString(ParserJson.API_PARAMETER_POLICY);
        AppCore.initToolsBar(getActivity(), true);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initControler(view);
    }

    private void initControler(View view) {

        edt_full_name = (EditText) view.findViewById(R.id.edt_full_name);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_phone_num = (EditText) view.findViewById(R.id.edt_phone_num);
        edt_password = (EditText) view.findViewById(R.id.edt_password);
        tv_phone_state = (TextView) view.findViewById(R.id.tv_phone_state);

        btn_submit = (Button) view.findViewById(R.id.btn_Accept);
        btn_back = (ImageView) view.findViewById(R.id.btn_back);

        btn_submit.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        img_showPassword = (ImageView) view.findViewById(R.id.btn_show_password);
        img_showPassword.setOnClickListener(this);

        tv_description = (TextView) view.findViewById(R.id.tv_description);
        initRuleAndPolicyLink();

        Utils.showKeyboard(getActivity());
    }

    private void initRuleAndPolicyLink() {
        SpannableString ss = new SpannableString(getString(R.string.confirm_signin_login_1));
//        ClickableSpan rule = new ClickableSpan() {
//            @Override
//            public void onClick(View view) {
//
//                Bundle bundle = new Bundle();
//                bundle.putString(Const.KEY_BUNDLE_ACTION_TITLE, getResources().getString(R.string.nav_Support_Using_Policy));
//                bundle.putString(Const.KEY_BUNDLE_ACTION_URL, policyURL);
//
//                AppCore.CallFragmentSection(Const.UI_WEB_VIEW, bundle, true, false);
//            }
//
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                ds.setColor(Color.GREEN);
//                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//            }
//        };

        ClickableSpan policy = new ClickableSpan() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString(Const.KEY_BUNDLE_ACTION_TITLE, getResources().getString(R.string.nav_Support_Secret_Policy));
                bundle.putString(Const.KEY_BUNDLE_ACTION_URL, policyURL);

                AppCore.CallFragmentSection(Const.UI_WEB_VIEW, bundle, true, false);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.GREEN);
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            }
        };

        ss.setSpan(policy, 56, 96, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_description.setText(ss);
        tv_description.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public String getEdt_full_name() {
        if (edt_full_name == null) {
            return "";
        }
        return edt_full_name.getText().toString().trim();
    }

    public String getEdt_email() {
        if (edt_email == null) {
            return "";
        }
        return edt_email.getText().toString().trim();
    }


    public String getEdt_phone_num() {
        return getTv_phone_state() + (getTxtPhone().startsWith("0") ? getTxtPhone().substring(1, getTxtPhone().length()) : getTxtPhone());
    }

    public String getTxtPhone() {
        if (edt_phone_num == null) {
            return "";
        }
        return edt_phone_num.getText().toString().trim();
    }

    public String getEdt_password() {
        if (edt_password == null) {
            return "";
        }
        return edt_password.getText().toString().trim();
    }

    public String getTv_phone_state(){

        if (tv_phone_state == null) {
            return "";
        }
        return tv_phone_state.getText().toString().trim();
    }

    private int validateData() {
        if (getEdt_full_name().equals("")) {
            return R.string.validate_full_name_null;
        } else if (!Utils.validateName(getEdt_full_name())) {
            return R.string.validate_full_name;
        } else if (!getEdt_email().isEmpty() && !Utils.validateEmail(getEdt_email())) {
            return R.string.validate_email;
        } else if (TextUtils.isEmpty(getTxtPhone())) {
            return R.string.validate_phone_num_null;
        } else if (!Utils.validatePhoneNumber(getTxtPhone())) {
            return R.string.validate_phone_num;
        }else if (TextUtils.isEmpty(getEdt_password())) {
            return R.string.validate_password_null;
        } else if (getEdt_password().length() < 6 || getEdt_password().length() > 32) {
            return R.string.validate_password;
        }

        return 0;
    }

    @Override
    public void onClick(View v) {
        Utils.hideKeyboard(getActivity());
        switch (v.getId()) {
            case R.id.btn_Accept:

                int message = validateData();

                if (message == 0) {
                    initGetRegister(UIRegister.this, getEdt_full_name(), getEdt_phone_num(), getEdt_password(), getEdt_email());
                } else {
                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getActivity().getResources().getString(message));
                }

                break;

            case R.id.btn_back:
                cmdBack();
                break;

            case R.id.btn_show_password:
                doShowPassword();
                break;
            default:
                return;
        }
    }

    private void doShowPassword() {
        int inputType = edt_password.getInputType();
        // A password field with the password hide to the user
        // inputType = TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD
        // So we need to use the operator & to get the correct flag
        int hidePass = InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT;

        if (inputType == hidePass) {
            // change to show password by set flag to TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            img_showPassword.setImageResource(R.mipmap.ic_action_action_visibility_off);
        } else {
            // change to show password by set flag to TYPE_TEXT_VARIATION_PASSWORD
            edt_password.setInputType(hidePass);
            img_showPassword.setImageResource(R.mipmap.ic_action_action_visibility);
        }
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        if (keyType == TypeActionConnection.TYPE_ACTION_REGISTER) {

            AppCore.getPreferenceUtil(getActivity()).setValueString(PreferenceUtil.AUTH_TOKEN, ParserJson.getAuthToken(response));

            AppCore.getPreferenceUtil(getActivity()).setValueString(PreferenceUtil.USER_INFO, response);

            setVtcModelUser(VtcModelUser.getDataJson(response));

            MainScreen.updateProfile(getActivity(), getVtcModelUser());

            Bundle bundle = new Bundle();

            bundle.putString(Const.KEY_BUNDLE_ACTION_PHONE_NUM, getEdt_phone_num());
            bundle.putString(Const.KEY_BUNDLE_ACTION_TYPE_PASSCODE, Const.TYPE_PASSCODE_REGISTER);

            AppCore.CallFragmentSection(Const.UI_MY_CONFIRM_CODE, bundle, true, false);
        }
    }

    @Override
    public void onPostExecuteError(TypeActionConnection keyTypeAction, TypeErrorConnection keyType, String msg) {
        super.onPostExecuteError(keyTypeAction, keyType, msg);

        if (keyTypeAction == TypeActionConnection.TYPE_ACTION_REGISTER) {

            if(keyType == TypeErrorConnection.TYPE_CONNECTION_ERROR_CODE_VERIFY_CODE){

                Bundle bundle = new Bundle();

                bundle.putString(Const.KEY_BUNDLE_ACTION_PHONE_NUM, getEdt_phone_num());
                bundle.putString(Const.KEY_BUNDLE_ACTION_TYPE_PASSCODE, Const.TYPE_PASSCODE_REGISTER);

                AppCore.CallFragmentSection(Const.UI_MY_CONFIRM_CODE, bundle, true, false);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppCore.initToolsBar(getActivity(), false);
        Utils.hideKeyboard(getCurrentActivity());
    }
}