package com.strategy.intecom.vtc.fixuser.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;
import com.strategy.intecom.vtc.fixuser.view.base.AppCoreBase;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Created by Mr. Ha on 5/16/16.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    /**
     * Checks if the device has Internet connection.
     *
     * @return <code>true</code> if the phone is connected to the Internet.
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }

    /**
     * Check is internet available or not
     * @return true if internet available
     */
    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");
        } catch (Exception e) {
            Logger.e(TAG, null, "isInternetAvailable error : " + e.getMessage());
            return false;
        }

    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static boolean checkPlayServices(final Activity context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                AppCoreBase.showLog(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    public static void getHashKey(Context context) {

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;

                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                AppCoreBase.showLog("KeyHash ----- : ", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            AppCoreBase.showLog(" ------ name not found", e1.toString());
        }

        catch (NoSuchAlgorithmException e) {
            AppCoreBase.showLog(" --------- no such an algorithm " + e.toString());
        } catch (Exception e) {
            AppCoreBase.showLog(" -------- exception" +  e.toString());
        }
    }

    /**
     * Get Size Screen
     */
    public static int[] getSizeScreen(Activity activity){
        int[] screen = new int[2];
        if(activity != null) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            screen[0] = width;
            screen[1] = height;

            int screenSize = activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
            switch (screenSize) {
                case Configuration.SCREENLAYOUT_SIZE_LARGE:
                    AppCore.showLog("Large screen");
                    break;
                case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                    AppCore.showLog("Normal screen");
                    break;
                case Configuration.SCREENLAYOUT_SIZE_SMALL:
                    AppCore.showLog("Small screen");
                    break;
                default:
                    AppCore.showLog("Screen size is neither large, normal or small");
            }
        }
        return screen;
    }

    @SuppressLint("SimpleDateFormat")
    public static String initGetDateDefault(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
        if(date == null) {
            return df.format(new Date());
        }else {
            return df.format(date);
        }
    }

    public static String initConvertTime(String strTime) {

        String formattedDate = "";
        try {
            Locale.setDefault(new Locale("vi", "VN"));
            DateFormat originalFormat = new SimpleDateFormat("EEEE dd:MM:yyyy HH:mm", Locale.getDefault());
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault());
            Date date = originalFormat.parse(strTime);

            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static String initConvertTimeDisplayHistory(String strTime) {

        String formattedDate = "";
        try {
            TimeZone tz = TimeZone.getTimeZone("GMT+14:00");
            Locale.setDefault(new Locale("vi", "VN"));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            DateFormat dateFormat2 = new SimpleDateFormat("EE, HH:mm dd/MM/yyyy", Locale.getDefault());
            Date date = dateFormat.parse(strTime);
            dateFormat2.setTimeZone(tz);
            formattedDate = dateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static String initConvertTimeDisplay(String strTime) {

        String formattedDate = "";
        try {
            Locale.setDefault(new Locale("vi", "VN"));
            DateFormat originalFormat = new SimpleDateFormat("EEEE dd:MM:yyyy HH:mm", Locale.getDefault());
            Date date = originalFormat.parse(strTime);
            DateFormat targetFormat = new SimpleDateFormat("HH:mm dd/MM", Locale.getDefault());

            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static void hideKeyboard(Activity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }

    public static void showKeyboard(Activity activity){
        if(activity == null){
            return;
        }

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } catch (RuntimeException e) {
            AppCore.showLog(TAG + " showKeyboard RuntimeException : " + e.getMessage());
        }
    }

    private static final String phone_patterns = "^0[1-9][0-9]{8,9}";

    public static boolean validatePhoneNumber(String phoneNo) {
        Pattern p = Pattern.compile(phone_patterns);
        return p.matcher(phoneNo).matches();
    }

    public static boolean validateEmail(String email) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        }
        return false;
    }

    public static String formatDecimal(float number) {
        return String.format(Locale.getDefault(), "%,.0f", number);
    }

    public static boolean validateName(String name) {

        Pattern p = Pattern.compile("[ a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ]+");

        return p.matcher(name).matches();
    }

    public static String initTextBold(String str) {
        return String.valueOf("<b>" + str + "</b>");
    }

    public static void call(Context context, String phone){
        String number = "tel:" + phone.trim();
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
        context.startActivity(callIntent);
    }
}
