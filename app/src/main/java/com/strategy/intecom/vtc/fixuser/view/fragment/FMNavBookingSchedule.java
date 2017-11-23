package com.strategy.intecom.vtc.fixuser.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.adt.adtnormal.AdtNavBookingScheduleLst;
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
public class FMNavBookingSchedule extends AppCore implements CompoundButton.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener {
    private View viewRoot;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private AdtNavBookingScheduleLst mAdapter;
    private Callback callback;

    private RelativeLayout lout_BookingSchedule_None;
    private Button btn_BookingShedule_Now;
    private ImageView btn_back;
    private TextView tv_title;

    public FMNavBookingSchedule() {

    }

    @SuppressLint("ValidFragment")
    public FMNavBookingSchedule(Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.ui_booking_schedule_list, container, false);
        return viewRoot;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initControler(view);

    }

    private void initControler(View view) {

        lout_BookingSchedule_None = (RelativeLayout) view.findViewById(R.id.lout_BookingSchedule_None);
        btn_BookingShedule_Now = (Button) view.findViewById(R.id.btn_BookingShedule_Now);
        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        tv_title = (TextView) view.findViewById(R.id.tv_title);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getCurrentActivity()));

        tv_title.setText(getResources().getString(R.string.title_bar_history_job));

        btn_BookingShedule_Now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalBroadcastManager.getInstance(getCurrentActivity()).sendBroadcast(new Intent
                        (FMMainJobMap.KEY_NEW_ORDER_FILTER));
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBack();
            }
        });

        initData(true);
    }

    private void initData(boolean isShowProcess) {

        initGetOrderLst(FMNavBookingSchedule.this, StatusBookingJob.STATUS_FINDING.getValuesStatus() + "|" +
                StatusBookingJob.STATUS_ACCEPTED.getValuesStatus()
                + "|" + StatusBookingJob.STATUS_COMING.getValuesStatus() + "|" + StatusBookingJob.STATUS_WORKING
                .getValuesStatus(), isShowProcess);
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

                if(lst != null && lst.size() > 0) {

                    lout_BookingSchedule_None.setVisibility(RelativeLayout.GONE);

                    mAdapter = new AdtNavBookingScheduleLst(getActivity(), lst);
                    mAdapter.setOnClickItem(new AdtNavBookingScheduleLst.onClickItem() {

                        @Override
                        public void onClickIten(VtcModelOrder vtcModelOrder) {
                            Bundle bundle = new Bundle();

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
                    mRecyclerView.setVisibility(View.VISIBLE);
                }else {
                    lout_BookingSchedule_None.setVisibility(RelativeLayout.VISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                }

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

    @Override
    protected void onBackToTop() {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        }, 1000);
    }
}
