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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.adt.adtnormal.AdtNavHistoryLst;
import com.strategy.intecom.vtc.fixuser.enums.StatusBookingJob;
import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Luan Pham on 5/30/6.
 */
public class FMNavHistory extends AppCore implements CompoundButton.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener {
    private View viewRoot;

    private ImageView btn_back;
    private TextView tv_title;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private AdtNavHistoryLst mAdapter;
    private Callback callback;

    public FMNavHistory() {

    }

    @SuppressLint("ValidFragment")
    public FMNavHistory(Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.ui_history_list, container, false);
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

        tv_title.setText(getResources().getString(R.string.title_bar_history_job_all));

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBack();
            }
        });

        initData(true);
    }

    private void initData(boolean isShowProcess) {

        initGetOrderLst(FMNavHistory.this, StatusBookingJob.STATUS_USER_CANCEL.getValuesStatus() + "|" + StatusBookingJob.STATUS_FINISH.getValuesStatus() + "|" + StatusBookingJob.STATUS_AGENCY_CANCEL.getValuesStatus(), isShowProcess);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onPostExecuteError(TypeActionConnection keyTypeAction, TypeErrorConnection keyType, String msg) {
        super.onPostExecuteError(keyTypeAction, keyType, msg);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        if (!response.equals("")) {

            try {
                JSONArray jsonArray = new JSONArray(response);
                List<VtcModelOrder> lst = VtcModelOrder.getOrderData(jsonArray);

                mAdapter = new AdtNavHistoryLst(getActivity(), lst);

                mAdapter.setOnClickItem(new AdtNavHistoryLst.onClickItem() {
                    @Override
                    public void onClickItem(VtcModelOrder vtcModelOrder) {
                        Bundle bundle = new Bundle();
                        if(vtcModelOrder != null && vtcModelOrder.getAgency() != null){
                            vtcModelOrder.getAgency().setId("");
                        }

                        bundle.putParcelable(Const.KEY_BUNDLE_ACTION_NEW_ORDER, vtcModelOrder);
                        bundle.putBoolean(Const.KEY_BUNDLE_ACTION_VIEW, true);

                        AppCore.CallFragmentSectionWithCallback(Const.UI_REPAIRER_INFO, bundle, new Callback() {
                            @Override
                            public <T> void onHandlerCallBack(int key, T... t) {

                            }
                        });
                    }
                });
                mRecyclerView.setAdapter(mAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        initData(false);
    }
}
