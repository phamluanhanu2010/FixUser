package com.strategy.intecom.vtc.fixuser.service;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;
import com.strategy.intecom.vtc.fixuser.view.base.AppCoreBase;

import java.io.IOException;

/**
 * Created by Mr. Ha on 5/17/16.
 */
public class RegistrationService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public RegistrationService() {
        super("RegistrationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID myID = InstanceID.getInstance(this);
        try {
            String registrationToken = myID.getToken(this.getResources().getString(R.string.gcm_sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            AppCore.getPreferenceUtil(this).setValueString(PreferenceUtil.DEVICE_ID, registrationToken);
            AppCoreBase.showLog("--------- : " + registrationToken);
        } catch (IOException e) {
            e.printStackTrace();
            AppCoreBase.showLog("-IOException-------- : " + e.getMessage());
        }
    }
}
