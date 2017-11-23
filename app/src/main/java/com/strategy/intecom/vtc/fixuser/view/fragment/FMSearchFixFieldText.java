package com.strategy.intecom.vtc.fixuser.view.fragment;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.adt.adtnormal.AdtSearchFixFields;
import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.model.VtcModelFields;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mr. Ha on 7/27/16.
 */
public class FMSearchFixFieldText extends AppCore {
    public static String TAG = FMSearchFixFieldText.class.getName();
    private View viewRoot;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private AdtSearchFixFields mAdapter;
    private VtcModelFields vtcModelFieldsParent;

    private ImageView btn_back;
    private TextView tv_title;

    private SearchView search;
    private List<String> items;

    private Callback callback;

    @Override
    public void onResume() {
        super.onResume();
        search.requestFocusFromTouch();
    }

    public FMSearchFixFieldText() {

    }

    @SuppressLint("ValidFragment")
    public FMSearchFixFieldText(Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.ui_search_text, container, false);

        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            vtcModelFieldsParent = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_FIELD);
        }
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
        mSwipeRefreshLayout.setEnabled(false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getCurrentActivity()));
        mAdapter = new AdtSearchFixFields(getCurrentActivity(), new ArrayList<Holder>());
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
                Utils.hideKeyboard(getCurrentActivity());
                cmdBack();
            }
        });

        mAdapter.setOnClickItem(new AdtSearchFixFields.OnClickItem() {
            @Override
            public void onClickItem(Holder holder) {
                callback.onHandlerCallBack(-1, holder);
                Utils.hideKeyboard(getCurrentActivity());
                cmdBack();
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
                    if (newText.length() > 0) {
                        showData(doSearch(newText));
                    }
                    return false;
                }
            });
        }

        mRecyclerView.removeAllViews();
    }


    private void showData(List<Holder> matchedList) {
        mAdapter.updateData(matchedList);
        mAdapter.notifyDataSetChanged();
    }

    private List<Holder> doSearch(String newText) {
        String txtToSearch = newText.toLowerCase();
        VtcModelFields fields = new VtcModelFields();
        List<VtcModelFields> listParent = fields.getLstFieldsParent();
        HashMap<String, List<VtcModelFields>> listChild = fields.getLstFieldsChild();

        List<Holder> listMatched = new ArrayList<Holder>();

        if (vtcModelFieldsParent != null) {
            List<VtcModelFields> childs = listChild.get(vtcModelFieldsParent.getName());
            listMatched.addAll(getChildList(childs, txtToSearch, vtcModelFieldsParent));
        } else {
            for (int i = 0; i < listParent.size(); i++) {
                VtcModelFields parent = listParent.get(i);
                List<VtcModelFields> childs = listChild.get(parent.getName());
                listMatched.addAll(getChildList(childs, txtToSearch, parent));
            }
        }
        return listMatched;
    }

    private List<Holder> getChildList(List<VtcModelFields> childs, String txtToSearch, VtcModelFields parent) {
        List<Holder> listMatched = new ArrayList<Holder>();
        if (childs != null) {
            for (int j = 0; j < childs.size(); j++) {
                VtcModelFields child = childs.get(j);
                String name = child.getName().toLowerCase();
                if (name.contains(txtToSearch)) {
                    Holder matched = new Holder();
                    matched.parent = parent;
                    matched.child = child;
                    listMatched.add(matched);
                }
            }
        }
        return listMatched;
    }

    public static final class Holder {
        public VtcModelFields parent;
        public VtcModelFields child;
    }

}
