package com.strategy.intecom.vtc.fixuser.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skyfishjy.library.RippleBackground;
import com.strategy.intecom.vtc.fixuser.MainScreen;
import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.adt.adtrecycler.AdtSearchRepairer;
import com.strategy.intecom.vtc.fixuser.enums.StatusBookingJob;
import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.model.VtcModelAgencyNearBy;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNewOrder;
import com.strategy.intecom.vtc.fixuser.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mr. Ha on 5/31/16.
 */
public class FMRepairerSearch extends AppCore implements MainScreen.OnBackListenner{
    static final int CLOSE_BACK = -1;
    static final int CLOSE_DONE = 0;
    static final int CLOSE_CHANGE_TIME = 1;

    private View viewRoot;

    private RippleBackground rippleBackground;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private LinearLayout lout_container_search_repairer;

    private VtcModelNewOrder vtcModelNewOrder;

    private TextView tv_address;
    private TextView tv_title_job;
    private TextView tv_description_money;
    private TextView tv_money;

    private Button btn_cancel;

    private CountDownTimer timerWaitReview;
    private TimerTask timerTaskWaitReview;
    private Timer timerReSearch;
    private TimerTask timerTaskReSearch;

    private VtcModelOrder vtcModelOrder = null;

    private  TextView tv_count;
    private ProgressBar mCountDownProgress;

    private int countDown = 50;

    private Callback callback;
    private boolean isPaused = false;
    private boolean orderAccepted = false;

    @SuppressLint("ValidFragment")
    public FMRepairerSearch(Callback callback){
        this.callback = callback;
    }

    public FMRepairerSearch(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            vtcModelNewOrder = savedInstanceState.getParcelable(Const.KEY_BUNDLE_ACTION_NEW_ORDER);
        }
        viewRoot = inflater.inflate(R.layout.ui_search_repairer, container, false);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initController(view);
    }

    private void initController(View view) {
        tv_count = (TextView) view.findViewById(R.id.tv_count);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_title_job = (TextView) view.findViewById(R.id.tv_title_job);
        tv_description_money = (TextView) view.findViewById(R.id.tv_description_money);
        tv_money = (TextView) view.findViewById(R.id.tv_money);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        mCountDownProgress = (ProgressBar) view.findViewById(R.id.count_down_progress);

        lout_container_search_repairer = (LinearLayout) view.findViewById(R.id.lout_container_search_repairer);

        rippleBackground = (RippleBackground) view.findViewById(R.id.content);
        rippleBackground.startRippleAnimation();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getCurrentActivity()));

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBack(CLOSE_BACK);
            }
        });

        tv_address.setSelected(true);

        initData();
    }

    private void initData() {

        if (vtcModelNewOrder != null) {
            tv_address.setText(vtcModelNewOrder.getAddname());
            tv_title_job.setText(Html.fromHtml(String.valueOf(Utils.initTextBold(vtcModelNewOrder.getName()) + " - " + vtcModelNewOrder.getFieldChildName())));
//            tv_description_content_job.setText(vtcModelNewOrder.getFieldChildName());
            tv_description_money.setText(getResources().getString(R.string.content_title_money, Utils.formatDecimal(Float.valueOf(vtcModelNewOrder.getPrice()))));

            tv_money.setText(vtcModelNewOrder.getPaytype());
        }

        mCountDownProgress.setProgress(countDown);
        tv_count.setText(String.valueOf(countDown));
        setTimeEnableOparation();
    }

    public void setTimeEnableOparation() {

        // because onTick method always respond later than real time. so I set the interval to smaller than 100ms to
        // make user feel better and easier to display from 1 to 0 (count down timer always miss the last tick)
        timerWaitReview = new CountDownTimer(5000, 87) {
            @Override
            public void onTick(long l) {
                mCountDownProgress.setProgress(countDown);
                if (countDown % 10 == 0) {
                    int displayValue = countDown / 10;
                    tv_count.setText(String.valueOf(displayValue));
                }
                countDown--;
            }

            @Override
            public void onFinish() {
                Activity activity = getCurrentActivity();
                if (activity == null) {
                    activity = getActivity();
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (timerWaitReview != null) {
                            timerWaitReview.cancel();
                            timerWaitReview = null;
                        }
                        tv_count.setText(String.valueOf(0));

                        if (timerTaskWaitReview != null) {
                            timerTaskWaitReview.cancel();
                            timerTaskWaitReview = null;
                        }
                        initCreateOrder(FMRepairerSearch.this, vtcModelNewOrder, getVtcModelUser(), false);

                        initTimerDoSearch(true);
                    }
                });
            }
        }.start();
    }

    private void initTimerDoSearch(final boolean isFirst) {
        timerReSearch  = new Timer();
        timerTaskReSearch = new TimerTask() {
            @Override
            public void run() {
                Activity activity = getCurrentActivity();
                if (activity == null) {
                    activity = getActivity();
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFirst) {
                            if(timerReSearch != null) {
                                timerReSearch.cancel();
                                timerReSearch = null;
                            }
                            if(timerTaskReSearch != null) {
                                timerTaskReSearch.cancel();
                                timerTaskReSearch = null;
                            }
                            initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_CONFIRM, "",
                                    getCurrentActivity().getResources().getString(R.string
                                            .title_message_research_agency_empty));
                        } else {
                            if (AppCore.getVtcModelUser() != null && vtcModelOrder != null) {
                                initCancelOrderMark(FMRepairerSearch.this, AppCore.getVtcModelUser().get_id(),
                                        vtcModelOrder.getId(), "");
                            }
                            initShowDialogOption(getActivity(), TypeShowDialog
                                    .TYPE_SHOW_MESSAGE_CHANGE_ORDER_TIME, "", getCurrentActivity().getResources()
                                    .getString(R.string.title_message_research_agency));

                        }
                    }
                });
            }
        };

        timerReSearch.schedule(timerTaskReSearch, 30 * 1000);
    }

    @Override
    public void cmdPressDialogYes(TypeShowDialog typeShowDialog) {
        super.cmdPressDialogYes(typeShowDialog);
        switch (typeShowDialog) {
            case TYPE_SHOW_MESSAGE_CONFIRM:
                if(vtcModelOrder != null) {
                    initReSearchOrder(FMRepairerSearch.this, vtcModelOrder.getId());
                    initTimerDoSearch(false);
                }
                break;
            case TYPE_SHOW_MESSAGE_CHANGE_ORDER_TIME:
                initBack(CLOSE_CHANGE_TIME);
                break;
        }
    }

    @Override
    public void cmdPressDialogNo(TypeShowDialog typeShowDialog) {
        super.cmdPressDialogNo(typeShowDialog);
        switch (typeShowDialog) {
            case TYPE_SHOW_MESSAGE_CONFIRM:
                if(AppCore.getVtcModelUser() != null && vtcModelOrder != null) {
                    initCancelOrderMark(FMRepairerSearch.this, AppCore.getVtcModelUser().get_id(), vtcModelOrder.getId(), "");
                }
                break;
            case TYPE_SHOW_MESSAGE_CHANGE_ORDER_TIME:
                initBack(CLOSE_BACK);
                break;
        }
    }

    private void initBack(int closeType){
        if(timerReSearch != null) {
            timerReSearch.cancel();
            timerReSearch = null;
        }
        if(timerWaitReview != null) {
            timerWaitReview.cancel();
            timerWaitReview = null;
        }
        if(timerTaskWaitReview != null) {
            timerTaskWaitReview.cancel();
            timerTaskWaitReview = null;
        }
        if(callback != null) {
            callback.onHandlerCallBack(closeType);
        }

        cmdBack();
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        switch (keyType) {
            case TYPE_ACTION_GET_CREATE_ORDER:

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    vtcModelOrder = VtcModelOrder.getOrderData(jsonObject);
                    if (vtcModelOrder != null) {

                        vtcModelOrder.setPayment_method(vtcModelNewOrder.getPaytype());
                        vtcModelOrder.setImages(vtcModelNewOrder.getLstImageUri());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case TYPE_ACTION_CANCEL_ORDER_MARK:

                initBack(CLOSE_BACK);
                break;
        }
    }

    @Override
    public void onPostExecuteError(TypeActionConnection keyTypeAction, TypeErrorConnection keyType, String msg) {
        super.onPostExecuteError(keyTypeAction, keyType, msg);
        initBack(CLOSE_BACK);
    }

    private void initViewNearBy(String response) {

        if (vtcModelNewOrder != null) {

            List<VtcModelAgencyNearBy> lst = VtcModelAgencyNearBy.getDataJson(response);
            if (lst != null && lst.size() > 0) {

                int[] width = Utils.getSizeScreen(getActivity());

                mAdapter = new AdtSearchRepairer(getActivity(), lst, width[0]);
                mRecyclerView.setAdapter(mAdapter);

                lout_container_search_repairer.setVisibility(LinearLayout.GONE);
            }
        }
    }

    @Override
    protected void cmdOnResponseListAgency(String response) {
        super.cmdOnResponseListAgency(response);
        initViewNearBy(response);
    }

    @Override
    protected void cmdOnResponseAcceptOrder(String response) {
        super.cmdOnResponseAcceptOrder(response);

        if(vtcModelOrder != null) {

            if(timerReSearch != null) {
                timerReSearch.cancel();
                timerReSearch = null;
            }
            if(timerTaskReSearch != null) {
                timerTaskReSearch.cancel();
                timerTaskReSearch = null;
            }

            try {
                JSONObject jsonObject = new JSONObject(response);

                VtcModelOrder.Agency agency = VtcModelOrder.Agency.getAgency(jsonObject);
                vtcModelOrder.setAgency(agency);
                vtcModelOrder.setStatus(StatusBookingJob.STATUS_COMING.getValuesStatus());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            orderAccepted = true;

            finishWorking();
        }
    }

    private void finishWorking() {
        if (!isPaused && orderAccepted) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Const.KEY_BUNDLE_ACTION_NEW_ORDER, vtcModelOrder);
            bundle.putBoolean(Const.KEY_BUNDLE_ACTION_VIEW, true);
            AppCore.CallFragmentSectionWithCallback(Const.UI_REPAIRER_INFO, bundle, new Callback() {
                @Override
                public <T> void onHandlerCallBack(int key, T... t) {
                    initBack(CLOSE_DONE);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isPaused = false;
        finishWorking();
        getCurrentActivity().setOnBackListenner(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        isPaused = true;
        getCurrentActivity().setOnBackListenner(null);
    }

    @Override
    public void onBack() {
        // do nothing
    }
}
