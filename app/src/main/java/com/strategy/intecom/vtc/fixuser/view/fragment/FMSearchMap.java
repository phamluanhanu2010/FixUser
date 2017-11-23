package com.strategy.intecom.vtc.fixuser.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.model.VtcModelAddress;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNewOrder;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;
import com.strategy.intecom.vtc.fixuser.view.custom.MyViewPager;

/**
 * Created by Mr. Ha on 7/27/16.
 */
public class FMSearchMap extends AppCore implements View.OnClickListener{
    private View viewRoot;

    private SectionsPagerAdapter sectionsPagerAdapter;
    private MyViewPager main_viewpager;

    private ImageView btn_history;
    private ImageView btn_search_text;
    private ImageView btn_pin_map;

    private Callback callback;

    private VtcModelAddress vtcModelAddress;

    public FMSearchMap() {

    }

    @SuppressLint("ValidFragment")
    public FMSearchMap(Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        savedInstanceState = getArguments();
        if(savedInstanceState != null){
            vtcModelAddress = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_ADDRESS);
        }
        viewRoot = inflater.inflate(R.layout.content_pin_map, container, false);
        return viewRoot;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initControler(view);
    }

    private void initControler(View view) {

        btn_history = (ImageView) view.findViewById(R.id.btn_history);
        btn_search_text = (ImageView) view.findViewById(R.id.btn_search_text);
        btn_pin_map = (ImageView) view.findViewById(R.id.btn_pin_map);

        sectionsPagerAdapter = new SectionsPagerAdapter(getCurrentActivity().getSupportFragmentManager());
        main_viewpager = (MyViewPager) viewRoot.findViewById(R.id.content_frame_main);
        main_viewpager.setPagingEnabled(false);
        main_viewpager.setOffscreenPageLimit(3);
        main_viewpager.setAdapter(sectionsPagerAdapter);
        main_viewpager.setCurrentItem(0, false);

        btn_history.setOnClickListener(this);
        btn_search_text.setOnClickListener(this);
        btn_pin_map.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Utils.hideKeyboard(getCurrentActivity());
        switch (v.getId()){
            case R.id.btn_search_text:
                btn_history.setBackgroundResource(R.drawable.btn_ripple_yellow_grey);
                btn_search_text.setBackgroundResource(R.drawable.btn_ripple_yellow_grey_tranparent);
                btn_pin_map.setBackgroundResource(R.drawable.btn_ripple_yellow_grey);
                main_viewpager.setCurrentItem(0, false);
                break;
            case R.id.btn_history:
                btn_history.setBackgroundResource(R.drawable.btn_ripple_yellow_grey_tranparent);
                btn_search_text.setBackgroundResource(R.drawable.btn_ripple_yellow_grey);
                btn_pin_map.setBackgroundResource(R.drawable.btn_ripple_yellow_grey);
                main_viewpager.setCurrentItem(1, false);
                break;
            case R.id.btn_pin_map:
                btn_history.setBackgroundResource(R.drawable.btn_ripple_yellow_grey);
                btn_search_text.setBackgroundResource(R.drawable.btn_ripple_yellow_grey);
                btn_pin_map.setBackgroundResource(R.drawable.btn_ripple_yellow_grey_tranparent);
                main_viewpager.setCurrentItem(2, false);
                break;
        }
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter implements Callback{
        private FMSearchMapText searchMapText;
        private FMSearchMapHistory searchMapHistory;
        private FMSearchMapPinMap searchMapPinMap;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            searchMapText = new FMSearchMapText(this);
            searchMapHistory = new FMSearchMapHistory(this);
            searchMapPinMap = new FMSearchMapPinMap(this);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return searchMapText;
                case 1:
                    return searchMapHistory;
                case 2:
                    searchMapPinMap.setVtcModelAddress(vtcModelAddress);
                    return searchMapPinMap;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public <T> void onHandlerCallBack(int key, T... t) {
            switch (key){
                case 0:
                    searchMapHistory.initDataView();
                    break;
                case 1:
                    break;
                case 2:
                    if(t != null && t.length > 0) {
                        if(t[0] instanceof VtcModelAddress) {
                            searchMapText.onUpdateItemAddress((VtcModelAddress) t[0]);
                        }
                    }
                    break;
                case 3:
                    if(t != null && t.length > 0) {
                        if (t[0] instanceof VtcModelAddress) {
                            if (callback != null) {
                                callback.onHandlerCallBack(-1, (VtcModelAddress) t[0]);
                            }
                            cmdBack();
                        }
                    }
                    break;
            }
        }

        public void initRemoveFragment(){
            if(searchMapHistory != null) {
                FragmentManager manager0 = searchMapHistory.getFragmentManager();
                if(manager0 != null) {
                    FragmentTransaction trans0 = manager0.beginTransaction();
                    if(trans0 != null) {
                        trans0.remove(searchMapHistory);
                        trans0.commit();
                    }
                }
            }

            if(searchMapText != null) {
                FragmentManager manager1 = searchMapText.getFragmentManager();
                if(manager1 != null) {
                    FragmentTransaction trans1 = manager1.beginTransaction();
                    if(trans1 != null) {
                        trans1.remove(searchMapText);
                        trans1.commit();
                    }
                }
            }

            if(searchMapPinMap != null) {
                FragmentManager manager = searchMapPinMap.getFragmentManager();
                if(manager != null) {
                    FragmentTransaction trans = manager.beginTransaction();
                    if(trans != null) {
                        trans.remove(searchMapPinMap);
                        trans.commit();
                    }
                }
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(sectionsPagerAdapter != null) {
            sectionsPagerAdapter.initRemoveFragment();
        }
        sectionsPagerAdapter = null;
        main_viewpager = null;
        Utils.hideKeyboard(getCurrentActivity());
    }
}
