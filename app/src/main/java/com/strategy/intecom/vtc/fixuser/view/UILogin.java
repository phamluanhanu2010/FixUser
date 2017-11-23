package com.strategy.intecom.vtc.fixuser.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
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
import com.strategy.intecom.vtc.fixuser.view.fragment.FMMainJobMap;


/**
 * Created by Mr. Ha on 5/23/16.
 */

public class UILogin extends AppCore implements View.OnClickListener{

    private View viewRoot;
    private EditText edt_phone_num;
    private EditText edt_password;
    private Button btn_submit;
    private ImageView btn_back;
//    private TextView tv_description;

    private TextView tv_forgot_password;
    private ImageView btn_show_password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.ui_login, container, false);

        AppCore.initToolsBar(getActivity(), true);
        return viewRoot;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControler(view);
    }


    private void initControler(View view){

        tv_forgot_password = (TextView) view.findViewById(R.id.tv_forgot_password);
//        tv_description = (TextView) view.findViewById(R.id.tv_description);
        edt_phone_num = (EditText) view.findViewById(R.id.edt_phone_num);
        edt_password = (EditText) view.findViewById(R.id.edt_password);
//        view.findViewById(R.id.btn_submit).setVisibility(View.GONE);

        btn_submit = (Button) view.findViewById(R.id.btn_Accept);
        btn_back = (ImageView) view.findViewById(R.id.btn_back);

        btn_show_password = (ImageView) view.findViewById(R.id.btn_show_password);
        btn_show_password.setOnClickListener(this);

        tv_forgot_password.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_back.setOnClickListener(this);

//        initRuleAndPolicyLink();

        Utils.showKeyboard(getActivity());
    }

    public String getEdt_phone_num() {
        return "+84" + (getTxtPhone().startsWith("0") ? getTxtPhone().substring(1, getTxtPhone().length()) : getTxtPhone());
    }

    public String getTxtPhone() {
        if (edt_phone_num == null) {
            return "";
        }
        return edt_phone_num.getText().toString().trim();
    }

    public String getEdt_password() {
        if(edt_password == null){
            return "";
        }
        return edt_password.getText().toString().trim();
    }

    private int validateData(){
        if (TextUtils.isEmpty(getTxtPhone())) {
            return R.string.login_phone_num_null;
        } else if(!Utils.validatePhoneNumber(getTxtPhone())){
            return R.string.validate_phone_num;
        }else if (TextUtils.isEmpty(getEdt_password())) {
            return R.string.validate_password_null;
        } else if(getEdt_password().length() > 32 || getEdt_password().length() < 6){
            return R.string.login_password_not_correct;
        }

        return 0;
    }

    @Override
    public void onClick(View v) {
        Utils.hideKeyboard(getActivity());
        switch (v.getId()){
            case R.id.btn_Accept:

                int message = validateData();

                if(message == 0) {
                    initGetLogin(UILogin.this, getEdt_phone_num(), getEdt_password());
                }else {
                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getActivity().getResources().getString(message));
                }

                break;

            case R.id.btn_back:
                cmdBack();
                break;

            case R.id.btn_show_password:
                doShowPassword();
                break;

            case R.id.tv_forgot_password:

                AppCore.CallFragmentSection(Const.UI_MY_FORGOT_PASSWORD, true, false);
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
            btn_show_password.setImageResource(R.mipmap.ic_action_action_visibility_off);
        } else {
            // change to show password by set flag to TYPE_TEXT_VARIATION_PASSWORD
            edt_password.setInputType(hidePass);
            btn_show_password.setImageResource(R.mipmap.ic_action_action_visibility);
        }
    }

//    private void initRuleAndPolicyLink() {
//        SpannableString ss = new SpannableString(getString(R.string.confirm_signin_login_1));
//        ClickableSpan rule = new ClickableSpan() {
//            @Override
//            public void onClick(View view) {
//
//                Bundle bundle = new Bundle();
//                bundle.putString(Const.KEY_BUNDLE_ACTION_TITLE, getResources().getString(R.string.nav_Support_Using_Policy));
//                bundle.putString(Const.KEY_BUNDLE_ACTION_URL, Const.URL_POLICY);
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
//
//        ClickableSpan policy = new ClickableSpan() {
//            @Override
//            public void onClick(View view) {
//
//                Bundle bundle = new Bundle();
//                bundle.putString(Const.KEY_BUNDLE_ACTION_TITLE, getResources().getString(R.string.nav_Support_Secret_Policy));
//                bundle.putString(Const.KEY_BUNDLE_ACTION_URL, Const.URL_SECURITY);
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
//
//        ss.setSpan(rule, 56, 74, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(policy, 78, 96, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        tv_description.setText(ss);
//        tv_description.setMovementMethod(LinkMovementMethod.getInstance());
//    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        switch (keyType){
            case TYPE_ACTION_LOGIN:

                setVtcModelUser(VtcModelUser.getDataJson(response));

                AppCore.getPreferenceUtil(getActivity()).setValueString(PreferenceUtil.AUTH_TOKEN, ParserJson.getAuthToken(response));

                AppCore.getPreferenceUtil(getActivity()).setValueString(PreferenceUtil.USER_INFO, response);

                MainScreen.updateProfile(getActivity(), getVtcModelUser());

                getCurrentActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                Bundle b = new Bundle();
                b.putBoolean(Const.KEY_BUNDLE_ACTION_ON_BOOT, true);
                AppCore.CallFragmentSection(Const.UI_MAIN_JOB_MAP_SIMPLE, b, false, true);
                break;
            case TYPE_ACTION_GET_LIST_NEARBY:
                FMMainJobMap.initViewNearBy(response);
                break;
        }
    }

    @Override
    public void onPostExecuteError(TypeActionConnection keyTypeAction, TypeErrorConnection keyType, String msg) {
        super.onPostExecuteError(keyTypeAction, keyType, msg);
        switch (keyType){
            case TYPE_CONNECTION_ERROR_CODE_VERIFY_CODE:

                initGetPassCode(UILogin.this, getEdt_phone_num(), Const.TYPE_PASSCODE_LOGIN);

                Bundle bundle = new Bundle();

                bundle.putString(Const.KEY_BUNDLE_ACTION_PHONE_NUM, getEdt_phone_num());
                bundle.putString(Const.KEY_BUNDLE_ACTION_TYPE_PASSCODE, Const.TYPE_PASSCODE_LOGIN);

                AppCore.CallFragmentSection(Const.UI_MY_CONFIRM_CODE, bundle, true, false);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppCore.initToolsBar(getActivity(), false);
        Utils.hideKeyboard(getCurrentActivity());
    }
}
