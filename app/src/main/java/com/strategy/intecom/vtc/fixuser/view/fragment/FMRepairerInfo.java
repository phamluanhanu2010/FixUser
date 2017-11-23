package com.strategy.intecom.vtc.fixuser.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.adt.adtnormal.AdtPlaceSlidesImage;
import com.strategy.intecom.vtc.fixuser.enums.StatusBookingJob;
import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNoti;
import com.strategy.intecom.vtc.fixuser.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.Logger;
import com.strategy.intecom.vtc.fixuser.utils.ParserJson;
import com.strategy.intecom.vtc.fixuser.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.utils.map.GPSTracker;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad.MediaAsync;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr. Ha on 5/31/16.
 */

public class FMRepairerInfo extends AppCore implements SlidingUpPanelLayout.PanelSlideListener,
        ViewPager.OnPageChangeListener, View.OnClickListener, Callback {
    private static final String TAG = FMRepairerInfo.class.getSimpleName();

    private View viewRoot;

    private ImageView btn_back;
    private SlidingUpPanelLayout sliding_layout;
    private RelativeLayout lout_container_content;
    private RelativeLayout lout_container_title;
    private LinearLayout lout_container_view_image;
    private LinearLayout lout_container_view_feedback;
    private View lout_Repairer_Infor;
    private TextView tv_title_toet_voi;

    private ImageView img_phone;
    private ImageView img_message;
    private ImageView img_home;
    private ImageView img_avatar;

    private TextView tv_Repairer_Name;
    private TextView tv_address;
    private TextView tv_Confirm_Price;
    private TextView tv_money;
    private TextView tv_danhmuc;
    private TextView tv_Danhmuc_Detail;
    private TextView tv_description;
    private TextView tv_distance;
    private TextView tv_title_message;

    private TextView tv_Rank;
    private ImageView img_Rank_Icon;

    private EditText edt_feeback;
    private RatingBar ratingBar;

    private TextView tv_description_app;

    private Button btn_cancel;

    private AdtPlaceSlidesImage mAdapter;
    private ViewPager mPager;
    private PageIndicator mIndicator;

    private int idActionDialog = 0;

    private VtcModelOrder vtcModelOrder;

    private VtcModelNoti vtcModelNoti;

    private Callback callback;

    private boolean isOnlyView;

    @Override
    public void onStart() {
        super.onStart();
        onMapInitializer(getActivity());

    }

    public FMRepairerInfo() {

    }

    @SuppressLint("ValidFragment")
    public FMRepairerInfo(Callback callback) {
        this.callback = callback;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            vtcModelOrder = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_NEW_ORDER);
            vtcModelNoti = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_NOTI);
            isOnlyView = savedInstanceState.getBoolean(Const.KEY_BUNDLE_ACTION_VIEW, false);
        }
        viewRoot = inflater.inflate(R.layout.ui_repairer_info, container, false);

        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VtcModelOrder.Address objAddress;
        double longt = 0.0f;
        double lat = 0.0f;
        boolean isShow = Boolean.FALSE;

        if (vtcModelOrder != null) {
            if (vtcModelOrder.getAgency() != null && !vtcModelOrder.getAgency().getId().equals(PreferenceUtil
                    .IS_KEEP_WORKING) && !isOnlyView) {
                Logger.d(TAG, this, "show noti when model order not null");
                initShowDialogOption(getCurrentActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO_REPAIRER, "",
                        vtcModelOrder);
            }
        } else {
            if (vtcModelNoti != null) {
                try {
                    JSONObject jsonObject = new JSONObject(vtcModelNoti.getResponseData());

                    vtcModelOrder = VtcModelOrder.getOrderData(jsonObject);

                    isShow = Boolean.TRUE;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (vtcModelOrder != null && vtcModelOrder.getAddress() != null) {
            objAddress = vtcModelOrder.getAddress();
            try {
                lat = Double.parseDouble(objAddress.getSlat());
                longt = Double.parseDouble(objAddress.getSlong());

            } catch (NumberFormatException e) {
            }

            onMapinitMapView(view, savedInstanceState, getActivity(), new double[]{lat, longt});

            if (isShow) {
                initAddMarker(new double[]{lat, longt}, objAddress.getName(), "");
            }
        }

        initController(view);
    }

    private void initController(View view) {

        tv_title_toet_voi = (TextView) view.findViewById(R.id.tv_title_toet_voi);
        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        sliding_layout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);

        lout_container_content = (RelativeLayout) sliding_layout.findViewById(R.id.lout_container_content);
        lout_container_title = (RelativeLayout) sliding_layout.findViewById(R.id.lout_container_title);
        lout_container_view_image = (LinearLayout) sliding_layout.findViewById(R.id.lout_container_view_image);
        lout_container_view_feedback = (LinearLayout) sliding_layout.findViewById(R.id.lout_container_view_feedback);
        lout_Repairer_Infor = sliding_layout.findViewById(R.id.lout_Repairer_Infor);

        tv_Rank = (TextView) sliding_layout.findViewById(R.id.tv_Rank);
        img_Rank_Icon = (ImageView) sliding_layout.findViewById(R.id.img_Rank_Icon);

        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        mPager = (ViewPager) view.findViewById(R.id.pager);

        img_phone = (ImageView) sliding_layout.findViewById(R.id.img_phone);
        img_message = (ImageView) sliding_layout.findViewById(R.id.img_message);
        img_home = (ImageView) sliding_layout.findViewById(R.id.img_home);
        edt_feeback = (EditText) sliding_layout.findViewById(R.id.edt_feeback);
        ratingBar = (RatingBar) sliding_layout.findViewById(R.id.ratingBar);
        tv_title_message = (TextView) sliding_layout.findViewById(R.id.tv_title_message);

        tv_description_app = (TextView) sliding_layout.findViewById(R.id.tv_description_app);

        img_avatar = (ImageView) sliding_layout.findViewById(R.id.img_avatar);

        tv_Repairer_Name = (TextView) sliding_layout.findViewById(R.id.tv_Repairer_Name);
        tv_address = (TextView) sliding_layout.findViewById(R.id.tv_address);
        tv_Confirm_Price = (TextView) sliding_layout.findViewById(R.id.tv_Confirm_Price);
        tv_money = (TextView) sliding_layout.findViewById(R.id.tv_money);
        tv_danhmuc = (TextView) sliding_layout.findViewById(R.id.tv_danhmuc);
        tv_Danhmuc_Detail = (TextView) sliding_layout.findViewById(R.id.tv_Danhmuc_Detail);
        tv_description = (TextView) sliding_layout.findViewById(R.id.tv_description);

        btn_cancel = (Button) sliding_layout.findViewById(R.id.btn_cancel);

        initEvent();
    }

    private void initEvent() {

        lout_container_title.setSelected(true);
        btn_back.setOnClickListener(this);
        sliding_layout.addPanelSlideListener(this);
        mIndicator.setOnPageChangeListener(this);
        img_phone.setOnClickListener(this);
        img_message.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        ratingBar.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View view, MotionEvent event)
            {
                float touchPositionX = event.getX();
                float width = ratingBar.getWidth();
                float starsf = (touchPositionX / width) * 5.0f;
                int stars = (int)starsf + 1;
                ratingBar.setRating(stars);
                return true;
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                initRatingStatus((int) rating);
            }
        });

        initData();
    }


    private void initRatingStatus(final int rating){
        Activity activity = getCurrentActivity();

        if (activity == null) {
            activity = getActivity();
        }

        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    int message = 0;

                    switch (rating){

                        case 1:
                            message = R.string.title_rating_review_1;
                            break;
                        case 2:
                            message = R.string.title_rating_review_2;
                            break;
                        case 3:
                            message = R.string.title_rating_review_3;
                            break;
                        case 4:
                            message = R.string.title_rating_review_4;
                            break;
                        case 5:
                            message = R.string.title_rating_review_5;
                            break;
                    }

                    tv_title_toet_voi.setText(getResources().getString(message));
                }
            });
        }
    }

    private void initData() {

        int width[] = Utils.getSizeScreen(getActivity());

        if (vtcModelOrder != null) {

            initAvatar(img_home, width);

            initAvatar(img_avatar, width);

            if (vtcModelOrder.getAgency() == null) {
                Logger.d(TAG, this, "getAgency = null");
                lout_Repairer_Infor.setVisibility(View.GONE);
                tv_description_app.setText("");
            } else {
                Logger.d(TAG, this, "getAgency.name = " + vtcModelOrder.getAgency().getName());
                tv_Repairer_Name.setText(vtcModelOrder.getAgency().getName());
                tv_description_app.setText(vtcModelOrder.getAgency().getName());
            }

            String strPrice = vtcModelOrder.getField() == null ? "0.0" : vtcModelOrder.getField().getPrice();

            AppCore.setFormatCurrency(tv_Confirm_Price, strPrice);

            tv_money.setText(vtcModelOrder.getPayment_method());
            tv_danhmuc.setText(vtcModelOrder.getField() == null ? "" : vtcModelOrder.getField().getCategory_name());
            tv_Danhmuc_Detail.setText(vtcModelOrder.getField() == null ? "" : vtcModelOrder.getField().getName());
            tv_description.setText((TextUtils.isEmpty(vtcModelOrder.getDescription())) ? "" : getResources().getString(R.string.title_info_new_book_note, vtcModelOrder.getDescription()));
            tv_address.setText(Html.fromHtml(getCurrentActivity().getResources().getString(R.string.title_info_new_book_address, vtcModelOrder.getAddress() == null ? "" : vtcModelOrder.getAddress().getName())));



            if (vtcModelOrder.getImages() != null && vtcModelOrder.getImages().size() > 0) {

                lout_container_view_image.setVisibility(LinearLayout.VISIBLE);

                int offset = (int) getResources().getDimension(R.dimen.confirm_ui_padding_w) * 2;

                mAdapter = new AdtPlaceSlidesImage(getActivity().getSupportFragmentManager(), vtcModelOrder.getImages(), width[0] - offset, (width[0] - offset) / 2);

                mPager.setAdapter(mAdapter);

                mIndicator.setViewPager(mPager);

                ((CirclePageIndicator) mIndicator).setSnap(true);
            } else {
                lout_container_view_image.setVisibility(LinearLayout.GONE);
            }

            if(vtcModelOrder.getImages() == null || vtcModelOrder.getImages().size() <= 0){

                lout_container_view_image.setVisibility(LinearLayout.GONE);
            }else {

                lout_container_view_image.setVisibility(LinearLayout.VISIBLE);
            }

            initStatusAgency(vtcModelOrder.getStatus());
        }

        initRate();
    }

    private boolean initStatusAgency(String status) {

        btn_back.setVisibility(ImageView.VISIBLE);
        btn_cancel.setText(getCurrentActivity().getResources().getString(R.string.btn_cancel_order));
        lout_container_view_feedback.setVisibility(LinearLayout.GONE);
        if (status.equals(StatusBookingJob.STATUS_FINDING.getValuesStatus())) {
            tv_title_message.setText(getActivity().getResources().getString(R.string.title_status_my_order_waiting));
            tv_Repairer_Name.setVisibility(TextView.GONE);
            tv_Rank.setVisibility(TextView.GONE);
            img_Rank_Icon.setVisibility(ImageView.GONE);
            if (sliding_layout != null) {
                sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                sliding_layout.setTouchEnabled(false);
            }
            btn_cancel.setVisibility(Button.VISIBLE);
        } else if (status.equals(StatusBookingJob.STATUS_USER_CANCEL.getValuesStatus())) {
            tv_title_message.setText(getActivity().getResources().getString(R.string.title_status_my_order_user_cancel));
            if (sliding_layout != null) {
                sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                sliding_layout.setTouchEnabled(false);
            }
            btn_cancel.setVisibility(Button.GONE);
        } else if (status.equals(StatusBookingJob.STATUS_AGENCY_CANCEL.getValuesStatus())) {
            tv_title_message.setText(getActivity().getResources().getString(R.string.title_status_my_order_agency_cancel));
            if (sliding_layout != null) {
                sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                sliding_layout.setTouchEnabled(false);
            }
            btn_cancel.setVisibility(Button.GONE);
        } else if (status.equals(StatusBookingJob.STATUS_FINISH.getValuesStatus())) {
            if(vtcModelOrder != null && vtcModelOrder.getAgency() != null && !vtcModelOrder.getAgency().getId().isEmpty()) {

                btn_cancel.setText(getCurrentActivity().getResources().getString(R.string.btn_Accept));
                lout_container_view_feedback.setVisibility(LinearLayout.VISIBLE);

                btn_back.setVisibility(ImageView.GONE);
                tv_title_toet_voi.setText(getResources().getString(R.string.title_rating_review_3));

//                ratingBar.setRating(3);
            }else {
                btn_back.setVisibility(ImageView.VISIBLE);
                btn_cancel.setVisibility(Button.GONE);
            }
            if (sliding_layout != null) {
                sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                sliding_layout.setTouchEnabled(false);
            }
            tv_title_message.setText(getActivity().getResources().getString(R.string.title_status_my_order_finish));
            return true;
        } else if (status.equals(StatusBookingJob.STATUS_EXPIRED.getValuesStatus())) {
            btn_cancel.setVisibility(Button.GONE);
            tv_title_message.setText(getActivity().getResources().getString(R.string.title_status_my_order_ex));
        } else if (status.equals(StatusBookingJob.STATUS_COMING.getValuesStatus())) {
            if (sliding_layout != null) {
                sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            }
            btn_cancel.setVisibility(Button.VISIBLE);
            tv_title_message.setText(getActivity().getResources().getString(R.string.title_status_my_order_coming));
        } else if (status.equals(StatusBookingJob.STATUS_WORKING.getValuesStatus())) {
            if (sliding_layout != null) {
                sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            }
            btn_cancel.setVisibility(Button.VISIBLE);
            tv_title_message.setText(getActivity().getResources().getString(R.string.title_status_my_order_working));
        } else {
            if (sliding_layout != null) {
                sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            }
            btn_cancel.setVisibility(Button.VISIBLE);
            tv_title_message.setText(getActivity().getResources().getString(R.string.title_status_my_order_coming));
        }
        return false;
    }

    private void initRate() {
        if (vtcModelOrder != null && vtcModelOrder.getAgency() != null) {

            String sLV = vtcModelOrder.getAgency().getLevel();
            if (sLV.equals("")) {
                sLV = "0";
            }
            int level = Integer.parseInt(sLV);

            switch (level) {
                case 1:
                    // Thợ cấp 1
                    sLV = getResources().getString(R.string.title_rating_level_1);
                    level = R.drawable.ic_level_1;
                    break;

                case 2:
                    // Thợ cấp 2
                    sLV = getResources().getString(R.string.title_rating_level_2);
                    level = R.drawable.ic_level_2;
                    break;

                case 3:
                    // Thợ cấp 3
                    sLV = getResources().getString(R.string.title_rating_level_3);
                    level = R.drawable.ic_level_3;
                    break;

                case 4:
                    // Thợ cấp 4
                    sLV = getResources().getString(R.string.title_rating_level_4);
                    level = R.drawable.ic_level_4;
                    break;

                case 5:
                    // Thợ cấp 5
                    sLV = getResources().getString(R.string.title_rating_level_5);
                    level = R.drawable.ic_level_5;
                    break;
            }

            tv_Rank.setText(sLV);
            img_Rank_Icon.setBackgroundResource(level);

        }
    }

    private void initAvatar(ImageView img, int[] width) {

        ImageLoadAsync loadAsyncAvatar = new ImageLoadAsync(getActivity(), img, width[0]);
        loadAsyncAvatar.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, String.valueOf(vtcModelOrder.getAgency() == null ? "" : vtcModelOrder.getAgency().getAvatar()));

    }

    @Override
    protected void cmdOnResponseGetLocation(String response) {
        super.cmdOnResponseGetLocation(response);

        try {
            JSONObject jsonObject = new JSONObject(response);

            double lat = jsonObject.optDouble(ParserJson.API_PARAMETER_LATITUDE, 0.0f);
            double longt = jsonObject.optDouble(ParserJson.API_PARAMETER_LONGITUDE, 0.0f);

            int errorCode = jsonObject.optInt(ParserJson.API_PARAMETER_SUCCESS, 0);

            if(errorCode == 1) {
                if (getTimerRequestLocation() != null) {
                    getTimerRequestLocation().cancel();
                    setTimerRequestLocation(null);
                }
                String message = jsonObject.optString(ParserJson.API_PARAMETER_MESSAGE, "");
                initShowDialogOption(getCurrentActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", message);
            }

            initDistance(errorCode, lat, longt);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initDistance(int errorCode, final double lat, final double longt){
        String address = "";

        GPSTracker gpsTracker = getGpsTracker(getCurrentActivity());

        final LatLng from = new LatLng(lat, longt);
        LatLng to = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        if(vtcModelOrder != null && vtcModelOrder.getAddress() != null){

            double order_lat;
            double order_long;

            try {
                order_lat = Double.parseDouble(vtcModelOrder.getAddress().getSlat());
                order_long = Double.parseDouble(vtcModelOrder.getAddress().getSlong());
                if(order_lat != 0 && order_long != 0){
                    to = new LatLng(order_lat, order_long);
                }
            }catch (NumberFormatException e){
            }
        }
        if(errorCode == 0) {

            final String distance = CalculationByDistance(from, to);

            Activity activity = getCurrentActivity();

            if (activity == null) {
                activity = getActivity();
            }

            if (activity != null) {
                address = activity.getResources().getString(R.string.title_repairer_location);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (lat == 0 && longt == 0) {

                            tv_distance.setText("0 Km");
                        } else {

                            tv_distance.setText(distance);
                        }
                    }
                });
            }
        }

        if(vtcModelOrder != null) {
            initShowDistance(from, address, to, vtcModelOrder.getAddress() == null ? "" : vtcModelOrder.getAddress().getName(), "");
        }

    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);

        switch (keyType){
            case TYPE_ACTION_CANCEL_ORDER_MARK:

                if (getTimerRequestLocation() != null) {
                    getTimerRequestLocation().cancel();
                    setTimerRequestLocation(null);
                }

                AppCore.getPreferenceUtil(getCurrentActivity()).setValueString(PreferenceUtil.IS_KEEP_WORKING, "");

                if (sliding_layout != null) {
                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }

                showLogToast(getView(), getResources().getString(R.string.title_status_my_order_finish));

                cmdBack();
                break;
        }
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
        if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            lout_container_content.setVisibility(RelativeLayout.GONE);
            lout_container_title.setVisibility(RelativeLayout.VISIBLE);
        } else {
            lout_container_content.setVisibility(RelativeLayout.VISIBLE);
            lout_container_title.setVisibility(RelativeLayout.GONE);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

        String sPhone = vtcModelOrder == null ? "" : vtcModelOrder.getAgency() == null ? "" : vtcModelOrder.getAgency().getPhone();
        switch (v.getId()) {

            case R.id.img_phone:
                idActionDialog = R.id.img_phone;
                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_CONFIRM, "", "Gọi " + sPhone);
                break;

            case R.id.img_message:
                idActionDialog = R.id.img_message;
                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_CONFIRM, "", "Nhắn tin cho " + sPhone);
                break;

            case R.id.btn_cancel:

                if(vtcModelOrder != null) {

                    if (vtcModelOrder.getStatus().equals(StatusBookingJob.STATUS_FINISH.getValuesStatus())) {

                        if (getVtcModelUser() != null && vtcModelOrder != null) {
                            Logger.d(TAG, this, "ratting bar : " + getRatingBar());
                            if (getRatingBar() > 0) {
                                initRatePepairer(FMRepairerInfo.this, vtcModelOrder.getId(), getRatingBar(), getVtcModelUser().getName(), getEdt_feeback());
                            } else {
                                AppCore.initReceived(Const.TYPE_IS_KEEP_JOB, this.getResources().getString(R.string.validate_keep_job));
                            }
                        }
                    } else {

                        Bundle bundle = new Bundle();
                        bundle.putString(Const.KEY_BUNDLE_ACTION_ID_ORDER, vtcModelOrder.getId());
                        CallFragmentSectionWithCallback(Const.UI_REPAIRER_INFO_CANCEL, bundle, this);
                    }
                }
                break;
            case R.id.btn_back:
                cmdBack();
                break;
        }
    }

    @Override
    public void cmdPressDialogYes(TypeShowDialog typeShowDialog) {
        super.cmdPressDialogYes(typeShowDialog);

        String sPhone = vtcModelOrder == null ? "" : vtcModelOrder.getAgency() == null ? "" : vtcModelOrder.getAgency().getPhone();

        switch (idActionDialog) {

            case R.id.img_phone:
                initCallIntentPhone(sPhone);
                break;

            case R.id.img_message:
                initCallIntentMessage(sPhone, "Body message");
                break;
        }
    }

    public int getRatingBar() {
        if(ratingBar == null){
            return  0;
        }
        return Math.round(ratingBar.getRating());
    }

    public String getEdt_feeback() {
        if(edt_feeback == null){
            return "";
        }
        return edt_feeback.getText().toString().trim();
    }

    @Override
    public void onResume() {
        onMapResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onMapDestroy();

        mAdapter = null;
        mPager = null;
        mIndicator = null;

        if (getTimerRequestLocation() != null) {
            getTimerRequestLocation().cancel();
            setTimerRequestLocation(null);
        }

        if (callback != null) {
            callback.onHandlerCallBack(-1);
        }
        Const.UI_CURRENT_CONTEXT = Const.UI_MAIN_JOB_MAP_SIMPLE;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        onMapLowMemory();
    }

    @Override
    public <T> void onHandlerCallBack(int key, T... t) {
        if (sliding_layout != null) {
            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    @Override
    protected void cmdOnCancelJob() {
        super.cmdOnCancelJob();
        cmdBack();
    }

    @Override
    protected void cmdOnNotifiData(VtcModelOrder vtcModelOrder) {
        super.cmdOnNotifiData(vtcModelOrder);
        this.vtcModelOrder = vtcModelOrder;
        initEvent();
    }
}
