package com.strategy.intecom.vtc.fixuser.view.base;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.view.UIConfirmChangePass;
import com.strategy.intecom.vtc.fixuser.view.UIConfirmPassCode;
import com.strategy.intecom.vtc.fixuser.view.UIConfirmForgotChangePass;
import com.strategy.intecom.vtc.fixuser.view.UIConfirmSignIn;
import com.strategy.intecom.vtc.fixuser.view.UIEditProfile;
import com.strategy.intecom.vtc.fixuser.view.UIForgotPassword;
import com.strategy.intecom.vtc.fixuser.view.UILogin;
import com.strategy.intecom.vtc.fixuser.view.UIRegister;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMCreateOrder;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMFixFieldsList;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMFixFieldsListDetail;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMMainJobMap;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMNavBookingSchedule;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMNavHistory;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMNotification;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMSearchFixFieldText;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMSearchMap;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMRepairerInfo;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMRepairerInfoCancel;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMRepairerSearch;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMViewWeb;

import static com.strategy.intecom.vtc.fixuser.utils.Const.UI_FIXFIELDS_DETAIL_LIST;

public class VtcFragmentFactory {

    /**
     * All class in app will int below with key_value and return Fragment by
     * name class fragment and CallBack
     **/
    public static Fragment getFragmentWithCallbackByKey(int key, Callback callback) {

        switch (key) {
            case Const.UI_FIXFIELDS_LIST:
                return new FMFixFieldsList(callback);
            case UI_FIXFIELDS_DETAIL_LIST:
                return new FMFixFieldsListDetail(callback);
            case Const.UI_REPAIRER_SEARCH:
                return new FMRepairerSearch(callback);
            case Const.UI_REPAIRER_INFO_CANCEL:
                return new FMRepairerInfoCancel(callback);
            case Const.UI_REPAIRER_INFO:
                return new FMRepairerInfo(callback);
            case Const.UI_NAV_HISTORY:
                return new FMNavHistory(callback);
            case Const.UI_NAV_BOOKINGSCHEDUDE:
                return new FMNavBookingSchedule(callback);
            case Const.UI_NOTIFICATION:
                return new FMNotification(callback);
            case Const.UI_PIN_MAP:
                return new FMSearchMap(callback);
            case Const.UI_CREATE_ORDER:
                return new FMCreateOrder(callback);
            case Const.UI_SEARCH_FIX_FIELD:
                return new FMSearchFixFieldText(callback);
            default:
                return null;
        }

    }

    /**
     * All class in app will int below with key_value and return Fragment by
     * name class fragment
     */
    @Nullable
    public static Fragment getFragmentByKey(int key) {

        switch (key) {
            case Const.UI_MAIN_JOB_MAP_DETAIL:
                return new FMMainJobMap(FMMainJobMap.STATE_DETAIL);
            case Const.UI_MAIN_JOB_MAP_SIMPLE:
                return new FMMainJobMap(FMMainJobMap.STATE_SIMPLE);
            case Const.UI_FIXFIELDS_LIST:
                return new FMFixFieldsList();
            case UI_FIXFIELDS_DETAIL_LIST:
                return new FMFixFieldsListDetail();
            case Const.UI_REPAIRER_SEARCH:
                return new FMRepairerSearch();
            case Const.UI_REPAIRER_INFO_CANCEL:
                return new FMRepairerInfoCancel();
            case Const.UI_REPAIRER_INFO:
                return new FMRepairerInfo();
            case Const.UI_NAV_HISTORY:
                return new FMNavHistory();
            case Const.UI_NAV_BOOKINGSCHEDUDE:
                return new FMNavBookingSchedule();
            case Const.UI_NOTIFICATION:
                return new FMNotification();
            case Const.UI_MY_LOGIN_CONFIRM:
                return new UIConfirmSignIn();
            case Const.UI_MY_LOGIN:
                return new UILogin();
            case Const.UI_MY_CONFIRM_CODE:
                return new UIConfirmPassCode();
            case Const.UI_MY_REGISTER:
                return new UIRegister();
            case Const.UI_PIN_MAP:
                return new FMSearchMap();
            case Const.UI_WEB_VIEW:
                return new FMViewWeb();
            case Const.UI_MY_FORGOT_PASSWORD:
                return new UIForgotPassword();
            case Const.UI_MY_CONFIRM_FORGOT_CHANGE_PASSWORD:
                return new UIConfirmForgotChangePass();
            case Const.UI_MY_CONFIRM_CHANGE_PASSWORD:
                return new UIConfirmChangePass();
            case Const.UI_MY_UPDATE_PROFILE:
                return new UIEditProfile();
            default:
                AppCoreBase.showLog("getFragmentByKey wrong key");
                return null;
        }
    }
}
