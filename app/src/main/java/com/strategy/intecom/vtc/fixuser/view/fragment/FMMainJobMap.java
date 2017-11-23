package com.strategy.intecom.vtc.fixuser.view.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.strategy.intecom.vtc.fixuser.MainScreen;
import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.model.VtcModelAddress;
import com.strategy.intecom.vtc.fixuser.model.VtcModelAgencyNearBy;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNewOrder;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.ParserJson;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.utils.map.GPSTracker;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog.TYPE_SHOW_MESSAGE_INFO;

/**
 * Created by Mr. Ha on 5/19/16.
 */
public class FMMainJobMap extends AppCore implements View.OnClickListener, Callback {
    /**
     * Show only one item for create new order
     */
    public static final int STATE_SIMPLE = 0;

    /**
     * Show detail content for an order
     */
    public static final int STATE_DETAIL = 1;

    public static final String KEY_NEW_ORDER_FILTER = "new_order";

    private View viewRoot;

    private TextView btnType;
    private TextView btnTime;
    private TextView btnVoucher;
    private TextView btnNote;
    private Button btnSearch;
    private ImageView btn_home;

    private TextView tv_title_field;
    private TextView tv_title_price;
    private LinearLayout lout_container_field;

    private LinearLayout lout_container_search;
    private LinearLayout lout_container_button_option;

    private TextView tv_address;

    private ImageView btnMyLocation;

    private VtcModelNewOrder vtcModelNewOrder;

    private int couponValue = 0;
    private boolean isCouponSet = false;

    private int mFragmentState = STATE_SIMPLE;
    // This param use to decide to create new order immediately or not
    private boolean isOnBoot = false;

    private IntentFilter createOrderFilter;
    private BroadcastReceiver newOrderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getCurrentActivity().getSupportFragmentManager().popBackStack(null, FragmentManager
                    .POP_BACK_STACK_INCLUSIVE);
            createNewOrder();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        savedInstanceState = getArguments();
        if(savedInstanceState != null){
            isOnBoot = savedInstanceState.getBoolean(Const.KEY_BUNDLE_ACTION_ON_BOOT);
        }
        viewRoot = inflater.inflate(R.layout.ui_main_job_map, container, false);
        return viewRoot;
    }

    public FMMainJobMap() {
        throw new IllegalArgumentException("You need to use FMMainJobMap(FragmentState)");
    }

    /**
     * Open new Fragment with detail or simple
     * @param state the state of fragment with value STATE_SIMPLE and STATE_DETAIL
     */
    @SuppressLint("ValidFragment")
    public FMMainJobMap(int state) {
        mFragmentState = state;
    }

    @Override
    public void onStart() {
        super.onStart();
        onMapInitializer(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vtcModelNewOrder = new VtcModelNewOrder();

        if (!getGpsTracker(getCurrentActivity()).isConnection() && Utils.isNetworkConnected(getCurrentActivity())) {
            initShowMessageConfirmGPS(getCurrentActivity());
        } else {
            GPSTracker gpsTracker = getGpsTracker(getCurrentActivity());
            onMapinitMapView(view, savedInstanceState, getCurrentActivity(), new double[]{gpsTracker.getLatitude(),
                    gpsTracker.getLongitude()}, getCurrentActivity().getResources().getString(R.string
                    .title_my_location), true);
            initGetNearBy();
        }

        initController(view);


        createOrderFilter = new IntentFilter(KEY_NEW_ORDER_FILTER);
        LocalBroadcastManager.getInstance(getCurrentActivity()).registerReceiver(newOrderReceiver , createOrderFilter);
    }

    private void initGetNearBy() {
        GPSTracker gpsTracker = getGpsTracker(getCurrentActivity());
        initSocketConnecting(FMMainJobMap.this);
        initGetNearbyLst(FMMainJobMap.this, String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude()), "");

        VtcModelAddress vtcModelAddress = getAddressFromLocation(getActivity(), new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()));
        if (vtcModelAddress != null) {
            if (vtcModelNewOrder == null) {
                vtcModelNewOrder = new VtcModelNewOrder();
            }

            vtcModelNewOrder.setAddname(vtcModelAddress.getAddress());
            vtcModelNewOrder.setAddlat(String.valueOf(vtcModelAddress.getLatitude()));
            vtcModelNewOrder.setAddlong(String.valueOf(vtcModelAddress.getLongitude()));
        }

    }


    private void initController(View view) {
        btn_home = (ImageView) view.findViewById(R.id.btn_home);
        lout_container_search = (LinearLayout) view.findViewById(R.id.lout_container_search);
        lout_container_button_option = (LinearLayout) view.findViewById(R.id.lout_container_button_option);
        btnType = (TextView) view.findViewById(R.id.btn_type);
        btnTime = (TextView) view.findViewById(R.id.btn_Time);
        btnVoucher = (TextView) view.findViewById(R.id.btn_Voucher);
        btnNote = (TextView) view.findViewById(R.id.btn_Note);
        lout_container_field = (LinearLayout) view.findViewById(R.id.lout_container_field);
        tv_title_field = (TextView) view.findViewById(R.id.tv_title_field);
        tv_title_price = (TextView) view.findViewById(R.id.tv_title_price);

        btnSearch = (Button) view.findViewById(R.id.btn_Search);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        btn_home.setOnClickListener(this);
        btnMyLocation = (ImageView) view.findViewById(R.id.btn_my_location);
        btnMyLocation.setSelected(true);

        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMyLocation();
                reDrawMarker();
                initGetNearBy();
            }
        });

        switch (mFragmentState) {
            case STATE_SIMPLE:
                initSimple();
                break;
            case STATE_DETAIL:
                initDetail();
                break;
        }
    }

    private void initSimple() {
        btnType.setVisibility(View.INVISIBLE);
        lout_container_search.setVisibility(View.INVISIBLE);
        lout_container_button_option.setVisibility(View.INVISIBLE);
        btnNote.setVisibility(View.INVISIBLE);
        btnSearch.setBackgroundResource(R.drawable.icon_book_disable);
        tv_title_field.setText(R.string.btn_TranInfo_create_order);
        tv_title_price.setVisibility(TextView.GONE);
        lout_container_field.setOnClickListener(this);
        btnSearch.setOnClickListener(null);
    }

    private void initDetail() {
        btnType.setVisibility(View.VISIBLE);
        lout_container_search.setVisibility(View.VISIBLE);
        lout_container_button_option.setVisibility(View.VISIBLE);
        btnNote.setVisibility(View.VISIBLE);
        btnSearch.setBackgroundResource(R.drawable.icon_book);
        tv_title_field.setText(R.string.btn_TranInfo_Fieldspicker);

        btnType.setSelected(true);
        btnTime.setSelected(true);
        btnVoucher.setSelected(true);
        btnNote.setSelected(true);

        tv_address.setSelected(true);
        tv_address.setOnClickListener(this);

        initEvent();
    }

    private void initEvent() {
        lout_container_search.setOnClickListener(this);
        btnType.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnVoucher.setOnClickListener(this);
        btnNote.setOnClickListener(this);
        lout_container_field.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isOnBoot) {
            isOnBoot = false;
            Bundle bundle = new Bundle();
            bundle.putParcelable(Const.KEY_BUNDLE_ACTION_NEW_ORDER, vtcModelNewOrder);
            AppCore.CallFragmentSectionWithCallback(Const.UI_CREATE_ORDER, bundle, this);
        }
        if (getCurrentActivity().isRefreshConnection()) {
            GPSTracker gpsTracker = getGpsTracker(getCurrentActivity());

            showLog("FMMainJobMap onResume " + gpsTracker.isConnection());

            onMapinitMapView(getView(), null, getCurrentActivity(), new double[]{gpsTracker.getLatitude(),
                    gpsTracker.getLongitude()}, getCurrentActivity().getResources().getString(R.string
                    .title_my_location), true);
            initMap(new double[]{gpsTracker.getLatitude(), gpsTracker.getLongitude()}, getCurrentActivity()
                    .getResources().getString(R.string.title_my_location), true);
            initGetNearBy();
        }
        onMapResume();
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

    @Override
    public void cmdPressDialogYes(TypeShowDialog typeShowDialog, String str) {
        super.cmdPressDialogYes(typeShowDialog, str);

        int key = typeShowDialog.getValuesTypeDialog();

        switch (TypeShowDialog.forValue(key)) {
            case TYPE_SHOW_MESSAGE_INFO_NOTE:

                if(vtcModelNewOrder == null) {
                    vtcModelNewOrder = new VtcModelNewOrder();
                }
                vtcModelNewOrder.setDescription(str);

                if(btnNote != null){
                    btnNote.setText(str);
                }
                break;

            case TYPE_SHOW_MESSAGE_INFO_PAY_TYPE:
                if(vtcModelNewOrder == null) {
                    vtcModelNewOrder = new VtcModelNewOrder();
                }
                vtcModelNewOrder.setPaytype(str);

                if (btnType != null) {
                    btnType.setText(str);
                }
                break;

            case TYPE_SHOW_MESSAGE_INFO_PROMOTION:

                if(vtcModelNewOrder == null) {
                    vtcModelNewOrder = new VtcModelNewOrder();
                }

                vtcModelNewOrder.setCoupon_code(str);
                if(btnVoucher != null){
                    btnVoucher.setText(str);
                }

                initCheckCounpon(FMMainJobMap.this, str);
                break;
            case TYPE_SHOW_MESSAGE_CANCEL:
                initEmptyData();
                initSimple();
                mFragmentState = STATE_SIMPLE;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_home:
                MainScreen.initMenu(getActivity());
                break;
            case R.id.tv_address:
            case R.id.lout_container_search:
                AppCore.CallFragmentSectionWithCallback(Const.UI_PIN_MAP, new Callback() {
                    @Override
                    public <T> void onHandlerCallBack(int key, T... t) {

                        if(t != null && t.length > 0 && t[0] instanceof VtcModelAddress){

                            VtcModelAddress vtcModelAddress = (VtcModelAddress) t[0];

                            if(vtcModelNewOrder == null){
                                vtcModelNewOrder = new VtcModelNewOrder();
                            }

                            vtcModelNewOrder.setAddname(vtcModelAddress.getAddress());
                            vtcModelNewOrder.setAddlat(String.valueOf(vtcModelAddress.getLatitude()));
                            vtcModelNewOrder.setAddlong(String.valueOf(vtcModelAddress.getLongitude()));

                            tv_address.setText(vtcModelAddress.getAddress());
                        }
                    }
                });
                break;
            case R.id.btn_type:
                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO_PAY_TYPE);
                break;
            case R.id.btn_Time:
                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO_TIME);
                break;
            case R.id.btn_Voucher:
                int price = Integer.valueOf(vtcModelNewOrder.getPrice());
                if (price > 0) {
                    String strPromo = "";

                    if (vtcModelNewOrder != null) {
                        strPromo = vtcModelNewOrder.getCoupon_code();
                    }

                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO_PROMOTION, "", strPromo);
                } else {
                    initShowDialogOption(getActivity(), TYPE_SHOW_MESSAGE_INFO, "", getResources().getString(R.string
                            .msg_no_need_voucher));
                }
                break;
            case R.id.btn_Note:

                String strNote = "";

                if (vtcModelNewOrder != null) {
                    strNote = vtcModelNewOrder.getDescription();
                }

                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO_NOTE, "", strNote);

                break;
            case R.id.lout_container_field:
                createNewOrder();
                break;
            case R.id.btn_Search:

                int message = validateData();
                if (message == 0) {

                    if (vtcModelNewOrder != null) {
                        if (isCouponSet) {
                            // set the order price. The real price = estimated price - coupon
                            vtcModelNewOrder.setPrice(String.valueOf(Integer.valueOf(vtcModelNewOrder.getPrice()) - couponValue));
                        }


                        if (vtcModelNewOrder.getType().equals(VtcModelNewOrder.TYPE_BOOKING_NORMAL)) {

                            initCreateOrder(FMMainJobMap.this, vtcModelNewOrder, getVtcModelUser(), true);
                        } else {
                            vtcModelNewOrder.setAddname(getTextAddressView());
                            vtcModelNewOrder.setDescription(getBtnNote().toLowerCase());

                            vtcModelNewOrder.setOrder_time(Utils.initGetDateDefault(null));

                            Bundle bundSearchPre = new Bundle();
                            bundSearchPre.putParcelable(Const.KEY_BUNDLE_ACTION_NEW_ORDER, vtcModelNewOrder);
                            AppCore.CallFragmentSectionWithCallback(Const.UI_REPAIRER_SEARCH, bundSearchPre, new Callback() {
                                @Override
                                public <T> void onHandlerCallBack(int key, T... t) {
                                    if (key == FMRepairerSearch.CLOSE_DONE) {
                                        initEmptyData();
                                        mFragmentState = STATE_SIMPLE;
                                    } else if (key == FMRepairerSearch.CLOSE_CHANGE_TIME) {
                                        btnTime.performClick();
                                    }

                                }
                            }, true, true);
                        }
                    }
                } else {
                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getActivity().getResources().getString(message));
                }

                break;
        }
    }

    private void createNewOrder() {
        Bundle bundle = new Bundle();
        if (vtcModelNewOrder != null) {
            bundle.putParcelable(Const.KEY_BUNDLE_ACTION_NEW_ORDER, vtcModelNewOrder);
        }
        // open FMCreateOrder
        AppCore.CallFragmentSectionWithCallback(Const.UI_CREATE_ORDER,bundle, this);
    }

    private void initEmptyData(){
        vtcModelNewOrder = null;
        vtcModelNewOrder = new VtcModelNewOrder();

        btnTime.setText(getResources().getString(R.string.btn_TranInfo_Now));
        btnVoucher.setText("");
        tv_title_price.setText("");
        btnNote.setText("");
        tv_address.setText("");
        tv_title_price.setVisibility(TextView.GONE);
        tv_title_field.setText(getResources().getString(R.string.btn_TranInfo_Fieldspicker));
        initGetNearBy();
        initSimple();
    }

    private void updatePriceText() {
        try {
            tv_title_price.setText(Html.fromHtml(getCurrentActivity().getResources().getString(R.string.title_info_price,
                    Utils.initTextBold(Utils.formatDecimal((isCouponSet) ? (Float.valueOf(vtcModelNewOrder.getPrice()) -
                            Float.valueOf(couponValue)) : Float.valueOf(vtcModelNewOrder.getPrice()))))));
            tv_title_price.setVisibility(TextView.VISIBLE);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            tv_title_price.setText(Html.fromHtml(getCurrentActivity().getResources().getString(R.string
                    .title_info_price,
                    0)));
            tv_title_price.setVisibility(TextView.VISIBLE);
        }
    }

    private int validateData(){
        if(vtcModelNewOrder == null){
            return R.string.validate_input_new_order;
        }else if(getTextAddressView().equals("")){
            return R.string.btn_TranInfo_Search_Hint;
        } else if (vtcModelNewOrder.getFieldId().equals("")) {
            return R.string.btn_TranInfo_Fieldspicker;
        }

        return 0;
    }

    public String getTextAddressView() {
        if(tv_address == null){
            return "";
        }
        return tv_address.getText().toString().trim();
    }

    public String getBtnNote() {
        if(btnNote == null){
            return "";
        }
        return btnNote.getText().toString().trim();
    }

    @Override
    protected void cmdOnSelectTypeOrder(String type, String strDisplay, String strSend) {
        super.cmdOnSelectTypeOrder(type, strDisplay, strSend);
        if(vtcModelNewOrder == null) {
            vtcModelNewOrder = new VtcModelNewOrder();
        }
        vtcModelNewOrder.setType(type);
        vtcModelNewOrder.setOrder_time(strSend);

        if(btnTime != null){
            btnTime.setText(strDisplay);
        }
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);

        switch (keyType){
            case TYPE_ACTION_GET_LIST_NEARBY:
                initViewNearBy(response);
                break;

            case TYPE_ACTION_GET_CREATE_ORDER:
                if(vtcModelNewOrder != null) {
                    initShowDialogOption(getCurrentActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "",
                            getCurrentActivity().getResources().getString(R.string.title_message_create_job_success,
                                    vtcModelNewOrder.getOrderTimeString(), getTextAddressView()));
                }

                initEmptyData();
                mFragmentState = STATE_SIMPLE;
                break;
            case TYPE_ACTION_CHECK_COUPON:
                boolean result = false;
                try {
                    JSONObject respondData = new JSONObject(response);
                    result = respondData.getBoolean(ParserJson.API_PARAMETER_COUPON_RESPONSE);
                    couponValue = respondData.getInt(ParserJson.API_PARAMETER_VALUE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result) {
                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", message);
                    isCouponSet = true;

                    updatePriceText();
                } else {
                    if (vtcModelNewOrder == null) {
                        vtcModelNewOrder = new VtcModelNewOrder();
                    }

                    vtcModelNewOrder.setCoupon_code("");
                    if (btnVoucher != null) {
                        btnVoucher.setText("");
                    }

                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", message);
                }
                break;
        }

    }

    @Override
    public void onPostExecuteError(TypeActionConnection keyTypeAction, TypeErrorConnection keyType, String msg) {
        super.onPostExecuteError(keyTypeAction, keyType, msg);
        switch (keyTypeAction){
            case TYPE_ACTION_CHECK_COUPON:

                if(vtcModelNewOrder == null) {
                    vtcModelNewOrder = new VtcModelNewOrder();
                }

                vtcModelNewOrder.setCoupon_code("");
                if(btnVoucher != null){
                    btnVoucher.setText("");
                }

                break;
        }
    }

    public static void initViewNearBy(String response) {
        List<VtcModelAgencyNearBy> lst = VtcModelAgencyNearBy.getDataJson(response);
        if (lst != null && lst.size() > 0) {
            for (VtcModelAgencyNearBy model : lst)
            showFromNearBy(model);
        }
    }

    @Override
    public <T> void onHandlerCallBack(int key, T... t) {
        mFragmentState = STATE_DETAIL;
        initDetail();
        vtcModelNewOrder = (VtcModelNewOrder) t[0];
        initData();
    }

    private void initData() {
        tv_address.setText(vtcModelNewOrder.getAddname());
        btnType.setText(vtcModelNewOrder.getPaytype());
        if (vtcModelNewOrder.getType() == VtcModelNewOrder.TYPE_BOOKING_FAST) {
            btnTime.setText(R.string.btn_TranInfo_Now);
        } else {
            btnTime.setText(vtcModelNewOrder.getOrderTimeString());
        }
        btnVoucher.setText(vtcModelNewOrder.getCoupon_code());
        btnNote.setText(vtcModelNewOrder.getDescription());
        tv_title_field.setText(Html.fromHtml(Utils.initTextBold(String.valueOf(vtcModelNewOrder.getName()) + " - " + vtcModelNewOrder.getFieldChildName())));
        updatePriceText();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getCurrentActivity()).unregisterReceiver(newOrderReceiver);
    }
}