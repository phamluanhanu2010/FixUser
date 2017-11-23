package com.strategy.intecom.vtc.fixuser.view.fragment;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.MainScreen;
import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.adt.adtnormal.AdtImageOrder;
import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.model.VtcModelAddress;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNewOrder;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.Logger;
import com.strategy.intecom.vtc.fixuser.utils.ParserJson;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.utils.UtilsBitmap;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import org.json.JSONException;
import org.json.JSONObject;

import static com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog.TYPE_SHOW_ENABLE_NETWORK;
import static com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog.TYPE_SHOW_FILL_ADDRESS;
import static com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog.TYPE_SHOW_FILL_TIME;
import static com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog.TYPE_SHOW_MESSAGE_INFO;


/**
 * A simple {@link Fragment} subclass.
 */
public class FMFillOrderDetail extends AppCore implements View.OnClickListener {
    private static final String TAG = FMFillOrderDetail.class.getSimpleName();

    private Callback mCallBack;

    private VtcModelNewOrder mNewOrder;

    private TextView btnTime;
    private TextView btnType;
    private TextView btnVoucher;
    private TextView btnNote;
    private TextView tv_address;
    private Button imgBtnGallery;

    // Main container to display images
    private GridView glr_image;
    private AdtImageOrder adtImageOrder;
    private MainScreen.OnBackListenner mBackListenner;

    private VtcModelAddress vtcModelAddress;

    public FMFillOrderDetail() {}

    @SuppressLint("ValidFragment")
    public FMFillOrderDetail(Callback callback) {
        mNewOrder = new VtcModelNewOrder();
        mCallBack = callback;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.ui_fill_order_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    /**
     * match all view to the variable and setup onclick listener
     * Update text for title and items
     *
     * @param view the root view which contain all views in this screen
     */
    private void initViews(View view) {
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        btnTime = (TextView) view.findViewById(R.id.btn_Time);
        btnType = (TextView) view.findViewById(R.id.btn_type);
        btnVoucher = (TextView) view.findViewById(R.id.btn_Voucher);
        btnNote = (TextView) view.findViewById(R.id.btn_Note);
        Button imgBtnCamera = (Button) view.findViewById(R.id.imgBtn_Camera);
        ImageView btn_back = (ImageView) view.findViewById(R.id.btn_back);
        glr_image = (GridView) view.findViewById(R.id.glr_image);
        Button btnAccept = (Button) view.findViewById(R.id.btn_Accept);


        imgBtnGallery = (Button) view.findViewById(R.id.imgBtn_Gallery);
        imgBtnGallery.setOnClickListener(this);


        tv_address.setSelected(true);
        tv_address.setOnClickListener(this);

        btnTime.setSelected(true);
        btnTime.setOnClickListener(this);

        btnType.setSelected(true);
        btnType.setOnClickListener(this);

        btnVoucher.setSelected(true);
        btnVoucher.setOnClickListener(this);

        btnNote.setSelected(true);
        btnNote.setOnClickListener(this);


        imgBtnCamera.setOnClickListener(this);

        int width[] = Utils.getSizeScreen(getActivity());
        adtImageOrder = new AdtImageOrder(getActivity(), width[0]);
        glr_image.setAdapter(adtImageOrder);

        adtImageOrder.setOnClickItem(new AdtImageOrder.onClickItem() {
            @Override
            public void onClickItemDelete() {
                initCheckViewImage();
            }
        });

        if (adtImageOrder != null) {
            adtImageOrder.initSetData(mNewOrder.getLstImageUri(), mNewOrder.getLstImageBase64());
        }
        initCheckViewImage();

        btn_back.setOnClickListener(this);

        btnAccept.setOnClickListener(this);

        ((TextView) view.findViewById(R.id.tv_title)).setText(R.string.title_bar_fill_order_detail);
    }

    private void initCheckViewImage() {
        if (adtImageOrder != null) {
            if (adtImageOrder.isEmpty()) {
                glr_image.setVisibility(GridView.GONE);
            } else {
                glr_image.setVisibility(GridView.VISIBLE);
            }
        }
    }

    public void updateData(VtcModelNewOrder newOrder) {
        mNewOrder = newOrder;

        initData();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_address:
            case R.id.lout_container_search:

                Bundle bundle = new Bundle();
                bundle.putParcelable(Const.KEY_BUNDLE_ACTION_ADDRESS, vtcModelAddress);
                AppCore.CallFragmentSectionWithCallback(Const.UI_PIN_MAP, bundle, new Callback() {
                    @Override
                    public <T> void onHandlerCallBack(int key, T... t) {

                        if (t != null && t.length > 0 && t[0] instanceof VtcModelAddress) {

                            vtcModelAddress = (VtcModelAddress) t[0];

                            if (mNewOrder == null) {
                                mNewOrder = new VtcModelNewOrder();
                            }

                            mNewOrder.setAddname(vtcModelAddress.getAddress());
                            mNewOrder.setAddlat(String.valueOf(vtcModelAddress.getLatitude()));
                            mNewOrder.setAddlong(String.valueOf(vtcModelAddress.getLongitude()));

                            tv_address.setText(vtcModelAddress.getAddress());
                        }
                    }
                });
                break;
            case R.id.btn_Time:
                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO_TIME);
                break;
            case R.id.btn_type:
                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO_PAY_TYPE);
                break;
            case R.id.btn_Voucher:
                int price = Integer.valueOf(mNewOrder.getPrice());
                if (price > 0) {
                    Utils.showKeyboard(getActivity());

                    String strPromo = "";

                    if (mNewOrder != null) {
                        strPromo = mNewOrder.getCoupon_code();
                    }

                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO_PROMOTION, "", strPromo);
                } else {
                    initShowDialogOption(getActivity(), TYPE_SHOW_MESSAGE_INFO, "", getResources().getString(R.string.msg_no_need_voucher));
                }
                break;
            case R.id.btn_Note:

                String strNote = "";

                if (mNewOrder != null) {
                    strNote = mNewOrder.getDescription();
                }

                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO_NOTE, "", strNote);

                break;
            case R.id.imgBtn_Camera:
                if (adtImageOrder != null && adtImageOrder.getCount() < 5 && UtilsBitmap.isThanFilesSize500K(adtImageOrder.initGetListmage())) {
                    initGetCamera(Const.REQUEST_CODE_CAM, "images");
                } else {
                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getResources()
                            .getString(R.string.validate_max_image));
                }
                break;
            case R.id.imgBtn_Gallery:
                if (adtImageOrder != null && adtImageOrder.getCount() < 5 && UtilsBitmap.isThanFilesSize500K(adtImageOrder.initGetListmage())) {

                    dispatchPictureLibraryIntent(Const.REQUEST_CODE_LOAD_IMAGE);
                } else {
                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getResources()
                            .getString(R.string.validate_max_image));
                }
                break;
            case R.id.btn_Accept:

                if (mNewOrder != null && adtImageOrder != null) {

                    mNewOrder.setLstImageUri(adtImageOrder.initGetListmage());
                    mNewOrder.setLstImageBase64(adtImageOrder.initGetListmageBase64());
                }

                int message = validateData();
                if (message == 0) {
                    if(mNewOrder.getLstImageBase64().isEmpty()){
//                        initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_CONFIRM, "", getActivity().getResources().getString(R.string.validate_image));

                        initShowCustomAlertDialog(getActivity(), "", getActivity().getResources().getString(R.string
                                        .validate_image), getResources().getString(R
                                        .string.yes)

                                , getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        imgBtnGallery.performClick();
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        initHandlerDataBack();
                                    }
                                });
                    }else {
                        initHandlerDataBack();
                    }
                } else {
                    TypeShowDialog type;
                    switch (message) {
                        case R.string.btn_TranInfo_Search_Hint:
                            type = TYPE_SHOW_FILL_ADDRESS;
                            break;
                        case R.string.btn_TranInfo_choose_time_warning:
                            type = TYPE_SHOW_FILL_TIME;
                            break;
                        default:
                            type = TYPE_SHOW_MESSAGE_INFO;
                            break;
                    }
                    Logger.d(TAG, this, "error TypeShowDialog = " + type);
                    initShowDialogOption(getActivity(), type, "", getActivity().getResources().getString(message));
                }
                break;
            case R.id.btn_back:
                mBackListenner.onBack();
                break;
        }
    }

    @Override
    protected void cmdOnSetPicture(Bitmap bitmap, String strPath) {
        super.cmdOnSetPicture(bitmap, strPath);

        if (adtImageOrder != null) {

            if (!strPath.isEmpty()) {

                bitmap = UtilsBitmap.resizeImage(getActivity(), strPath,
                        strPath, 800, 500);

                String strBase64 = UtilsBitmap.convertImage(bitmap);

                adtImageOrder.initSetData(strPath, strBase64);

                initCheckViewImage();
            }
        }
    }

    @Override
    protected void cmdOnSelectTypeOrder(String type, String strDisplay, String strSend) {
        super.cmdOnSelectTypeOrder(type, strDisplay, strSend);
        if (mNewOrder == null) {
            mNewOrder = new VtcModelNewOrder();
        }
        mNewOrder.setType(type);
        mNewOrder.setOrder_time(strSend);
        mNewOrder.setOrderTimeString(strDisplay);

        if (btnTime != null) {
            btnTime.setText(strDisplay);
        }
    }

    @Override
    public void cmdPressDialogYes(TypeShowDialog dialogType) {
        super.cmdPressDialogYes(dialogType);

        int key = dialogType.getValuesTypeDialog();

        switch (TypeShowDialog.forValue(key)) {
            case TYPE_SHOW_FILL_ADDRESS:
                tv_address.performClick();
                Logger.d(TAG, this, "ok TypeShowDialog = TYPE_SHOW_FILL_ADDRESS");
                break;
            case TYPE_SHOW_FILL_TIME:
                btnTime.performClick();
                Logger.d(TAG, this, "ok TypeShowDialog = TYPE_SHOW_FILL_TIME");
                break;
        }
    }

    @Override
    public void cmdPressDialogYes(TypeShowDialog typeShowDialog, String str) {
        super.cmdPressDialogYes(typeShowDialog, str);

        Utils.hideKeyboard(getActivity());

        int key = typeShowDialog.getValuesTypeDialog();

        switch (TypeShowDialog.forValue(key)) {
            case TYPE_SHOW_MESSAGE_INFO_NOTE:

                if (mNewOrder == null) {
                    mNewOrder = new VtcModelNewOrder();
                }
                mNewOrder.setDescription(str);

                if (btnNote != null) {
                    btnNote.setText(str);
                }
                break;

            case TYPE_SHOW_MESSAGE_INFO_PAY_TYPE:
                if (mNewOrder == null) {
                    mNewOrder = new VtcModelNewOrder();
                }
                mNewOrder.setPaytype(str);

                if (btnType != null) {
                    btnType.setText(str);
                }
                break;

            case TYPE_SHOW_MESSAGE_INFO_PROMOTION:

                if (mNewOrder == null) {
                    mNewOrder = new VtcModelNewOrder();
                }

                mNewOrder.setCoupon_code(str);
                if (btnVoucher != null) {
                    btnVoucher.setText(str);
                }
                if(mNewOrder.getCoupon_code().isEmpty()){
                    initShowDialogOption(getActivity(), TYPE_SHOW_MESSAGE_INFO, "", getResources().getString(R.string.content_hint_Voucher));
                }else {
                    initCheckCounpon(FMFillOrderDetail.this, str);
                }
                break;
            case TYPE_SHOW_MESSAGE_CANCEL:
                cmdBack();
                break;
        }
    }

    @Override
    public void cmdPressDialogNo(TypeShowDialog typeShowDialog) {
        super.cmdPressDialogNo(typeShowDialog);

        initHandlerDataBack();
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);

        switch (keyType) {
            case TYPE_ACTION_CHECK_COUPON:
                boolean result = false;
                try {
                    JSONObject respondData = new JSONObject(response);
                    result = respondData.getBoolean(ParserJson.API_PARAMETER_COUPON_RESPONSE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result) {
                    initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", message);
                } else {
                    if (mNewOrder == null) {
                        mNewOrder = new VtcModelNewOrder();
                    }

                    mNewOrder.setCoupon_code("");
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
        switch (keyTypeAction) {
            case TYPE_ACTION_CHECK_COUPON:

                if (mNewOrder == null) {
                    mNewOrder = new VtcModelNewOrder();
                }

                mNewOrder.setCoupon_code("");
                if (btnVoucher != null) {
                    btnVoucher.setText("");
                }

                break;
        }
    }

    private int validateData(){
        if(mNewOrder == null){
            return R.string.validate_input_new_order;
        }else if(getTextAddressView().equals("")){
            return R.string.btn_TranInfo_Search_Hint;
        } else if (mNewOrder.getFieldId().equals("")) {
            return R.string.btn_TranInfo_Fieldspicker;
        } else if (mNewOrder.getType() != VtcModelNewOrder.TYPE_BOOKING_FAST && mNewOrder.getType() !=
                VtcModelNewOrder.TYPE_BOOKING_NORMAL) {
            return R.string.btn_TranInfo_choose_time_warning;
        }

        return 0;
    }

    public String getTextAddressView() {
        if(tv_address == null){
            return "";
        }
        return tv_address.getText().toString().trim();
    }


    // this function coppy from FM Fix Field List detail for moving logic purpose.
    private void initHandlerDataBack() {

        if (mCallBack != null) {

            mCallBack.onHandlerCallBack(Const.KEY_CREATE_ORDER_FLOW_STEP_THREE, mNewOrder);
        }
    }

    private void initData() {
        tv_address.setText(mNewOrder.getAddname());
        btnType.setText(mNewOrder.getPaytype());
        if (mNewOrder.getType() == VtcModelNewOrder.TYPE_BOOKING_FAST) {
            btnTime.setText(R.string.btn_TranInfo_Now);
        } else if (mNewOrder.getType() == VtcModelNewOrder.TYPE_BOOKING_NORMAL) {
            btnTime.setText(mNewOrder.getOrderTimeString());
        }
        btnVoucher.setText(mNewOrder.getCoupon_code());
        btnNote.setText(mNewOrder.getDescription());


        if (adtImageOrder != null) {
            adtImageOrder.initSetData(mNewOrder.getLstImageUri(), mNewOrder.getLstImageBase64());
        }

        initCheckViewImage();
    }

    public void setmBackListenner(MainScreen.OnBackListenner mBackListenner) {
        this.mBackListenner = mBackListenner;
    }
}
