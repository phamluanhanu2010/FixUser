package com.strategy.intecom.vtc.fixuser.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.strategy.intecom.vtc.fixuser.view.base.AppCoreAPI;
import com.strategy.intecom.vtc.fixuser.view.base.AppCoreBase;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad.MediaAsync;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Luan Pham on 5/23/16.
 */
public class UIEditProfile extends AppCore implements View.OnClickListener, TextWatcher {
    private EditText txtName, txtMail;
    private ImageButton btnClearName, btnClearEmail;
    private ImageView img_avatar;

    private View viewRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.ui_edit_profile, container, false);

        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initController(view);
    }


    private void initController(View view) {
        img_avatar = (ImageView) viewRoot.findViewById(R.id.img_avatar);
        Button btnLogout = (Button) view.findViewById(R.id.btn_Edit_Profile_Remove);
        btnLogout.setOnClickListener(this);

        ImageView btnQuit = (ImageView) view.findViewById(R.id.btn_back);
        btnQuit.setOnClickListener(this);

        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(getActivity().getResources().getText(R.string.title_edit_profile));
        tv_title.setSelected(true);

        TextView btn_submit = (TextView) view.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        btn_submit.setVisibility(TextView.VISIBLE);
        btn_submit.setText(R.string.save);

        ImageView img_edit_profile = (ImageView) view.findViewById(R.id.img_edit_profile);
        img_edit_profile.setOnClickListener(this);


        btnClearName = (ImageButton) view.findViewById(R.id.imgbtn_Edit_Profile_Name);
        btnClearName.setOnClickListener(this);

        btnClearEmail = (ImageButton) view.findViewById(R.id.imgbtn_Edit_Profile_Email);
        btnClearEmail.setOnClickListener(this);

        txtName = (EditText) view.findViewById(R.id.etxt_Edit_Profile_Name);
        txtName.setText(AppCore.getVtcModelUser() == null ? "" : AppCore.getVtcModelUser().getName());
        txtName.addTextChangedListener(this);

        String phone = AppCore.getVtcModelUser() == null ? "" : AppCore.getVtcModelUser().getPhone();

        TextView txtPhone = (TextView) view.findViewById(R.id.etxt_Edit_Profile_Phone);
        txtPhone.setText(phone);

        txtMail = (EditText) view.findViewById(R.id.etxt_Edit_Profile_Email);
        txtMail.setText(AppCore.getVtcModelUser() == null ? "" : AppCore.getVtcModelUser().getEmail());
        txtMail.addTextChangedListener(this);

        if (getTxtName().length() > 0) {
            btnClearName.setVisibility(View.VISIBLE);
        } else {
            btnClearName.setVisibility(View.GONE);
        }

        if (getTxtMail().length() > 0) {
            btnClearEmail.setVisibility(View.VISIBLE);
        } else {
            btnClearEmail.setVisibility(View.GONE);
        }

        String avatar = AppCore.getPreferenceUtil(getActivity()).getValueString(PreferenceUtil.USER_INFO_AVATAR);
        updateAvata(img_avatar, avatar);

        Utils.showKeyboard(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Edit_Profile_Remove:
                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_CONFIRM, "", getActivity().getResources().getString(R.string.nav_Edit_Profile_content_Notification));
                break;
            case R.id.btn_back:
                Utils.hideKeyboard(getActivity());
                cmdBack();
                break;
            case R.id.imgbtn_Edit_Profile_Name:
                txtName.getText().clear();
                break;
            case R.id.imgbtn_Edit_Profile_Email:
                txtMail.getText().clear();
                break;
            case R.id.img_edit_profile:

                Utils.hideKeyboard(getActivity());
                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_OPTION_CHOICE_MEDIA);
                break;
            case R.id.btn_submit:
                validateData();
                Utils.hideKeyboard(getActivity());
                break;
        }
    }

    @Override
    protected void cmdOnCapture() {
        super.cmdOnCapture();
        initGetCamera(Const.REQUEST_CODE_CAM_AVATAR, "avatar");
    }

    @Override
    protected void cmdOnLibrary() {
        super.cmdOnLibrary();
        dispatchPictureLibraryIntent(Const.REQUEST_CODE_LOAD_IMAGE_AVATAR);
    }

    private void updateAvata(ImageView imageView, String link) {
        int sizeAvatar = (int) AppCore.getCurrentActivity().getResources().getDimension(R.dimen.confirm_ui_button_option);

        ImageLoadAsync loadAsync = new ImageLoadAsync(AppCore.getCurrentActivity(), imageView, sizeAvatar, true);
        loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, link);
    }


    @Override
    protected void cmdOnSetPicture(Bitmap bitmap, String strPath) {
        super.cmdOnSetPicture(bitmap, strPath);
        if (img_avatar != null) {
//            if (bitmap != null) {
//
//                bitmap = UtilsBitmap.getRoundedCornerImage(bitmap, 100);
//                img_avatar.setImageBitmap(bitmap);
//            } else {
            updateAvata(img_avatar, strPath);
//            }
        }

        AppCoreAPI.initUpLoadAvatar(this, strPath);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (s == txtName.getEditableText()) {
            if (getTxtName().length() > 0) {
                btnClearName.setVisibility(View.VISIBLE);
            } else {
                btnClearName.setVisibility(View.GONE);
            }
        } else if (s == txtMail.getEditableText()) {
            if (Utils.validateEmail(txtMail.getText().toString())) {
                txtMail.setTextColor(Color.BLACK);
            } else {
                txtMail.setTextColor(Color.RED);
            }
            if (getTxtMail().length() > 0) {
                btnClearEmail.setVisibility(View.VISIBLE);
            } else {
                btnClearEmail.setVisibility(View.GONE);
            }
        }
    }

    public String getTxtName() {
        if (txtName == null) {
            return "";
        }
        return txtName.getText().toString().trim();
    }

    public String getTxtMail() {
        if (txtMail == null) {
            return "";
        }
        return txtMail.getText().toString().trim();
    }

    private void validateData() {

        String name = getTxtName();
        String email = getTxtMail();

        int message = 0;

        if (name.isEmpty()) {
            message = R.string.validate_full_name;
        } else if (!email.isEmpty() && !Utils.validateEmail(email)) {
            message = R.string.validate_email;
        } else if (email.isEmpty()) {
            message = R.string.validate_email_null;
        }
        if (message != 0) {
            initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getResources().getString(message));
            return;
        }

        if (name.equals(AppCore.getVtcModelUser().getName())) {
            name = "";
        }
        if (email.equals(AppCore.getVtcModelUser().getEmail())) {
            email = "";
        }

        VtcModelUser user = AppCoreBase.getVtcModelUser();
        if (getTxtMail().equals(user.getEmail()) && getTxtMail().equals(user.getName())) {
            return;
        }

        AppCore.initUpdateProfile(UIEditProfile.this, AppCore.getVtcModelUser() == null ? "" : AppCore.getVtcModelUser().get_id(), name, email);
    }

    @Override
    public void onPostExecuteError(TypeActionConnection keyTypeAction, TypeErrorConnection keyType, String msg) {
        Activity activity = getActivity();
        if (activity == null) {
            activity = getCurrentActivity();
        }
        if (activity == null) {
            return;
        }

        switch (keyTypeAction) {

            case TYPE_ACTION_UPDATE_PROFILE:
                Toast.makeText(activity, activity.getResources().getString(R.string.update_profile_error), Toast.LENGTH_LONG).show();
                break;
            case TYPE_ACTION_UPLOAD_AVATAR:

                Toast.makeText(activity, activity.getResources().getString(R.string.update_profile_avatar_error), Toast.LENGTH_LONG).show();

                break;
        }
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {

        Activity activity = getActivity();
        if (activity == null) {
            activity = getCurrentActivity();
        }

        switch (keyType) {

            case TYPE_ACTION_UPDATE_PROFILE:
                AppCore.setVtcModelUser(VtcModelUser.getDataJson(response));
                if (activity != null) {
                    AppCore.getPreferenceUtil(activity.getApplicationContext()).setValueString(PreferenceUtil.USER_INFO, response);

                    Toast.makeText(activity, activity.getResources().getString(R.string.update_profile_complete), Toast.LENGTH_LONG).show();
                }
                break;
            case TYPE_ACTION_UPLOAD_AVATAR:

                if (activity != null) {
                    String avatar = "";
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        avatar = jsonObject.optString(ParserJson.API_PARAMETER_AVATAR);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (!avatar.isEmpty()) {
                        AppCore.getPreferenceUtil(activity).setValueString(PreferenceUtil.USER_INFO_AVATAR, avatar + getExtraPathAvatar());

                        MainScreen.updateProfile(getActivity(), getVtcModelUser());
                    }

                    Toast.makeText(activity, activity.getResources().getString(R.string.update_profile_complete), Toast.LENGTH_LONG).show();
                }

                break;
            case TYPE_LOGOUT:
                AppCore.initReceived(Const.TYPE_LOGOUT);
                break;
        }
    }

    @Override
    public void cmdPressDialogYes(TypeShowDialog dialogType) {
        super.cmdPressDialogYes(dialogType);
        initLogOut(this);
    }
}
