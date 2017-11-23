package com.strategy.intecom.vtc.fixuser;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.config.VtcNWConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixuser.interfaces.RequestListener;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNoti;
import com.strategy.intecom.vtc.fixuser.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixuser.model.VtcModelUser;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.Logger;
import com.strategy.intecom.vtc.fixuser.utils.ParserJson;
import com.strategy.intecom.vtc.fixuser.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.view.UIInvite;
import com.strategy.intecom.vtc.fixuser.view.UISupport;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;
import com.strategy.intecom.vtc.fixuser.view.base.AppCoreAPI;
import com.strategy.intecom.vtc.fixuser.view.base.AppCoreBase;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad.MediaAsync;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.socket.client.Socket;


/**
 * Created by Mr. Ha on 5/16/16.
 */

public class MainScreen extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, AppCoreBase.OnProfileUpdateListenner {

    private static final String TAG = MainScreen.class.getSimpleName();

    private NavigationView navigationView;
    private static ImageView img_avatar;
    private static TextView tv_profile_name;

    private LinearLayout nav_job_history;
    private LinearLayout nav_job_list;
    private LinearLayout nav_message;
    private LinearLayout nav_invite;
    private LinearLayout nav_support;
    private LinearLayout nav_change_password;
    private TextView mUnreadFeedbackCount;
    private ImageView mImgNoFeedback;

    private static DrawerLayout drawer;

    public Bundle bundle = null;

    boolean isStartChangeColor = false;
    boolean isStartUnChangeColor = false;
    boolean isChangeColor = true;

    private OnBackListenner onBackListenner;

    private boolean isRefreshConnection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_main_activity);

        try {
            bundle = savedInstanceState;


            Utils.getHashKey(this);

            AppCore.setCurrentActivity(this);

            initController();

            String infoUser = AppCore.getPreferenceUtil(this).getValueString(PreferenceUtil.USER_INFO);

            if (infoUser.isEmpty()) {
                AppCore.initToolsBar(MainScreen.this, false);
                AppCore.CallFragmentSection(Const.UI_MY_LOGIN_CONFIRM, false, false);
            } else {

                VtcModelUser user = VtcModelUser.getDataJson(infoUser);
                if (user != null) {
                    AppCore.initToolsBar(MainScreen.this, true);

                    AppCore.setVtcModelUser(user);

                    MainScreen.updateProfile(MainScreen.this, AppCore.getVtcModelUser());

                    Bundle b = new Bundle();
                    b.putBoolean(Const.KEY_BUNDLE_ACTION_ON_BOOT, true);
                    AppCore.CallFragmentSection(Const.UI_MAIN_JOB_MAP_SIMPLE, b, false, true);

                } else {
                    AppCore.showLog("Login Parser User Error.....");
                }
            }

            AppCore.setOnProfileUpdateListenner(this);


        } catch (ExceptionInInitializerError error) {
            error.printStackTrace();
        }
    }

    public static void updateProfile(final Activity activity, final VtcModelUser user) {
        if(activity == null){
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(user != null) {
                    tv_profile_name.setText(user.getName());
                }

                int sizeAvatar = (int) AppCore.getCurrentActivity().getResources().getDimension(R.dimen.confirm_ui_button_option);

                String avatar = AppCore.getPreferenceUtil(activity).getValueString(PreferenceUtil.USER_INFO_AVATAR);

                ImageLoadAsync loadAsync = new ImageLoadAsync(AppCore.getCurrentActivity(), img_avatar, sizeAvatar , true);
                loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, avatar);
            }
        });
    }

    public void initController(){

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main_screen);
        img_avatar = (ImageView) headerLayout.findViewById(R.id.img_avatar);
        tv_profile_name = (TextView) headerLayout.findViewById(R.id.tv_profile_name);

        ImageView imgEdit = (ImageView)headerLayout.findViewById(R.id.img_edit_profile);
        imgEdit.setOnClickListener(this);

        View contentLayout = navigationView.inflateHeaderView(R.layout.nav_content_main_screen);
        nav_job_history = (LinearLayout) contentLayout.findViewById(R.id.nav_job_history);
        nav_job_list = (LinearLayout) contentLayout.findViewById(R.id.nav_job_list);
        nav_message = (LinearLayout) contentLayout.findViewById(R.id.nav_message);
        nav_invite = (LinearLayout) contentLayout.findViewById(R.id.nav_invite);
        nav_support = (LinearLayout) contentLayout.findViewById(R.id.nav_support);
        nav_change_password = (LinearLayout) contentLayout.findViewById(R.id.nav_change_password);
        mUnreadFeedbackCount = (TextView) contentLayout.findViewById(R.id.tv_badges);
        mImgNoFeedback = (ImageView) contentLayout.findViewById(R.id.img_no_feedback);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        initEventAction();

        AppCoreBase.getConnection(new RequestListener() {
            @Override
            public void onPostExecuteError(
                    TypeActionConnection keyTypeAction, TypeErrorConnection keyType, String msg)
            {

            }

            @Override
            public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
                try {
                    JSONObject respondJSON = new JSONObject(response);

                    String invite_url = respondJSON.getString(ParserJson.API_PARAMETER_INVITE);
                    String policy_url = respondJSON.getString(ParserJson.API_PARAMETER_POLICY);


                    if (!TextUtils.isEmpty(invite_url)) {
                        AppCore.getPreferenceUtil(MainScreen.this).setValueString(ParserJson.API_PARAMETER_INVITE,
                                invite_url);
                    }
                    if (!TextUtils.isEmpty(policy_url)) {
                        AppCore.getPreferenceUtil(MainScreen.this).setValueString(ParserJson.API_PARAMETER_POLICY, policy_url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onResultSocket(TypeActionConnection keyType, Socket socket, String response) {

            }
        }).onExcuteProcess(TypeActionConnection.TYPE_ACTION_GET_LIST_COMMONINFO,
                RequestListener.API_CONNECTION_GET_COMMONINFO, null, false, false);
    }

    private void initEventAction() {

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(AppCore.getPreferenceUtil(MainScreen.this).getValueString(PreferenceUtil.IS_KEEP_WORKING).isEmpty()) {
                    if (isChangeColor) {
                        if (slideOffset < 0.3f) {
                            isStartChangeColor = true;
                        } else if (slideOffset > 0.7f) {
                            isStartUnChangeColor = true;
                        }

                        if (isStartChangeColor) {
                            isStartChangeColor = false;
                            AppCore.initToolsBar(MainScreen.this, true);
                        } else if (isStartUnChangeColor) {
                            AppCore.initToolsBar(MainScreen.this, false);
                            isStartUnChangeColor = false;
                        }
                    }
                }else {
                    initCloseMenu();
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                refreshFeedbackNotificationCount();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isChangeColor = true;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        img_avatar.setOnClickListener(this);

        nav_job_history.setOnClickListener(this);
        nav_job_list.setOnClickListener(this);
        nav_message.setOnClickListener(this);
        nav_invite.setOnClickListener(this);
        nav_support.setOnClickListener(this);
        nav_change_password.setOnClickListener(this);

        nav_job_history.setOnTouchListener(this);
        nav_job_list.setOnTouchListener(this);
        nav_message.setOnTouchListener(this);
        nav_invite.setOnTouchListener(this);
        nav_support.setOnTouchListener(this);
        nav_change_password.setOnTouchListener(this);

        nav_job_history.setFocusableInTouchMode(true);
        nav_job_list.setFocusableInTouchMode(true);
        nav_message.setFocusableInTouchMode(true);
        nav_invite.setFocusableInTouchMode(true);
        nav_support.setFocusableInTouchMode(true);
        nav_change_password.setFocusableInTouchMode(true);
    }

    /**
     * Auto update unread feedback from admin
     */
    private void refreshFeedbackNotificationCount() {

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN, AppCore.getPreferenceUtil(this).getValueString(PreferenceUtil.AUTH_TOKEN));
        AppCoreBase.getConnection(new RequestListener() {
            @Override
            public void onPostExecuteError(
                    TypeActionConnection keyTypeAction, TypeErrorConnection keyType, String msg)
            {
                Logger.e(TAG, MainScreen.this, "refreshFeedbackNotificationCount onPostExecuteError : " + msg);
            }

            @Override
            public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
                Logger.i(TAG, MainScreen.this, "refreshFeedbackNotificationCount onPostExecuteSuccess respond : " + response);
                try {
                    int count = Integer.valueOf(response);
                    if (count > 0) {
                        if (mUnreadFeedbackCount != null) {
                            mUnreadFeedbackCount.setText(response);
                            mUnreadFeedbackCount.setVisibility(View.VISIBLE);
                            mImgNoFeedback.setVisibility(View.GONE);
                        }
                    } else {
                        if (mImgNoFeedback != null) {
                            mUnreadFeedbackCount.setVisibility(View.GONE);
                            mImgNoFeedback.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (NumberFormatException e) {
                    Logger.e(TAG, MainScreen.this, e.getMessage());
                    e.printStackTrace();
                    if (mImgNoFeedback != null) {
                        mUnreadFeedbackCount.setVisibility(View.GONE);
                        mImgNoFeedback.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onResultSocket(TypeActionConnection keyType, Socket socket, String response) {
                // Un-use
            }
        }).onExcuteProcess(TypeActionConnection.TYPE_GET_UNREAD_FEEDBACK_COUNT, RequestListener.API_CONNECTION_NOTIFICATION_COUNT + VtcNWConnection.urlEncodeUTF8(map), null, false, false);
    }

    @Override
    public void onBackPressed() {

        if(Const.UI_CURRENT_CONTEXT == Const.UI_REPAIRER_INFO && !AppCore.getPreferenceUtil(MainScreen.this).getValueString(PreferenceUtil.IS_KEEP_WORKING).isEmpty()) {

            AppCore.initReceived(Const.TYPE_IS_KEEP_JOB, this.getResources().getString(R.string.validate_keep_job));

        }else {
            if (onBackListenner != null) {
                onBackListenner.onBack();
            }
            else if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                initCloseMenu();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_UP) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                    AppCore.initReceived(Const.UI_CURRENT_CONTEXT);
                    break;
            }
        }

        return super.onKeyUp(keyCode, event);
    }

    private void initCloseMenu(){
        if(drawer != null) {
            AppCore.initToolsBar(MainScreen.this, true);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void initOpenMenu(){
        if(drawer != null) {
            AppCore.initToolsBar(MainScreen.this, false);
            drawer.openDrawer(GravityCompat.START);
        }
    }

    public static void initMenu(Activity activity){
        if(drawer != null) {
            if(drawer.isActivated()) {
                AppCore.initToolsBar(activity, true);
                drawer.closeDrawer(GravityCompat.START);
            }else {
                AppCore.initToolsBar(activity, false);
                drawer.openDrawer(GravityCompat.START);
            }
        }
    }


    @Override
    public void onClick(View v) {
        Utils.hideKeyboard(this);

        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.img_avatar:
                initCloseMenu();
                AppCore.CallFragmentSection(Const.UI_MY_UPDATE_PROFILE);
                break;
            case R.id.img_edit_profile:
                initCloseMenu();
                AppCore.CallFragmentSection(Const.UI_MY_UPDATE_PROFILE);
                break;
        }
    }
    private void callUIActivity(Class aClass) {

        startActivity(new Intent(this, aClass));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(AppCore.getPreferenceUtil(MainScreen.this).getValueString(PreferenceUtil.IS_KEEP_WORKING).isEmpty()) {
            Utils.hideKeyboard(this);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                switch (v.getId()) {
                    case R.id.nav_job_history:
                        AppCore.CallFragmentSection(Const.UI_NAV_HISTORY);
                        break;
                    case R.id.nav_job_list:
                        AppCore.CallFragmentSection(Const.UI_NAV_BOOKINGSCHEDUDE);
                        break;
                    case R.id.nav_message:
                        AppCore.CallFragmentSection(Const.UI_NOTIFICATION);
                        break;
                    case R.id.nav_invite:
                        callUIActivity(UIInvite.class);
                        break;
                    case R.id.nav_support:
                        callUIActivity(UISupport.class);
                        break;
                    case R.id.nav_change_password:
                        AppCore.CallFragmentSection(Const.UI_MY_CONFIRM_CHANGE_PASSWORD);
                        break;
                }

                initCloseMenu();
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.hideKeyboard(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String strData = AppCore.getPreferenceUtil(MainScreen.this).getValueString(PreferenceUtil.IS_KEEP_WORKING);
        if(strData.isEmpty()) {
            initGetDataBundle();
        }else {

            try {
                JSONObject  jsonObject = new JSONObject(strData);
                VtcModelOrder vtcModelOrder = VtcModelOrder.getOrderData(jsonObject);
                if(vtcModelOrder != null){
                    if(vtcModelOrder.getAgency() != null){
                        vtcModelOrder.getAgency().setId(PreferenceUtil.IS_KEEP_WORKING);
                    }

                    AppCore.initReceived(Const.UI_GO_FRAGMENT, vtcModelOrder);
                }else {
                    initGetDataBundle();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (AppCoreBase.getGpsTracker(AppCore.getCurrentActivity()).isConnection() || Utils.isInternetAvailable()) {
                    setRefreshConnection(false);
                }
            }
        }).run();
    }

    private void initGetDataBundle(){
        if (bundle == null && this.getIntent() != null) {
            bundle = this.getIntent().getExtras();
        }

        if (bundle != null) {
            int type = bundle.getInt("type");
            String message = bundle.getString("message");
            String title = bundle.getString("title");
            String id_order = bundle.getString("id_order");
            String responseData = bundle.getString("order");

            VtcModelNoti vtcModelNoti = new VtcModelNoti();
            vtcModelNoti.setType(type);
            vtcModelNoti.setMessage(message);
            vtcModelNoti.setTitle(title);
            vtcModelNoti.setId_order(id_order);
            vtcModelNoti.setResponseData(responseData);
            vtcModelNoti.setTypeGoto(0);

            AppCore.initReceived(1000001, vtcModelNoti);

            bundle = null;

            setIntent(null);
        }
    }

    @Override
    public void onProfileUpdate(VtcModelUser user) {
        if (user != null) {
            MainScreen.updateProfile(MainScreen.this, user);
        }
    }

    public void setOnBackListenner(OnBackListenner onBackListenner) {
        this.onBackListenner = onBackListenner;
    }

    public boolean isRefreshConnection() {
        return isRefreshConnection;
    }

    public void setRefreshConnection(boolean refreshConnection) {
        AppCoreBase.showLog("setRefreshConnection " + refreshConnection);
        isRefreshConnection = refreshConnection;
    }


    public interface OnBackListenner {
        void onBack();
    }
}