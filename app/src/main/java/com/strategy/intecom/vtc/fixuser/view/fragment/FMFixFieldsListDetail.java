package com.strategy.intecom.vtc.fixuser.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.MainScreen;
import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.adt.adtnormal.AdtFixFieldsDetailLst;
import com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.model.VtcModelFields;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNewOrder;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad.MediaAsync;

import java.util.List;

/**
 * Created by Luan Pham on 5/30/6.
 */
public class FMFixFieldsListDetail extends AppCore implements View.OnClickListener {
    private View viewRoot;

    private ListView my_recycler_view;
    private AdtFixFieldsDetailLst mAdapter;

    private Callback callback;

    private VtcModelFields vtcModelFields;

    private VtcModelNewOrder vtcModelNewOrder;

    private ImageView btn_back;
    private TextView tv_bar_fields_titile;
    private ImageView img_bar_fields_icon;

    private int mKeyFunction = Const.KEY_NORMAL_CASE;
    private MainScreen.OnBackListenner mBackListenner;

    public FMFixFieldsListDetail() {

    }

    @SuppressLint("ValidFragment")
    public FMFixFieldsListDetail(Callback callback) {
        this.callback = callback;
    }

    /**
     * using this contructor to create new order
     *
     * @param callback main fragment controll all this action
     * @param flowKey  usualy : Const.KEY_CREATE_ORDER_FLOW_STEP_ONE only for create new order
     */
    @SuppressLint("ValidFragment")
    public FMFixFieldsListDetail(int flowKey, Callback callback) {
        this.callback = callback;
        mKeyFunction = flowKey;
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            vtcModelFields = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_FIELD);
            vtcModelNewOrder = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_NEW_ORDER);
        }
        viewRoot = inflater.inflate(R.layout.ui_fixfields_detail_list, container, false);
        return viewRoot;
    }

    void updateData(Bundle data) {
        if (data != null) {
            vtcModelFields = data.getParcelable(Const.KEY_BUNDLE_ACTION_FIELD);
            vtcModelNewOrder = data.getParcelable(Const.KEY_BUNDLE_ACTION_NEW_ORDER);
        }
        initController(viewRoot);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initController(view);
    }

    private void initController(View view) {

        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        tv_bar_fields_titile = (TextView) view.findViewById(R.id.tv_bar_fields_titile);
        img_bar_fields_icon = (ImageView) view.findViewById(R.id.img_bar_fields_icon);

        my_recycler_view = (ListView) view.findViewById(R.id.my_recycler_view);
        my_recycler_view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        btn_back.setOnClickListener(this);

        initData();

        ImageView btn_next = (ImageView)view.findViewById(R.id.btn_next);
        btn_next.setVisibility(View.VISIBLE);
        btn_next.setImageResource(R.drawable.icon_search);
        btn_next.setSelected(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putParcelable(Const.KEY_BUNDLE_ACTION_FIELD, vtcModelFields);
                CallFragmentSectionWithCallback(Const.UI_SEARCH_FIX_FIELD, b, new Callback() {
                    @Override
                    public <T> void onHandlerCallBack(int key, T... t) {
                        callback.onHandlerCallBack(FMCreateOrder.STEP_SPECIAL_UPDATE_FIX_FIELD, t[0]);
                    }
                });
            }
        });
    }

    private void initData() {

        if (vtcModelFields != null && vtcModelFields.getLstFieldsChild() != null) {

            List<VtcModelFields> lstField = vtcModelFields.getLstFieldsChild().get(vtcModelFields.getName());

            String idField = "";
            if (vtcModelNewOrder != null && vtcModelNewOrder.getFieldId() != null) {
                idField = vtcModelNewOrder.getFieldId();
            }
            if (lstField != null) {
                for (int i = 0; i < lstField.size(); i++) {
                    VtcModelFields vtcModelFields = lstField.get(i);
                    if (vtcModelFields != null && vtcModelFields.getId() != null) {
                        if (vtcModelFields.getId().equals(idField)) {
                            vtcModelFields.setChoice(true);
                        } else {
                            vtcModelFields.setChoice(false);
                        }
                    }
                }
            }

            mAdapter = new AdtFixFieldsDetailLst(getActivity(), lstField);

            mAdapter.setOnClickItem(new AdtFixFieldsDetailLst.onClickItem() {
                @Override
                public void onClickItem(VtcModelFields vtcModelFields) {
                        if (vtcModelNewOrder != null) {
                            vtcModelNewOrder.setName(FMFixFieldsListDetail.this.vtcModelFields.getName());
                            vtcModelNewOrder.setPrice(vtcModelFields.getPrice());
                            vtcModelNewOrder.setFieldId(vtcModelFields.getId());
                            vtcModelNewOrder.setFieldChildName(vtcModelFields.getName());
                        }
                    if (mKeyFunction == Const.KEY_CREATE_ORDER_FLOW_STEP_TWO) {
                        callback.onHandlerCallBack(mKeyFunction, vtcModelNewOrder);
                    }
                }
            });
            my_recycler_view.setAdapter(mAdapter);

            tv_bar_fields_titile.setText(vtcModelFields.getName());

            int sizeAvatar = (int) AppCore.getCurrentActivity().getResources().getDimension(R.dimen.size_icon_in_app);

            ImageLoadAsync loadAsync = new ImageLoadAsync(AppCore.getCurrentActivity(), img_bar_fields_icon,
                    sizeAvatar, sizeAvatar);
            loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, String.valueOf(vtcModelFields.getImage()));
        }
    }

    @Override
    public void cmdPressDialogYes(TypeShowDialog typeShowDialog, String str) {
        switch (TypeShowDialog.forValue(typeShowDialog.getValuesTypeDialog())) {
            case TYPE_SHOW_MESSAGE_CANCEL:
                cmdBack();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                if (mKeyFunction == Const.KEY_CREATE_ORDER_FLOW_STEP_TWO) {
                    mBackListenner.onBack();
                } else {
                    cmdBack();
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mKeyFunction != Const.KEY_CREATE_ORDER_FLOW_STEP_TWO) {
            initHandlerDataBack();
        }
    }

    private void initHandlerDataBack() {
        if (callback != null && vtcModelNewOrder != null) {

            callback.onHandlerCallBack(-1, vtcModelNewOrder);
        }
    }

    public void setmBackListenner(MainScreen.OnBackListenner mBackListenner) {
        this.mBackListenner = mBackListenner;
    }
}
