package com.strategy.intecom.vtc.fixuser.view.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.strategy.intecom.vtc.fixuser.MainScreen;
import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNewOrder;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;
import com.strategy.intecom.vtc.fixuser.view.base.AppCoreBase;
import com.strategy.intecom.vtc.fixuser.view.custom.MyViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class FMCreateOrder extends AppCore implements Callback, MainScreen.OnBackListenner {

    // the steps of this create order
    private static final int PAGE_NUMBER = 3;

    // private package
    static final int STEP_ONE_RESPOND_FIELDS = 1;
    static final int STEP_TWO_RESPOND_FIELDS_DETAIL = 2;
    static final int STEP_THREE_RESPOND_FIELD_DETAIL = 3;
    static final int STEP_SPECIAL_UPDATE_FIX_FIELD = 4;

    private ImageView mTabOne;
    private ImageView mTabTwo;
    private ImageView mTabThree;


    private Pager adapter;

    //This is our viewPager
    private MyViewPager viewPager;

    private Callback mCallback;

    private VtcModelNewOrder mNewOrder;

    public FMCreateOrder() {

    }
    @SuppressLint("ValidFragment")
    public FMCreateOrder(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurrentActivity().setOnBackListenner(this);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        savedInstanceState = getArguments();
        if(savedInstanceState != null){
            mNewOrder = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_NEW_ORDER);
        }
        if(mNewOrder == null){
            mNewOrder = new VtcModelNewOrder();
        }
        return inflater.inflate(R.layout.ui_create_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mTabOne = (ImageView)view.findViewById(R.id.tab_one);
        mTabTwo = (ImageView)view.findViewById(R.id.tab_two);
        mTabThree = (ImageView)view.findViewById(R.id.tab_three);

        //Initializing viewPager
        viewPager = (MyViewPager) view.findViewById(R.id.pager);

        //Creating our pager adapter
        adapter = new Pager(getCurrentActivity().getSupportFragmentManager());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        viewPager.setPagingEnabled(false);
        setSelectedTab(Pager.PAGE_ONE);
    }

    private void setSelectedTab(int i) {
        mTabOne.setImageResource((i == Pager.PAGE_ONE) ? R.drawable.tab_bkg_selected: R.drawable.tab_bkg_unselected);
        mTabTwo.setImageResource((i == Pager.PAGE_TWO) ? R.drawable.tab_bkg_selected: R.drawable.tab_bkg_unselected);
        mTabThree.setImageResource((i == Pager.PAGE_THREE) ? R.drawable.tab_bkg_selected: R.drawable.tab_bkg_unselected);
    }

    @Override
    public <T> void onHandlerCallBack(int key, T... t) {
        switch (key) {
            case STEP_ONE_RESPOND_FIELDS:
                Bundle data = (Bundle) t[0];
                mNewOrder = data.getParcelable(Const.KEY_BUNDLE_ACTION_NEW_ORDER);
                adapter.getTabTwo().updateData(data);
                viewPager.setCurrentItem(Pager.PAGE_TWO);
                setSelectedTab(Pager.PAGE_TWO);
                break;
            case STEP_TWO_RESPOND_FIELDS_DETAIL:
                mNewOrder = (VtcModelNewOrder) t[0];
                adapter.getTabThree().updateData(mNewOrder);
                viewPager.setCurrentItem(Pager.PAGE_THREE);
                setSelectedTab(Pager.PAGE_THREE);
                break;
            case STEP_THREE_RESPOND_FIELD_DETAIL:
                mNewOrder = (VtcModelNewOrder) t[0];
                mCallback.onHandlerCallBack(-1, mNewOrder);
                cmdBack();
                break;
            case STEP_SPECIAL_UPDATE_FIX_FIELD:
                FMSearchFixFieldText.Holder dataHolder = (FMSearchFixFieldText.Holder) t[0];

                if (mNewOrder != null) {
                    mNewOrder.setName(dataHolder.parent.getName());
                    mNewOrder.setPrice(dataHolder.child.getPrice());
                    mNewOrder.setFieldId(dataHolder.child.getId());
                    mNewOrder.setFieldChildName(dataHolder.child.getName());
                }

                Bundle bundle = new Bundle();
                bundle.putParcelable(Const.KEY_BUNDLE_ACTION_FIELD, dataHolder.parent);
                bundle.putParcelable(Const.KEY_BUNDLE_ACTION_NEW_ORDER, mNewOrder);
                adapter.getTabTwo().updateData(bundle);

                adapter.getTabOne().updateData(mNewOrder);

                viewPager.setCurrentItem(Pager.PAGE_THREE);
                setSelectedTab(Pager.PAGE_THREE);
                break;
        }
    }

    @Override
    public void onBack() {
        int currentID = viewPager.getCurrentItem();
        switch (currentID) {
            case Pager.PAGE_ONE:
                if (adapter != null) {
                    adapter.getTabOne().onDestroyView();
                }
                cmdBack();
                break;
            case Pager.PAGE_TWO:
                viewPager.setCurrentItem(Pager.PAGE_ONE);
                setSelectedTab(Pager.PAGE_ONE);
                break;
            case Pager.PAGE_THREE:
                viewPager.setCurrentItem(Pager.PAGE_TWO);
                setSelectedTab(Pager.PAGE_TWO);
                break;
        }
    }

    @Override
    public void onDestroy() {
        getCurrentActivity().setOnBackListenner(null);
        super.onDestroy();
    }

    private class Pager extends FragmentStatePagerAdapter {
        private static final int PAGE_ONE = 0;
        private static final int PAGE_TWO = 1;
        private static final int PAGE_THREE = 2;

        //integer to count number of tabs
        int tabCount;

        private FMFixFieldsList mTabOne;
        private FMFixFieldsListDetail mTabTwo;
        private FMFillOrderDetail mTabThree;

        //Constructor to the class
        public Pager(FragmentManager fm) {
            super(fm);
            //Initializing tab count
            this.tabCount = PAGE_NUMBER;
        }

        //Overriding method getItem
        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    return getTabOne();
                case 1:
                    return getTabTwo();
                case 2:
                    return getTabThree();
                default:
                    return null;
            }
        }

        //Overriden method getCount to get the number of tabs
        @Override
        public int getCount() {
            return tabCount;
        }

        public FMFixFieldsList getTabOne() {
            if (mTabOne == null) {
                mTabOne = new FMFixFieldsList(Const.KEY_CREATE_ORDER_FLOW_STEP_ONE, FMCreateOrder.this);
                mTabOne.updateData(mNewOrder);
                mTabOne.setmBackListenner(FMCreateOrder.this);
            }
            return mTabOne;
        }

        public FMFixFieldsListDetail getTabTwo() {
            if (mTabTwo == null) {
                mTabTwo = new FMFixFieldsListDetail(Const.KEY_CREATE_ORDER_FLOW_STEP_TWO, FMCreateOrder.this);
                mTabTwo.setmBackListenner(FMCreateOrder.this);
            }
            return mTabTwo;
        }

        public FMFillOrderDetail getTabThree() {
            if (mTabThree == null) {
                mTabThree = new FMFillOrderDetail(FMCreateOrder.this);
                mTabThree.setmBackListenner(FMCreateOrder.this);
            }
            return mTabThree;
        }
    }
}
