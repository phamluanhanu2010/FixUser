package com.strategy.intecom.vtc.fixuser.view.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextThemeWrapper;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.strategy.intecom.vtc.fixuser.BuildConfig;
import com.strategy.intecom.vtc.fixuser.MainScreen;
import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.config.VtcNWConnection;
import com.strategy.intecom.vtc.fixuser.dialog.DLCancel;
import com.strategy.intecom.vtc.fixuser.dialog.DLInputData;
import com.strategy.intecom.vtc.fixuser.dialog.DLPaytype;
import com.strategy.intecom.vtc.fixuser.dialog.DLTime;
import com.strategy.intecom.vtc.fixuser.dialog.DlChoicePhoto;
import com.strategy.intecom.vtc.fixuser.dialog.DlConfirmDialog;
import com.strategy.intecom.vtc.fixuser.dialog.DlRatingRepairer;
import com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.interfaces.RequestListener;
import com.strategy.intecom.vtc.fixuser.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixuser.model.VtcModelUser;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.Logger;
import com.strategy.intecom.vtc.fixuser.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.utils.map.GPSTracker;

/**
 * Created by Mr. Ha on 5/16/16.
 */
public class AppCoreBase extends Fragment {

    private static final String TAG = "AppCoreBase";

    public static String APP_PATH;

    protected AlertDialog alertDialogMessage;
    protected AlertDialog alertDialog;
    protected static DlConfirmDialog alertDialogConfirmNetWork;
    protected static DlConfirmDialog alertDialogConfirmGPS;
    private static DlRatingRepairer dlRatingRepairer;

    protected static Handler handlerReceiveMessageAction;

    private static MainScreen mCurrentActivity;

    private static VtcModelUser vtcModelUser;

    private static OnProfileUpdateListenner profileUpdateListenner;

    protected static PreferenceUtil preferenceUtil;

    public static void setCurrentActivity(MainScreen mActivity) {
        AppCoreBase.mCurrentActivity = mActivity;
    }

    public static MainScreen getCurrentActivity() {
        return mCurrentActivity;
    }


    public static VtcNWConnection getConnection(RequestListener requestListener) {
        return new VtcNWConnection(getCurrentActivity(), requestListener);
    }

    public static VtcModelUser getVtcModelUser() {
        return vtcModelUser;
    }

    public static void setOnProfileUpdateListenner(OnProfileUpdateListenner listenner) {
        profileUpdateListenner = listenner;
    }

    public static void setVtcModelUser(VtcModelUser vtcModelUser) {
        AppCoreBase.vtcModelUser = vtcModelUser;
        AppCore.getPreferenceUtil(getCurrentActivity()).setValueString(PreferenceUtil.USER_INFO_AVATAR, vtcModelUser.getAvatar() + getExtraPathAvatar());
        if (profileUpdateListenner != null) {
            profileUpdateListenner.onProfileUpdate(vtcModelUser);
        }
    }

    protected static String getExtraPathAvatar(){
        return String.valueOf("?avatar_time=" + String.valueOf(System.currentTimeMillis()));
    }

    public static GPSTracker getGpsTracker(Activity context) {
        return new GPSTracker(context);
    }

    public static PreferenceUtil getPreferenceUtil(Context context) {
        if(preferenceUtil == null){
            preferenceUtil = new PreferenceUtil(context);
        }
        return preferenceUtil;
    }

    /**
     * Call Fragment
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     * @param bundle the send to data
     *
     */
    public static void CallFragmentSection(int KEY, Bundle bundle) {
        CallFragmentSection(KEY, bundle, true, true);
    }

    /**
     * Call Fragment
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     *
     */
    public static void CallFragmentSection(int KEY) {
        CallFragmentSection(KEY, true);
    }

    /**
     * Call Fragment
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     * @param isAddBackStack add back stack yes or no
     *
     */
    public static void CallFragmentSection(int KEY, boolean isAddBackStack) {
        CallFragmentSection(KEY, null, isAddBackStack, true);
    }

    /**
     * Call Fragment
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     * @param isAddBackStack add back stack yes or no
     * @param isAddFrag the replace for view or add view
     *
     */
    public static void CallFragmentSection(int KEY, boolean isAddBackStack, boolean isAddFrag) {
        CallFragmentSection(KEY, null, isAddBackStack, isAddFrag);
    }


    /**
     * Call Fragment
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     * @param bunds put data using bunder
     * @param isAddBackStack add back stack yes or no
     * @param isAddFrag the replace for view or add view
     *
     */
    public static void CallFragmentSection(int KEY, Bundle bunds, boolean isAddBackStack, boolean isAddFrag) {
        try {
            Const.UI_CURRENT_CONTEXT = KEY;
            System.gc();
            FragmentTransaction fragmentTS = getCurrentActivity().getSupportFragmentManager().beginTransaction();
            Fragment fragment = VtcFragmentFactory.getFragmentByKey(KEY);
            if(fragment != null) {
                fragment.setArguments(bunds);
            } else {
                return;
            }
            if(isAddFrag) {
                fragmentTS.add(R.id.fragcontent, fragment, fragment.getClass().getName());
            }else {
                fragmentTS.add(R.id.fragcontent, fragment, fragment.getClass().getName());
//                fragmentTS.add(android.R.id.content, fragment);
            }

            if (isAddBackStack) {
                fragmentTS.addToBackStack(fragment.getClass().getName());
            }
            fragmentTS.commit();
        } catch (Exception e) {
            AppCoreBase.showErrorLog(TAG + " Exception : " + e.getMessage());
        }
    }

    /**
     * Call Fragment callBack
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     * @param bundle put data using bunder
     * @param callback return data
     *
     */
    public static void CallFragmentSectionWithCallback(int KEY, Bundle bundle, Callback callback) {
        CallFragmentSectionWithCallback(KEY, bundle, callback, true, true);
    }

    /**
     * Call Fragment callBack
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     * @param callback return data
     *
     */
    public static void CallFragmentSectionWithCallback(int KEY, Callback callback) {
        CallFragmentSectionWithCallback(KEY, null, callback, true, true);
    }
    /**
     * Call Fragment callBack
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     * @param callback return data
     * @param isAddBackStack add back stack yes or no
     * @param isAddFrag the replace for view or add view
     *
     */
    public static void CallFragmentSectionWithCallback(int KEY, Callback callback, boolean isAddBackStack, boolean isAddFrag) {
        CallFragmentSectionWithCallback(KEY, null, callback, isAddBackStack, isAddFrag);
    }

    /**
     * Call Fragment callBack
     *
     * @param KEY the key for fragment on class VtcFragmentFactory.class
     * @param bund put data using bunder
     * @param callback return data
     * @param isAddBackStack add back stack yes or no
     * @param isAddFrag the replace for view or add view
     *
     */
    public static void CallFragmentSectionWithCallback(int KEY, Bundle bund, Callback callback, boolean isAddBackStack, boolean isAddFrag) {
        try {
            Const.UI_CURRENT_CONTEXT = KEY;
            System.gc();
            FragmentTransaction fragmentTS = getCurrentActivity().getSupportFragmentManager().beginTransaction();
            Fragment fragment = VtcFragmentFactory.getFragmentWithCallbackByKey(KEY, callback);
            if(fragment != null) {
                fragment.setArguments(bund);
            } else {
                return;
            }
            if(isAddFrag) {
                fragmentTS.add(R.id.fragcontent, fragment, fragment.getClass().getName());
            }else {
//                fragmentTS.add(android.R.id.content, fragment);
                fragmentTS.add(R.id.fragcontent, fragment, fragment.getClass().getName());
            }

            if (isAddBackStack) {
                fragmentTS.addToBackStack(fragment.getClass().getName());
            }
            fragmentTS.commit();
        } catch (Exception e) {
        }
    }

    /**
     * <p>Call when wan using dialog message, confirm, option..</p>
     *
     * <p>Type call Dialog</p>
     *
     * <p>TYPE_SHOW_MESSAGE_INFO</p>
     * <p>TYPE_SHOW_MESSAGE_CONFIRM</p>
     * <p>TYPE_SHOW_MESSAGE_INPUT</p>
     * <p>TYPE_SHOW_MESSAGE_INFO_REPAIRER</p>
     * <p>TYPE_SHOW_MESSAGE_INFO_NOTE</p>
     * <p>TYPE_SHOW_MESSAGE_INFO_PAY_TYPE</p>
     * <p>TYPE_SHOW_MESSAGE_INFO_TIME</p>
     * <p>TYPE_SHOW_MESSAGE_INFO_PROMOTION</p>
     *
     * @param context context
     * @param typekey Key type call dialog
     *
     */
    public void initShowDialogOption(Context context, TypeShowDialog typekey){
        initShowDialogOption(context, typekey, "", "");
    }

    /**
     * <p>Call when wan using dialog message, confirm, option..</p>
     *
     * <p>Type call Dialog</p>
     *
     * <p>TYPE_SHOW_MESSAGE_INFO</p>
     * <p>TYPE_SHOW_MESSAGE_CONFIRM</p>
     * <p>TYPE_SHOW_MESSAGE_INPUT</p>
     * <p>TYPE_SHOW_MESSAGE_INFO_REPAIRER</p>
     * <p>TYPE_SHOW_MESSAGE_INFO_NOTE</p>
     * <p>TYPE_SHOW_MESSAGE_INFO_PAY_TYPE</p>
     * <p>TYPE_SHOW_MESSAGE_INFO_TIME</p>
     * <p>TYPE_SHOW_MESSAGE_INFO_PROMOTION</p>
     *
     * @param context context
     * @param typekey Key type call dialog
     * @param title title dialog
     * @param msg content dialog
     *
     */
    public void initShowDialogOption(Context context, TypeShowDialog typekey, String title, Object msg) {

        int key = typekey.getValuesTypeDialog();

        String strmsg = "";
        if(msg instanceof String) {
            strmsg = (String) msg;
        }

        switch (TypeShowDialog.forValue(key)) {
            case TYPE_SHOW_MESSAGE_INFO:
                initShowMessageInfo(context, title, strmsg);
                break;

            case TYPE_SHOW_FILL_ADDRESS:
                initShowMessageInfo(context, title, strmsg, typekey);
                break;

            case TYPE_SHOW_FILL_TIME:
                initShowMessageInfo(context, title, strmsg, typekey);
                break;

            case TYPE_SHOW_MESSAGE_CONFIRM:
                initShowMessageAlert(context, title, strmsg, typekey);
                break;

            case TYPE_SHOW_MESSAGE_INFO_REPAIRER:
                initShowMessageRepairer(context, msg);
                break;

            case TYPE_SHOW_MESSAGE_INFO_NOTE:
                initShowMessageNoteInfo(context, TypeShowDialog.TYPE_SHOW_MESSAGE_INFO_NOTE, (String) msg);
                break;

            case TYPE_SHOW_MESSAGE_INFO_PAY_TYPE:
                initShowMessagePayType(context, TypeShowDialog.TYPE_SHOW_MESSAGE_INFO_PAY_TYPE);
                break;

            case TYPE_SHOW_MESSAGE_INFO_TIME:
                initShowMessageSelectTime(context);
                break;

            case TYPE_SHOW_MESSAGE_INFO_PROMOTION:
                initShowMessageNoteInfo(context, TypeShowDialog.TYPE_SHOW_MESSAGE_INFO_PROMOTION, (String) msg);
                break;
            case TYPE_SHOW_MESSAGE_CANCEL:
                initShowMessageCancel(context);
                break;
            case TYPE_SHOW_ENABLE_NETWORK:
                initShowMessageConfirmNetwork((Activity) context);
                break;
            case TYPE_SHOW_ENABLE_GPS:
                initShowMessageConfirmGPS((Activity) context);
                break;
            case TYPE_SHOW_OPTION_CHOICE_MEDIA:
                initShowSelectPhoto(context);
                break;
            case TYPE_SHOW_MESSAGE_CHANGE_ORDER_TIME:
                initShowMessageChangeOrderTime(context, title, (String) msg, typekey);
                break;
        }
    }

    private void initShowMessageCancel(Context context) {

//        int []size = Utils.getSizeScreen((Activity) context);

        DLCancel dlCancel = new DLCancel(context);
        dlCancel.setOnClickAccept(new DLInputData.onClickAccept() {
            @Override
            public void onClick(String str) {
                cmdPressDialogYes(TypeShowDialog.TYPE_SHOW_MESSAGE_CANCEL, str);
            }
        });
        dlCancel.show();
    }


    /**
     * Call dialog when show dialog message info
     *
     * @param context context
     * @param smg content message
     * @return Dialog
     *
     */

    private void initShowMessageInfo(final Context context, final String title, final String smg) {

        if (alertDialogMessage != null && alertDialogMessage.isShowing()) {
            alertDialogMessage.dismiss();
            alertDialogMessage = null;
        }

        if (context == null) {
            return;
        }

        alertDialogMessage = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom))
                .setCancelable(false)
                .setTitle(title.equals("") ? context.getResources().getString(R.string.title_dialog_message) : title)
                .setMessage(smg)
                .setPositiveButton(R.string.accepted, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cmdPressDialogYesInfo();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

    }
    /**
     * Call dialog when show dialog message info
     *
     * @param context context
     * @param smg content message
     * @return Dialog
     *
     */

    private void initShowMessageInfo(final Context context, final String title, final String smg, final  TypeShowDialog typeShowDialog) {

        if (alertDialogMessage != null && alertDialogMessage.isShowing()) {
            alertDialogMessage.dismiss();
            alertDialogMessage = null;
        }

        if (context == null) {
            return;
        }

        Logger.d(TAG, this, "initShowMessageInfo");

        alertDialogMessage = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom))
                .setCancelable(false)
                .setTitle(title.equals("") ? context.getResources().getString(R.string.title_dialog_message) : title)
                .setMessage(smg)
                .setPositiveButton(R.string.accepted, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cmdPressDialogYes(typeShowDialog);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

    }

    /**
     *
     * Call dialog when note info
     *
     * @param context context
     * @param typeShowDialog key type call show dialog
     *
     */
    private void initShowMessageNoteInfo(Context context, final TypeShowDialog typeShowDialog, String content){

        int []size = Utils.getSizeScreen((Activity) context);

        DLInputData dlNote = new DLInputData(context, typeShowDialog, content, size[0]);
        dlNote.setOnClickAccept(new DLInputData.onClickAccept() {
            @Override
            public void onClick(String str) {
                cmdPressDialogYes(typeShowDialog, str);
            }
        });
        dlNote.show();
    }

    /**
     * Call dialog when search repairer finish
     *
     * @param context
     *
     */
    private void initShowMessageRepairer(Context context, Object object){

        if(context != null && object instanceof VtcModelOrder) {
            VtcModelOrder vtcModelOrder = (VtcModelOrder) object;

            if(dlRatingRepairer != null){
                if (dlRatingRepairer.isShowing()) {
                    dlRatingRepairer.cancel();
                }
                dlRatingRepairer = null;
            }

            dlRatingRepairer = new DlRatingRepairer(context, vtcModelOrder);
            dlRatingRepairer.show();
        }

    }

    /**
     * Call dialog when select pay type
     *
     * @param context
     * @param typeShowDialog key type call show dialog
     *
     */
    private void initShowMessagePayType(Context context, final TypeShowDialog typeShowDialog){

        int []size = Utils.getSizeScreen((Activity) context);
        DLPaytype dl = new DLPaytype(context, size[0]);
        dl.setOnClick(new DLPaytype.onClickItem() {
            @Override
            public void onClickItem(String param) {
                cmdPressDialogYes(typeShowDialog, param);
            }
        });
        dl.show();

    }

    /**
     *
     *  Call dialog when select Time
     *
     * @param context
     *
     */
    public void initShowMessageSelectTime(Context context){

        int []size = Utils.getSizeScreen((Activity) context);
        DLTime dlTime = new DLTime(context, size[0]);
        dlTime.setOnClickItem(new DLTime.onClickItem() {
            @Override
            public void onClickItemType(String type, String strDisplay, String strSendSV) {
                cmdOnSelectTypeOrder(type, strDisplay, strSendSV);
            }
        });
        dlTime.show(((FragmentActivity) context).getSupportFragmentManager(), "");
    }

    /**
     * Call dialog when show dialog message Alert
     *
     * @param context
     * @param smg
     * @return Dialog
     *
     */
    private void initShowMessageAlert(Context context, String title, String smg, final TypeShowDialog dialogType) {

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }

        if (context == null) {
            return;
        }

        alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom))
                .setCancelable(false)
                .setTitle(title.equals("") ? context.getResources().getString(R.string.title_dialog_confirm) : title)
                .setMessage(smg)
                .setPositiveButton(R.string.accepted, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cmdPressDialogYes(dialogType);
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cmdPressDialogNo(dialogType);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Call dialog when show dialog message Alert
     *
     * @param context
     * @param smg
     * @return Dialog
     *
     */
    private void initShowMessageChangeOrderTime(Context context, String title, String smg, final TypeShowDialog dialogType) {

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }

        if (context == null) {
            return;
        }

        alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom))
                .setCancelable(false)
                .setTitle(title.equals("") ? context.getResources().getString(R.string.title_dialog_confirm) : title)
                .setMessage(smg)
                .setPositiveButton(R.string.confirm_button_change_order_time, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cmdPressDialogYes(dialogType);
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cmdPressDialogNo(dialogType);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Call dialog when show dialog message Alert setting network
     *
     * @param context
     * @return Dialog
     *
     */
    public void initShowMessageConfirmNetwork(final Activity context) {
        if (context == null) {
            return;
        }

        if (alertDialogConfirmNetWork != null) {
            return;
        }


        DlConfirmDialog.Builder builder = new DlConfirmDialog.Builder(context);
        builder.content(context.getResources().getString(R.string.network_not_enabled));
        builder.okString(context.getResources().getString(R.string.open_network_settings));
        builder.onClickistener(new DlConfirmDialog.onClickDialogItem() {
            @Override
            public void onClickDialogItemCancel() {
                alertDialogConfirmNetWork.dismiss();
                alertDialogConfirmNetWork = null;
                getCurrentActivity().finish();
            }

            @Override
            public void onClickDialogItemOK() {
                alertDialogConfirmNetWork.dismiss();
                Intent myIntent = new Intent(Settings.ACTION_SETTINGS);
                getCurrentActivity().startActivity(myIntent);
                alertDialogConfirmNetWork = null;
                getCurrentActivity().setRefreshConnection(true);
            }
        });

        alertDialogConfirmNetWork = builder.build();

        alertDialogConfirmNetWork.show();
    }
    /**
     * Call dialog when show dialog message Alert setting network
     *
     * @param context
     * @return Dialog
     *
     */
    public void initShowMessageConfirmGPS(final Activity context) {
        if (context == null) {
            return;
        }

        if (alertDialogConfirmGPS != null) {
            return;
        }


        DlConfirmDialog.Builder builder = new DlConfirmDialog.Builder(context);
        builder.content(context.getResources().getString(R.string.gps_not_enabled));
        builder.okString(context.getResources().getString(R.string.open_location_settings));
        builder.onClickistener(new DlConfirmDialog.onClickDialogItem() {
            @Override
            public void onClickDialogItemCancel() {
                alertDialogConfirmGPS.dismiss();
                alertDialogConfirmGPS = null;
                getCurrentActivity().finish();
            }

            @Override
            public void onClickDialogItemOK() {
                alertDialogConfirmGPS.dismiss();
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getCurrentActivity().startActivity(myIntent);
                alertDialogConfirmGPS = null;
                getCurrentActivity().setRefreshConnection(true);
            }
        });

        alertDialogConfirmGPS = builder.build();

        alertDialogConfirmGPS.show();
    }

    /**
     * show the custom dialog
     * @param context
     * @param title
     * @param message
     * @param okButton
     * @param cancelButton
     * @param okListenner
     * @param cancelListener
     */
    protected void initShowCustomAlertDialog(Context context, String title, String message,
            String okButton, String cancelButton, DialogInterface.OnClickListener okListenner,
            DialogInterface.OnClickListener cancelListener)
    {
        showLog("initShowCustomAlertDialog");

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }

        if (context == null) {
            return;
        }

        alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom))
                .setCancelable(false)
                .setTitle(title.equals("") ? context.getResources().getString(R.string.title_dialog_confirm) : title)
                .setMessage(message)
                .setPositiveButton(okButton, (okListenner != null) ?
                        okListenner : new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.cancel();
                    }
                }).setNegativeButton(cancelButton, (cancelListener != null) ?
                        cancelListener : new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    /**
     *
     *  Call dialog when select photo
     *
     * @param context
     *
     */
    public void initShowSelectPhoto(Context context){

        int []size = Utils.getSizeScreen((Activity) context);
        DlChoicePhoto dlTime = new DlChoicePhoto(context, size[0]);
        dlTime.setOnClickDialogPhoto(new DlChoicePhoto.IActionDialogPhoto() {
            @Override
            public void onClickGetPictureLibrary() {
                cmdOnLibrary();
            }

            @Override
            public void onClickGetCamera() {
                cmdOnCapture();
            }
        });
        dlTime.show();
    }

    public static void initReceived(int key) {
        initReceived(key, -1, null, -1);
    }

    public static void initReceived(int key, int arg1) {
        initReceived(key, arg1, null, -1);
    }

    public static void initReceived(int key, Object object) {
        initReceived(key, -1, object, -1);
    }

    public static void initReceived(int key, int arg1, Object object) {
        initReceived(key, arg1, object, -1);
    }

    public static void initReceived(int key, int arg1, int arg2) {
        initReceived(key, arg1, null, arg2);
    }

    public static void initReceived(int key, int arg1, Object object, int arg2) {
        try {
            if(AppCore.handlerReceiveMessageAction != null){

                AppCoreBase.showLog("initReceived send noti");
                android.os.Message msg = AppCore.handlerReceiveMessageAction.obtainMessage();
                msg.what = key;
                msg.obj = object;
                msg.arg1 = arg1;
                msg.arg2 = arg2;
                AppCore.handlerReceiveMessageAction.sendMessage(msg);
            }
        }catch (ExceptionInInitializerError error){
            error.printStackTrace();
        }catch (NoClassDefFoundError e){
            e.printStackTrace();
        }
    }

    /**
     *
     * Show Log toast
     * Enable when DEBUG is true
     *
     * @param view
     * @param msg content message
     *
     */
    public static void showLogToast(View view, String msg){
        if(BuildConfig.DEBUG) {
//            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }


    /**
     *
     * Show toast Long for message
     *
     * @param view
     * @param msg content message
     *
     */
    public static void showToastLong(View view, String msg){
//        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    /**
     *
     * Show toast Short for message
     *
     * @param view
     * @param msg content message
     *
     */
    public static void showToastShort(View view, String msg){
//        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }


    /**
     *
     * Show Log Bug
     * Enable when DEBUG is true
     *
     * @param msg content log
     *
     */
    public static void showLog(String msg){
        if(BuildConfig.DEBUG){
            Logger.i("Ha : ", null, "----------------- : " + msg);
        }
    }


    /**
     *
     * Show Log Bug
     * Enable when DEBUG is true
     *
     * @param msg content log
     *
     */
    public static void showErrorLog(String msg){
        if(BuildConfig.DEBUG){
            Logger.e("Ha : ", null, "----------------- : " + msg);
        }
    }

    /**
     *
     * Show Log Bug for Tag
     * Enable when DEBUG is true
     *
     * @param tag Tag Name
     * @param msg content log
     *
     */
    public static void showLog(String tag, String msg){
        if(BuildConfig.DEBUG){
            Logger.i(tag, null, "----------------- : " + msg);
        }
    }


    /**
     *
     * Call when action on message info yes
     *
     * @param str return data
     *
     */
    public void cmdPressDialogYes(TypeShowDialog typeShowDialog, String str){

    }

    /**
     *
     * Call when action on message info yes
     *
     */
    public void cmdPressDialogYesInfo(){

    }

    /**
     *
     * Call when action on message info yes
     *
     * @param dialogType
     */
    public void cmdPressDialogYes(TypeShowDialog dialogType){

    }


    /**
     *
     * Call when action on message info No
     *
     * @param dialogType
     */
    public void cmdPressDialogNo(TypeShowDialog dialogType){

    }

    /**
     *
     * Call when action set image
     *
     */
    protected void cmdOnSetPicture(Bitmap bitmap, String strPath){

    }

    /**
     *
     * Call when action Normal order
     *
     */
    protected void cmdOnSelectTypeOrder(String type, String strDisplay, String strSend){

    }

    /**
     *
     * Call when action Create Order
     *
     */
    protected void cmdOnResponseListAgency(String response){

    }

    /**
     *
     * Call when action Agency Accept Order
     *
     */
    protected void cmdOnResponseAcceptOrder(String response){

    }

    /**
     *
     * Call when action Get location
     *
     */
    protected void cmdOnResponseGetLocation(String response){

    }

    /**
     *
     * Call when action Get location
     *
     */
    protected void cmdOnGetLocation(LatLng destPosition){

    }

    /**
     *
     * Call when action reset data
     *
     */
    protected void cmdOnNotifiData(VtcModelOrder vtcModelOrder){

    }

    /**
     *
     * Call when action reset data
     *
     */
    protected void cmdOnCancelJob(){

    }

    /**
     *
     * Call when action Refresh Location
     *
     */
    protected void cmdOnRefreshLocation(){

    }

    /**
     *
     * Call when action Capture
     *
     */
    protected void cmdOnCapture(){

    }
    /**
     *
     * Call when action Choice Library
     *
     */
    protected void cmdOnLibrary(){

    }


    /**
     *
     * Call when back screen
     *
     */
    public void cmdBack() {
        FragmentActivity activity = getCurrentActivity();
        if (activity == null) {
            return;
        }

        try {
            FragmentManager fm = activity.getSupportFragmentManager();

            int index = fm.getBackStackEntryCount() - 2;
            if (fm.getBackStackEntryCount() > 1) {
                FragmentManager.BackStackEntry backEntry = fm.getBackStackEntryAt(index);
                String tag = backEntry.getName();
                Fragment fragment = fm.findFragmentByTag(tag);

                if (fragment instanceof AppCoreBase) {
                    ((AppCoreBase) fragment).onBackToTop();
                }
            }
            fm.popBackStack();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Call when back screen
     *
     */
    public void cmdBack(int i) {
        FragmentActivity activity = getCurrentActivity();
        if (activity == null) {
            return;
        }

        try {
            FragmentManager fm = activity.getSupportFragmentManager();

            int index = fm.getBackStackEntryCount() - (i+1);
            if (fm.getBackStackEntryCount() > i) {
                FragmentManager.BackStackEntry backEntry = fm.getBackStackEntryAt(index);
                String tag = backEntry.getName();
                Fragment fragment = fm.findFragmentByTag(tag);
                showLog("getBackStackEntryCount " + fm.getBackStackEntryCount());
                showLog("check fragment is AppCoreBase or not " + tag);
                showLog("fragment : " + fragment);

                if (fragment instanceof AppCoreBase) {
                    showLog("fragment is AppCoreBase");
                    ((AppCoreBase) fragment).onBackToTop();
                }
                index = fm.getBackStackEntryCount() - (i);
                tag = fm.getBackStackEntryAt(index).getName();
                fm.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    protected void onBackToTop() {
        // Do nothing
        showLog("default onBackToTop called");
    }

    public interface OnProfileUpdateListenner {
        void onProfileUpdate(VtcModelUser user);
    }
}
