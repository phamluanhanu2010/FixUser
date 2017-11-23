package com.strategy.intecom.vtc.fixuser.view.base;

import android.support.multidex.MultiDexApplication;

import com.deploygate.sdk.DeployGate;


/**
 * Created by Mr. Ha on 5/16/16.
 */
public class Controller extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
//        FacebookSdk.sdkInitialize(this);
        DeployGate.install(this);
    }
}
