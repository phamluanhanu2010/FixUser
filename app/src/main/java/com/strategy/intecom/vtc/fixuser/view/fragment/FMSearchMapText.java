package com.strategy.intecom.vtc.fixuser.view.fragment;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.strategy.intecom.vtc.fixuser.utils.Logger;
import com.strategy.intecom.vtc.fixuser.utils.map.PlaceProvider;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 7/27/16.
 */
public class FMSearchMapText extends AppCore implements LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener
        , CompoundButton.OnCheckedChangeListener {
    public static String TAG = FMSearchMapText.class.getSimpleName();
    private View viewRoot;

    private int acceptAction = -1;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private AdtSearchLocationHistory mAdapter;

    private List<VtcModelAddress> lstAdderss;

    private ImageView btn_back;
    private TextView tv_title;

    private SearchView search;
    private List<String> items;

    private Callback callback;

    @Override
    public void onResume() {
        super.onResume();
        Logger.d(TAG, this, "onResume");
        search.requestFocusFromTouch();
    }

    public FMSearchMapText() {

    }

    @SuppressLint("ValidFragment")
    public FMSearchMapText(Callback callback) {
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
            search.setIconified(false);
            search.setFocusable(true);

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
                if (callback != null) {
                    callback.onHandlerCallBack(0);
                }
            }

            @Override
            public void onClickItemAddress(VtcModelAddress modelAddress) {
                if (callback != null) {
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
                        doSearch(newText);
                    }
                    return false;
                }
            });
        }

        mRecyclerView.removeAllViews();
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

            search.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                @Override
                public boolean onSuggestionSelect(int position) {
                    search.setQuery(items.get(position), false);
                    return false;
                }

                @Override
                public boolean onSuggestionClick(int position) {
                    search.setQuery(items.get(position), false);
                    return false;
                }
            });
        }
    }

    public void onUpdateItemAddress(VtcModelAddress address) {
        if (lstAdderss == null) {
            lstAdderss = new ArrayList<>();
        }

        if (mRecyclerView != null && mAdapter != null) {
            mRecyclerView.removeAllViews();
            lstAdderss.clear();
            lstAdderss.add(address);

            mAdapter.setData(lstAdderss);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void doSearch(String query) {
        Bundle data = new Bundle();
        data.putString("query", query);
        getCurrentActivity().getSupportLoaderManager().restartLoader(0, data, this);
    }

    private void getPlace(String query) {
        Bundle data = new Bundle();
        data.putString("query", query);
        getCurrentActivity().getSupportLoaderManager().restartLoader(1, data, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle query) {
        CursorLoader cLoader = null;

        if (arg0 == 0)
            cLoader = new CursorLoader(getActivity(), PlaceProvider.SEARCH_URI, null, null, new String[]{query.getString("query")}, null);
        else if (arg0 == 1)
            cLoader = new CursorLoader(getActivity(), PlaceProvider.DETAILS_URI, null, null, new String[]{query.getString("query")}, null);
        return cLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
        mRecyclerView.removeAllViews();
        lstAdderss.clear();
        if (c != null) {
            if (c.getCount() > 0) {

                while (c.moveToNext()) {
                    VtcModelAddress vtcModelAddress = new VtcModelAddress();

                    vtcModelAddress.setAddress(c.getString(0));

                    double lat = 0.0f;
                    double longt = 0.0f;

                    try {
                        lat = Double.parseDouble(c.getString(1));
                        longt = Double.parseDouble(c.getString(2));
                    } catch (NumberFormatException e) {
                        AppCore.showLog("------- : " + e.getMessage());
                    }

                    vtcModelAddress.setLatitude(lat);
                    vtcModelAddress.setLongitude(longt);

                    lstAdderss.add(vtcModelAddress);
                }
            }
        }

        mAdapter.setData(lstAdderss);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {

    }
}
