package com.strategy.intecom.vtc.fixuser.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
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
import com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixuser.model.VtcModelUser;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.ParserJson;
import com.strategy.intecom.vtc.fixuser.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMMainJobMap;


/**
 * Created by Mr. Ha on 5/16/16.
 */
public class UIConfirmPassCode extends AppCore implements View.OnClickListener {

    private View viewRoot;

    private EditText edt_passcode;
    private Button btn_submit;
    private ImageView btn_back;
    private TextView tv_description_number;
    private Button btn_passcode;

    private String phoneNum = "";
    private String typePasscode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            phoneNum = savedInstanceState.getString(Const.KEY_BUNDLE_ACTION_PHONE_NUM);
            typePasscode = savedInstanceState.getString(Const.KEY_BUNDLE_ACTION_TYPE_PASSCODE);
        }

        viewRoot = inflater.inflate(R.layout.ui_confirm_pascode, container, false);
        AppCore.initToolsBar(getActivity(), true);

        return viewRoot;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControler(view);
    }


    private void initControler(View view) {

        edt_passcode = (EditText) view.findViewById(R.id.edt_passcode);

        tv_description_number = (TextView) view.findViewById(R.id.tv_description_number);
        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        btn_passcode = (Button) view.findViewById(R.id.btn_passcode);

        tv_description_number.setText(phoneNum);
        initEvents();
    }

    private void initEvents() {
        btn_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_passcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_back:
                cmdBack();
                break;
            case R.id.btn_submit:
                int message = validatePasscode();
                if (message == 0) {
                    initConfirmPassCode(UIConfirmPassCode.this, phoneNum, getEdt_passcode());
                } else {
                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getActivity().getResources().getString(message));
                }
                break;
            case R.id.btn_passcode:
                initGetPassCode(UIConfirmPassCode.this, phoneNum, typePasscode);
                break;
            default:
                return;
        }
    }

    public String getEdt_passcode() {
        if (edt_passcode == null) {
            return "";
        }
        return edt_passcode.getText().toString().trim();
    }

    private int validatePasscode() {
        if (TextUtils.isEmpty(getEdt_passcode())) {
            return R.string.validate_passcode_null;
        } else if (getEdt_passcode().length() > 6) {
            return R.string.validate_passcode;
        } else if (getEdt_passcode().length() < 6) {
            return R.string.validate_passcode;
        }
        return 0;
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        switch (keyType) {
            case TYPE_ACTION_CONFIRM_PASSCODE:

                setVtcModelUser(VtcModelUser.getDataJson(response));

                AppCore.getPreferenceUtil(getActivity()).setValueString(PreferenceUtil.AUTH_TOKEN, ParserJson.getAuthToken(response));

                AppCore.getPreferenceUtil(getActivity()).setValueString(PreferenceUtil.USER_INFO, response);

                MainScreen.updateProfile(getActivity(), getVtcModelUser());

                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", message);

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
    public void onDestroyView() {
        super.onDestroyView();
        Utils.hideKeyboard(getCurrentActivity());
    }
}

