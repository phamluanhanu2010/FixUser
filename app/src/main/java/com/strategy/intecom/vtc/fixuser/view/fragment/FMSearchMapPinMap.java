package com.strategy.intecom.vtc.fixuser.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.model.VtcModelAddress;
import com.strategy.intecom.vtc.fixuser.utils.Logger;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.utils.map.GPSTracker;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;


/**
 * Created by Mr. Ha on 7/27/16.
 */
public class FMSearchMapPinMap extends AppCore {

    public static String TAG = FMSearchMapPinMap.class.getName();
    private View viewRoot;

    private Button btn_ok;

    private Callback callback;

    private ImageView btn_back;
    private TextView tv_title;

    private ImageView btnMyLocation;

//    private boolean isTrue = Boolean.FALSE;

    private VtcModelAddress vtcModelAddress;

    private Bundle savedInstanceState = null;

    public FMSearchMapPinMap() {

    }

    @Override
    public void onStart() {
        super.onStart();
        onMapInitializer(getActivity());
    }


    @Override
    public void onResume() {
        super.onResume();
        onMapResume();
        if (getCurrentActivity().isRefreshConnection()) {
            initMap(savedInstanceState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onMapDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        onMapLowMemory();
    }

    @SuppressLint("ValidFragment")
    public FMSearchMapPinMap(Callback callback) {
        this.callback = callback;
    }

    public void setVtcModelAddress(VtcModelAddress vtcModelAddress) {
        AppCore.showLog("-----vtcModelAddress------------ : ");
        this.vtcModelAddress = vtcModelAddress;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.ui_pin_map, container, false);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (!getGpsTracker(getCurrentActivity()).isConnection() && Utils.isNetworkConnected(getCurrentActivity())) {
            initShowMessageConfirmGPS(getCurrentActivity());
            this.savedInstanceState = savedInstanceState;
        } else {
            initMap(savedInstanceState);
        }

        initControler(view);

    }
    private void initMap(@Nullable Bundle savedInstanceState) {
        Logger.d(TAG, this, "initMap");
        GPSTracker gpsTracker = getGpsTracker(getCurrentActivity());

        double latitude = 0.0f;
        double longitude = 0.0f;

        if(vtcModelAddress == null){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        }else {
            latitude = vtcModelAddress.getLatitude();
            longitude = vtcModelAddress.getLongitude();

        }
        onMapinitMapView(viewRoot, savedInstanceState, getActivity(), new double[]{latitude, longitude});

        initActionPinEvent();
    }

    private void initControler(View view) {

        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);
        btnMyLocation = (ImageView) view.findViewById(R.id.btn_my_location);
        btnMyLocation.setSelected(true);
        tv_title.setSelected(true);

        if(vtcModelAddress != null) {
            tv_title.setText(vtcModelAddress.getAddress());
        }

        initEvent();

    }

    private void initEvent() {

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onHandlerCallBack(3, vtcModelAddress);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBack();
            }
        });

        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMyLocation();
            }
        });
    }

    @Override
    protected void cmdOnGetLocation(final LatLng destPosition) {
        super.cmdOnGetLocation(destPosition);

        new ReadAddressTask().execute(destPosition);
    }

    private class ReadAddressTask extends AsyncTask<LatLng, Void, VtcModelAddress> {

        @Override
        protected void onPreExecute() {
            Activity activity = getActivity();
            if (activity == null) {
                activity = getCurrentActivity();
            }
            if (activity == null) {
                return;
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (btn_ok != null) {
                        btn_ok.setEnabled(false);
                        btn_ok.setAlpha(0.8f);
                    }
                }
            });
            super.onPreExecute();
        }

        @Override
        protected VtcModelAddress doInBackground(LatLng... sourcePosition) {
            return getAddressFromLocation(getActivity(), sourcePosition[0]);
        }

        @Override
        protected void onPostExecute(final VtcModelAddress result) {
            super.onPostExecute(result);
            if (result == null) {
                return;
            }
            Activity activity = getActivity();
            if (activity == null) {
                activity = getCurrentActivity();
            }
            if (activity == null) {
                return;
            }
            vtcModelAddress = result;

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    if (isTrue) {
//                        AppCore.initSettitle(Const.TYPE_MENU_NOMAL, vtcModelAddress.getAddress());
//                    }

                    tv_title.setText(vtcModelAddress.getAddress());

                    if (btn_ok != null) {
                        btn_ok.setEnabled(true);
                        btn_ok.setAlpha(1.0f);
                        if (callback != null) {
                            callback.onHandlerCallBack(2, vtcModelAddress);
                        }
                    }
//                    isTrue = Boolean.TRUE;
                }
            });
        }
    }
}
