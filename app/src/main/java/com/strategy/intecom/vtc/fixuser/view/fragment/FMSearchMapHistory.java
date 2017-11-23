package com.strategy.intecom.vtc.fixuser.view.fragment;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.adt.adtnormal.AdtSearchHistory;
import com.strategy.intecom.vtc.fixuser.adt.adtnormal.AdtSearchLocationHistory;
import com.strategy.intecom.vtc.fixuser.config.VtcDBConnect;
import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.model.VtcModelAddress;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 7/27/16.
 */
public class FMSearchMapHistory extends AppCore implements SwipeRefreshLayout.OnRefreshListener
        , CompoundButton.OnCheckedChangeListener {
    public static String TAG = FMSearchMapHistory.class.getName();
    private View viewRoot;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private AdtSearchLocationHistory mAdapter;

    private List<VtcModelAddress> lstAdderss;

    private Callback callback;

    private ImageView btn_back;
    private TextView tv_title;

    private SearchView search;
    private List<String> items;

    public FMSearchMapHistory() {

    }

    @SuppressLint("ValidFragment")
    public FMSearchMapHistory(Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.ui_search_text, container, false);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initControler(view);

    }


    private void initControler(View view) {
        items = new ArrayList<>();

        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getCurrentActivity()));

        lstAdderss = new VtcDBConnect(getActivity()).getAddressFromDB();

        mAdapter = new AdtSearchLocationHistory(getActivity(), lstAdderss);
        mRecyclerView.setAdapter(mAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

            search = (SearchView) view.findViewById(R.id.edt_search);

            search.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));
        }

        initEvent();

    }

    private void initEvent() {

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmdBack();
            }
        });

        mAdapter.setOnClickItem(new AdtSearchLocationHistory.onClickItem() {
            @Override
            public void onClickItem(VtcModelAddress modelAddress) {
                new VtcDBConnect(getActivity()).checkAddressExists(modelAddress);

                initDataView();
            }

            @Override
            public void onClickItemAddress(VtcModelAddress modelAddress) {
                if(callback != null){
                    callback.onHandlerCallBack(3, modelAddress);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (tv_title != null) {
                        if (hasFocus) {
                            tv_title.setVisibility(TextView.GONE);
                        }
                    }
                }
            });

            search.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    if (tv_title != null) {
                        tv_title.setVisibility(TextView.VISIBLE);
                    }
                    return false;
                }
            });

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    items.add(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    loadHistory(newText);
                    if (newText.length() > 0) {
                        lstAdderss = new VtcDBConnect(getActivity()).searchAddressFromDB(newText);

                        mRecyclerView.removeAllViews();
                        mAdapter.setData(lstAdderss);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    return false;
                }
            });
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        initDataView();
    }

    public void initDataView(){

        lstAdderss = new VtcDBConnect(getActivity()).getAddressFromDB();

        mRecyclerView.removeAllViews();
        mAdapter.setData(lstAdderss);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadHistory(String query) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            // Cursor
            String[] columns = new String[]{"_id", "text"};
            Object[] temp = new Object[]{0, "default"};

            MatrixCursor cursor = new MatrixCursor(columns);

            for (int i = 0; i < items.size(); i++) {

                temp[0] = i;
                temp[1] = items.get(i);

                cursor.addRow(temp);
            }
            search.setSuggestionsAdapter(new AdtSearchHistory(getCurrentActivity(), cursor, items));
        }
    }
}
