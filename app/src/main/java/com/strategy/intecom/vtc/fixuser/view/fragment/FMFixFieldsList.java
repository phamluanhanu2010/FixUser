package com.strategy.intecom.vtc.fixuser.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.MainScreen;
import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.adt.adtnormal.AdtFixFieldsLst;
import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.model.VtcModelFields;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNewOrder;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Luan Pham on 5/30/6.
 */
public class FMFixFieldsList extends AppCore implements SwipeRefreshLayout.OnRefreshListener{
    private View viewRoot;

    private RecyclerView mRecyclerView;
    private AdtFixFieldsLst mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Callback callback;

    private VtcModelFields vtcModelFields;

    private VtcModelNewOrder vtcModelNewOrder;

    private ImageView btn_back;
    private TextView tv_title;

    private int mKeyFunction = Const.KEY_NORMAL_CASE;
    private MainScreen.OnBackListenner mBackListenner;

    public FMFixFieldsList() {

    }
    @SuppressLint("ValidFragment")
    public FMFixFieldsList(Callback callback) {
        this.callback = callback;
    }

    /**
     * using this contructor to create new order
     * @param callback main fragment controll all this action
     * @param flowKey usualy : Const.KEY_CREATE_ORDER_FLOW_STEP_ONE only for create new order
     */
    @SuppressLint("ValidFragment")
    public FMFixFieldsList(int flowKey, Callback callback) {
        this.callback = callback;
        mKeyFunction = flowKey;
        vtcModelNewOrder = new VtcModelNewOrder();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        savedInstanceState = getArguments();
        if(savedInstanceState != null){
            vtcModelNewOrder = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_NEW_ORDER);
        }
        viewRoot = inflater.inflate(R.layout.ui_fixfields_list, container, false);
        return viewRoot;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initControler(view);
    }

    private void initControler(View view) {

        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        tv_title = (TextView) view.findViewById(R.id.tv_title);

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getCurrentActivity()));

        mAdapter = new AdtFixFieldsLst(getCurrentActivity());

        mAdapter.setOnClickItem(new AdtFixFieldsLst.onClickItem() {
            @Override
            public void onClickIten(final VtcModelFields vtcModelFields) {

                Bundle bundle = new Bundle();
                bundle.putParcelable(Const.KEY_BUNDLE_ACTION_FIELD, vtcModelFields);
                bundle.putParcelable(Const.KEY_BUNDLE_ACTION_NEW_ORDER, vtcModelNewOrder);
                if (mKeyFunction == Const.KEY_CREATE_ORDER_FLOW_STEP_ONE) {
                    callback.onHandlerCallBack(FMCreateOrder.STEP_ONE_RESPOND_FIELDS, bundle);
                } else {
                    AppCore.CallFragmentSectionWithCallback(Const.UI_FIXFIELDS_DETAIL_LIST, bundle, new Callback() {
                        @Override
                        public <T> void onHandlerCallBack(int key, T... t) {

                            if (t != null) {
                                if (t.length > 0 && t[0] instanceof VtcModelNewOrder) {
                                    if (callback != null) {
                                        vtcModelNewOrder = (VtcModelNewOrder) t[0];
                                        callback.onHandlerCallBack(-1, vtcModelNewOrder);
                                    }
                                }
                            }
                        }
                    }, true, true);
                }

            }
        });
        mRecyclerView.setAdapter(mAdapter);

        if (vtcModelFields == null || vtcModelFields.getLstFieldsParent() == null) {
            initGetFieldLst(FMFixFieldsList.this, true);
        } else {
            initData(vtcModelFields.getLstFieldsParent());
        }

        tv_title.setSelected(true);

        tv_title.setText(getResources().getString(R.string.title_FixFields));

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mKeyFunction == Const.KEY_CREATE_ORDER_FLOW_STEP_ONE) {
                    if (mBackListenner != null) {
                        mBackListenner.onBack();
                    }
                } else {
                    cmdBack();
                }
            }
        });

        ImageView btn_next = (ImageView)view.findViewById(R.id.btn_next);
        btn_next.setVisibility(View.VISIBLE);
        btn_next.setImageResource(R.drawable.icon_search);
        btn_next.setSelected(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallFragmentSectionWithCallback(Const.UI_SEARCH_FIX_FIELD, new Callback() {
                    @Override
                    public <T> void onHandlerCallBack(int key, T... t) {
                        callback.onHandlerCallBack(FMCreateOrder.STEP_SPECIAL_UPDATE_FIX_FIELD, t[0]);
                    }
                });
            }
        });
    }

    private void initData(List<VtcModelFields> lst){
        mRecyclerView.removeAllViews();
        mAdapter.initSetData(lst);
        mRecyclerView.setAdapter(mAdapter);
        if (vtcModelNewOrder != null) {
            mAdapter.setChecked(vtcModelNewOrder.getName());
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);

        initGetFieldLst(FMFixFieldsList.this, false);
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        mSwipeRefreshLayout.setRefreshing(false);
        switch (keyType) {
            case TYPE_ACTION_GET_LIST_FIELD:
                parserResponseField(response);
                return;
        }
    }

    @Override
    public void onPostExecuteError(TypeActionConnection keyTypeAction, TypeErrorConnection keyType, String msg) {
        super.onPostExecuteError(keyTypeAction, keyType, msg);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void parserResponseField(String response) {
        try {
            JSONArray jsonArrayFields = new JSONArray(response);
            if (jsonArrayFields.length() > 0) {
                vtcModelFields = VtcModelFields.getLstDataFields(jsonArrayFields);
                if (vtcModelFields != null && vtcModelFields.getLstFieldsParent() != null) {
                    initData(vtcModelFields.getLstFieldsParent());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public void updateData(VtcModelNewOrder vtcModelNewOrder) {
        this.vtcModelNewOrder = vtcModelNewOrder;
        if (mAdapter != null) {
            mAdapter.setChecked(vtcModelNewOrder.getName());
        }
    }

    public void setmBackListenner(MainScreen.OnBackListenner mBackListenner) {
        this.mBackListenner = mBackListenner;
    }

    @Override
    public void onResume() {
        showLog("FMFixFieldsList onResume");
        if (getCurrentActivity().isRefreshConnection()) onRefresh();
        super.onResume();
    }
}
