package com.strategy.intecom.vtc.fixuser.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.adt.adtnormal.AdtNavNotificationLst;
import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNotification;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.view.UINotificationContent;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Luan Pham on 5/30/6.
 */
public class FMNotification extends AppCore implements AdtNavNotificationLst.OnClickItem
        , SwipeRefreshLayout.OnRefreshListener{
    private View viewRoot;

    private ImageView btn_back;
    private TextView tv_title;

    private RecyclerView mRecyclerView;
    private AdtNavNotificationLst mAdapter;
    private Callback callback;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public FMNotification() {

    }
    @SuppressLint("ValidFragment")
    public FMNotification(Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.ui_notification_list, container, false);
        return viewRoot;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initController(view);

    }

    private void initController(View view) {

        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        tv_title = (TextView) view.findViewById(R.id.tv_title);

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getCurrentActivity()));

        tv_title.setText(getResources().getString(R.string.title_dialog_message));

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBack();
            }
        });

        initData();
    }

    private void initData(){
        // Call api to get all notification from server
        initGetNotificationList(FMNotification.this, getVtcModelUser().get_id());
    }

    @Override
    public void onPostExecuteError(TypeActionConnection keyTypeAction, TypeErrorConnection keyType, String msg) {
        super.onPostExecuteError(keyTypeAction, keyType, msg);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                ArrayList<VtcModelNotification> lst = VtcModelNotification
                        .getListNotification(jsonArray);

                mAdapter = new AdtNavNotificationLst(lst);
                mAdapter.setOnClickItem(this);
                mRecyclerView.setAdapter(mAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClickItem(ArrayList<VtcModelNotification> notiList, String
            noticeId) {
        Intent i = new Intent(getActivity(), UINotificationContent.class);
        i.putParcelableArrayListExtra(Const.KEY_EXTRACT_NOTIFICATION, notiList);
        i.putExtra(Const.KEY_EXTRACT_NOTI_ID, noticeId);
        startActivity(i);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }
}
