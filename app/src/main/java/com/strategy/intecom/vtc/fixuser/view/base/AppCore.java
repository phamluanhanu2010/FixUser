package com.strategy.intecom.vtc.fixuser.view.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.MainScreen;
import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixuser.interfaces.RequestListener;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNewOrder;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNoti;
import com.strategy.intecom.vtc.fixuser.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixuser.service.GcmListenerServices;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.utils.UtilsBitmap;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMMainJobMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Mr. Ha on 5/18/16.
 */
public abstract class AppCore extends AppCoreMap implements RequestListener {

    private String FILE_PATH = "";
    private String FILE_NAME = "";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        handlerReceiveMessageAction = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                initReceiveMeaasge(msg);
            }
        };

        // Create APP_PATH folder
        if (isExternalStorageWritable()) {
            AppCoreBase.APP_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            AppCoreBase.APP_PATH = getCurrentActivity().getCacheDir().getAbsolutePath();
        }

        File file = new File(AppCoreBase.APP_PATH, getAlbumName());
        if (!file.exists()) {
            file.mkdirs();
        }

        AppCoreBase.APP_PATH = file.getAbsolutePath();
    }


    /**
     * Handle When receive message action
     * Push action to UI Similar
     *
     * @param handlerMessage
     */
    protected synchronized void initReceiveMeaasge(Message handlerMessage) {
        showLog("initReceiveMeaasge " + handlerMessage.what);
        synchronized (this) {
            switch (handlerMessage.what) {
                case Const.UI_REPAIRER_SEARCH:
                    String responseLst = String.valueOf(handlerMessage.obj);
                    cmdOnResponseListAgency(responseLst);
                    break;

                case Const.UI_MY_CONFIRM_ACCEPT_JOB:
                    String responseAccept = String.valueOf(handlerMessage.obj);
                    cmdOnResponseAcceptOrder(responseAccept);
                    break;

                case Const.UI_MY_CONFIRM_GET_LOCATION:
                    String responseGetLocation = String.valueOf(handlerMessage.obj);
                    cmdOnResponseGetLocation(responseGetLocation);
                    break;

                case Const.TYPE_LOGOUT:
                    AppCore.getPreferenceUtil(getActivity()).RemoveDataWhenLogOut();
                    startActivity(new Intent(getActivity(), MainScreen.class));
                    getCurrentActivity().finish();

                    break;

                case Const.TYPE_IS_KEEP_JOB:
                    if (handlerMessage.obj instanceof String) {
                        initShowDialogOption(getCurrentActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", String.valueOf(handlerMessage.obj));
                    }
                    break;

                case Const.UI_GO_FRAGMENT:

                    if (handlerMessage.obj instanceof VtcModelOrder) {
                        Bundle bundle = new Bundle();

                        bundle.putParcelable(Const.KEY_BUNDLE_ACTION_NEW_ORDER, (VtcModelOrder) handlerMessage.obj);

                        AppCore.CallFragmentSection(Const.UI_REPAIRER_INFO, bundle);
                    }
                    break;

                default:
                    if (handlerMessage.obj instanceof VtcModelNoti) {
                        initGotoFragmentFromNoti((VtcModelNoti) handlerMessage.obj);
                    }
                    break;
            }

        }
    }

    /**
     * Handle When receive message action notification goto from Noti
     *
     * @param vtcModelNoti obj noti data
     */
    public void initGotoFragmentFromNoti(VtcModelNoti vtcModelNoti) {
        showLog("initGotoFragmentFromNoti " + vtcModelNoti.getType());

        if (vtcModelNoti != null) {

            int type = vtcModelNoti.getType();

            switch (type) {
                case GcmListenerServices.NOTI_AGENCY_ACCEPT:
                    initOrderShowDialog(vtcModelNoti.getResponseData());
                    break;
                case GcmListenerServices.NOTI_AGENCY_CANCEL_JOB:

                    if (getTimerRequestLocation() != null) {
                        getTimerRequestLocation().cancel();
                        setTimerRequestLocation(null);
                    }

                    cmdOnCancelJob();

                    initOrderShowDialog(vtcModelNoti.getResponseData());
                    break;
                case GcmListenerServices.NOTI_CHANGE_STATUS_COMING:
                    initOrderShowDialog(vtcModelNoti.getResponseData());
                    break;
                case GcmListenerServices.NOTI_CHANGE_STATUS_FINISH:

                    AppCore.getPreferenceUtil(getCurrentActivity()).setValueString(PreferenceUtil.IS_KEEP_WORKING, vtcModelNoti.getResponseData());

                    if (Const.UI_CURRENT_CONTEXT == Const.UI_REPAIRER_INFO) {
                        try {
                            JSONObject jsonObject = new JSONObject(vtcModelNoti.getResponseData());
                            VtcModelOrder vtcModelOrder = VtcModelOrder.getOrderData(jsonObject);
                            cmdOnNotifiData(vtcModelOrder);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        Bundle bundle = new Bundle();

                        bundle.putParcelable(Const.KEY_BUNDLE_ACTION_NOTI, vtcModelNoti);

                        AppCore.CallFragmentSection(Const.UI_REPAIRER_INFO, bundle);
                    }
                    break;
                case GcmListenerServices.NOTI_CHANGE_STATUS_START_JOB:
                    initOrderShowDialog(vtcModelNoti.getResponseData());
                    break;
                case GcmListenerServices.NOTI_REMIND_AGENCY_NOT_COME:
                    try {
                    JSONObject jsonObject = new JSONObject(vtcModelNoti.getResponseData());
                    VtcModelOrder vtcModelOrder = VtcModelOrder.getOrderData(jsonObject);
                        initDialogRemindAgencyNotCome(vtcModelOrder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    break;
                case GcmListenerServices.NOTI_REMIND_ORDER_EXPIRED:
                    try {
                        JSONObject jsonObject = new JSONObject(vtcModelNoti.getResponseData());
                        VtcModelOrder vtcModelOrder = VtcModelOrder.getOrderData(jsonObject);
                        initDialogRemindAgencyLate(vtcModelOrder);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case GcmListenerServices.NOTI_NO_AGENCY_ACCEPTED:
                    try {
                        JSONObject jsonObject = new JSONObject(vtcModelNoti.getResponseData());
                        VtcModelOrder vtcModelOrder = VtcModelOrder.getOrderData(jsonObject);
                        initDialogRemindNoAgencyAccept(vtcModelOrder);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

    }


    private void initDialogRemindNoAgencyAccept(final VtcModelOrder order) {
        initShowCustomAlertDialog(getContext(), "", getCurrentActivity().getResources().getString(R.string
                        .message_no_one_accept),
                getCurrentActivity().getResources().getString(R.string.btn_order_now), getCurrentActivity()
                        .getResources().getString(R.string.cancel), new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                initCancelOrderMark(null, getVtcModelUser().get_id(), order.getId(),
                                        getCurrentActivity().getResources().getString(R.string.reason_no_accepted));
                                LocalBroadcastManager.getInstance(getCurrentActivity()).sendBroadcast(new Intent
                                        (FMMainJobMap.KEY_NEW_ORDER_FILTER));
                            }
                        }, null);
    }

    private void initDialogRemindAgencyLate(final VtcModelOrder order) {
        initShowCustomAlertDialog(getContext(), "", getCurrentActivity().getResources().getString(R.string
                        .message_agency_late),
                getCurrentActivity().getResources().getString(R.string.btn_order_now), getCurrentActivity()
                        .getResources().getString(R.string.btn_close), new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                initCancelOrderMark(null, getVtcModelUser().get_id(), order.getId(),
                                        getCurrentActivity().getResources().getString(R.string.expired));
                                LocalBroadcastManager.getInstance(getCurrentActivity()).sendBroadcast(new Intent
                                        (FMMainJobMap.KEY_NEW_ORDER_FILTER));
                            }
                        }, null);
    }

    private void initDialogRemindAgencyNotCome(final VtcModelOrder order) {
        initShowCustomAlertDialog(getContext(), "", getCurrentActivity().getResources().getString(R.string
                        .mesage_agency_not_come),
                getCurrentActivity().getResources().getString(R.string.btn_call_agency), getCurrentActivity()
                        .getResources().getString(R.string.btn_close), new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                VtcModelOrder.Agency agency = order.getAgency();

                                if (agency != null) {
                                    Utils.call(getCurrentActivity(), agency.getPhone());
                                }
                            }
                        }, null);
    }

    private void initOrderShowDialog(String strResspon) {
        try {
            JSONObject jsonObject = new JSONObject(strResspon);

            VtcModelOrder vtcModelOrder = VtcModelOrder.getOrderData(jsonObject);

            initShowDialogOption(getCurrentActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO_REPAIRER, "", vtcModelOrder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call when want get size screen
     *
     * @param context
     * @return width, height
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static int[] getWithHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Activity.WINDOW_SERVICE);
        int[] widthheight = new int[2];

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            widthheight[0] = manager.getDefaultDisplay().getWidth();
            widthheight[1] = manager.getDefaultDisplay().getHeight();
        } else {
            Point point = new Point();
            manager.getDefaultDisplay().getSize(point);
            widthheight[0] = point.x;
            widthheight[1] = point.y;
        }

        return widthheight;
    }

    protected void dispatchPictureLibraryIntent(int requestCode) {

        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), requestCode);

    }

    protected void initGetCamera(int requestCode, String path) {
        final File filetmp = new File(AppCoreBase.APP_PATH, path);
        if (!filetmp.exists()) {
            filetmp.mkdirs();
        }
        FILE_PATH = filetmp.getAbsolutePath();
        FILE_NAME = String.valueOf(System.currentTimeMillis()) + ".jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(FILE_PATH, FILE_NAME);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, requestCode);
    }

    private boolean performCropImage(Uri mFinalImageUri, int hight, int width) {
        try {
            if (mFinalImageUri != null) {
                Intent cropIntent = new Intent("com.android.camera.action.CROP");
                cropIntent.setDataAndType(mFinalImageUri, "image/*");
                cropIntent.putExtra("crop", "true");
                cropIntent.putExtra("aspectX", width);
                cropIntent.putExtra("aspectY", hight);
                cropIntent.putExtra("scaleUpIfNeeded", false);
                cropIntent.putExtra("outputX", width * 100);
                cropIntent.putExtra("outputY", hight * 100);
                cropIntent.putExtra("return-data", false);

                FILE_NAME = "CROP_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                File f = new File(FILE_PATH, FILE_NAME);
                cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(cropIntent, Const.REQUEST_CODE_CROP_IMAGE);
                return true;
            }
        } catch (ActivityNotFoundException anfe) {
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            showLogToast(getView(), errorMessage);
            return false;
        }
        return false;
    }

    private String getAlbumName() {
        Activity activity = getCurrentActivity();
        if (activity == null) {
            return "";
        }
        return activity.getResources().getString(R.string.app_name);
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    /**
     * Call when using service call
     *
     * @param num Phone number
     */
    public void initCallIntentPhone(String num) {

        try {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
            startActivity(intent);
        } catch (SecurityException ex) {
            AppCore.showLog("initCallIntentPhone ----- : " + ex.getMessage());
        }
    }


    /**
     * Call when using service send message
     *
     * @param num  Phone number
     * @param body Body message
     */
    public void initCallIntentMessage(String num, String body) {

        try {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", num);
            smsIntent.putExtra("sms_body", body);
            startActivity(smsIntent);
        } catch (ActivityNotFoundException ex) {
            AppCore.showLog("initCallIntentMessage ----- : " + ex.getMessage());
            try {
                PendingIntent sentIntent = PendingIntent.getActivity(getCurrentActivity(), 0, new Intent(getCurrentActivity(), MainScreen.class), 0);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(num, null, body, sentIntent, null);
            } catch (SecurityException e) {
                AppCore.showLog("initCallIntentMessage ActivityNotFoundException ----- : " + e.getMessage());
            }
        }
    }

    /**
     * Call when using set Format currency
     *
     * @param textView
     * @param strPrice
     */
    public static void setFormatCurrency(TextView textView, String strPrice) {

        try {
            float price = Float.parseFloat(strPrice);

//            if(price == 0.0){
//                return;
//            }

            textView.setText(Html.fromHtml(getCurrentActivity().getResources().getString(R.string.title_currency, Utils.formatDecimal(price))));
        } catch (NullPointerException e) {
            e.printStackTrace();
            textView.setText(Html.fromHtml(getCurrentActivity().getResources().getString(R.string.title_currency, strPrice)));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            textView.setText(Html.fromHtml(getCurrentActivity().getResources().getString(R.string.title_currency, strPrice)));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case Const.REQUEST_CODE_CAM:

                    cmdOnSetPicture(null, FILE_PATH + File.separator + FILE_NAME);

                    break;
                case Const.REQUEST_CODE_LOAD_IMAGE:
                    if (data != null) {

                        FILE_PATH = UtilsBitmap.getRealPathFromURI(getActivity(), data.getData());

                        cmdOnSetPicture(null, FILE_PATH);
                    }

                    break;

                case Const.REQUEST_CODE_CAM_AVATAR:
                    File fRequestCamAvatar = UtilsBitmap.isFileOnList(FILE_PATH, FILE_NAME);
                    if (fRequestCamAvatar.exists()) {
                        Uri picUri = Uri.fromFile(fRequestCamAvatar);
                        performCropImage(picUri, 3, 3);
                    }
                    break;
                case Const.REQUEST_CODE_LOAD_IMAGE_AVATAR:
                    if (data != null) {

                        FILE_PATH = UtilsBitmap.getRealPathFromURI(getActivity(), data.getData());

                        String[] strPath = FILE_PATH.split(File.separator);

                        FILE_NAME = strPath[strPath.length - 1];

                        FILE_PATH = FILE_PATH.replace(String.valueOf(File.separator + FILE_NAME), "");

                        if (FILE_PATH != null && !FILE_PATH.isEmpty()) {

                            File fLoadImageAvatar = UtilsBitmap.isFileOnList(FILE_PATH, FILE_NAME);
                            if (fLoadImageAvatar.exists()) {
                                Uri picUri = Uri.fromFile(fLoadImageAvatar);
                                performCropImage(picUri, 3, 3);
                            }
                        }
                    }

                    break;

                case Const.REQUEST_CODE_CROP_IMAGE:
                    File fCrop = UtilsBitmap.isFileOnList(FILE_PATH, FILE_NAME);
                    try {
                        Bitmap bitmap = BitmapFactory.decodeFile(fCrop.getAbsolutePath(), UtilsBitmap.getBitmapOption());
                        cmdOnSetPicture(bitmap, FILE_PATH + File.separator + FILE_NAME);
                    } catch (Exception e) {
                        showLog("---------FILE_PATH----------" + e.getMessage());
                    }
                    break;
            }
        }
    }

    public static void initToolsBar(final Activity context, final boolean isTrue) {
        if (context == null) {
            return;
        }

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Window window = context.getWindow();

                    // clear FLAG_TRANSLUCENT_STATUS flag:
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                    // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                    // finally change the color
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (isTrue) {
                            window.setStatusBarColor(context.getResources().getColor(R.color.orange));
                        } else {
                            window.setStatusBarColor(context.getResources().getColor(android.R.color.transparent));
                        }
                    }
                }
            }
        });
    }
}