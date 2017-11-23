package com.strategy.intecom.vtc.fixuser.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.adt.adtnormal.AdtReasonLst;
import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixuser.interfaces.Callback;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 6/2/16.
 */

public class FMRepairerInfoCancel extends AppCore implements View.OnClickListener {

    private View viewRoot;

    private ListView mRecyclerView;
    private AdtReasonLst mAdapter;

    private Button btn_confirm;
    private ImageView btn_back;
    private TextView tv_title;

    private EditText edt_lydo;

    private Callback callback;

    private String idOrder = "";
    private String strReason = "";

    public FMRepairerInfoCancel() {

    }

    @SuppressLint("ValidFragment")
    public FMRepairerInfoCancel(Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            idOrder = savedInstanceState.getString(Const.KEY_BUNDLE_ACTION_ID_ORDER);
        }
        viewRoot = inflater.inflate(R.layout.ui_repairer_info_cancel, container, false);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initGetCommonInfo(this);

        initControler(view);
    }

    private void initControler(View view) {

        btn_back = (ImageView) view.findViewById(R.id.btn_back);
        tv_title = (TextView) view.findViewById(R.id.tv_title);

        mRecyclerView = (ListView) view.findViewById(R.id.my_recycler_view);

        btn_confirm = (Button) view.findViewById(R.id.btn_confirm);

        edt_lydo = (EditText) view.findViewById(R.id.edt_lydo);

        initEvent();
    }

    private void initEvent() {

        btn_confirm.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        initData();
    }

    private void initData() {

        tv_title.setText(getResources().getString(R.string.title_bar_info_service));

    }

    public String getEdt_lydo() {
        if (edt_lydo != null && !edt_lydo.getText().toString().trim().isEmpty()) {
            strReason = edt_lydo.getText().toString().trim();
        }

        return strReason;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (AppCore.getVtcModelUser() != null) {
                    if (getEdt_lydo().isEmpty()) {
                        initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getActivity().getResources().getString(R.string.validate_reasion_empty));
                    } else {
                        initCancelOrderMark(FMRepairerInfoCancel.this, AppCore.getVtcModelUser().get_id(), idOrder, getEdt_lydo());
                    }
                }
                break;
            case R.id.btn_back:
                Const.UI_CURRENT_CONTEXT = Const.UI_REPAIRER_INFO;
                cmdBack();
                break;
        }
    }

    @Override
    public void onPostExecuteError(TypeActionConnection keyTypeAction, TypeErrorConnection keyType, String msg) {
        super.onPostExecuteError(keyTypeAction, keyType, msg);
        switch (keyTypeAction) {
            case TYPE_ACTION_CANCEL_ORDER_MARK:

                if (callback != null) {
                    callback.onHandlerCallBack(1, "");
                }

                // back 2 steps to booking schedule screen
                cmdBack(2);
                break;
            case TYPE_ACTION_GET_LIST_COMMONINFO:
                break;
        }

    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType, String response, String message) {
        super.onPostExecuteSuccess(keyType, response, message);
        switch (keyType) {
            case TYPE_ACTION_CANCEL_ORDER_MARK:

                if (callback != null) {
                    callback.onHandlerCallBack(1, "");
                }

                initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO, "", getActivity().getResources().getString(R.string.confirm__repairer_info_message));

                // back 2 steps to booking schedule screen
                cmdBack(2);
                break;
            case TYPE_ACTION_GET_LIST_COMMONINFO:


                List<String> data = getCancelList(response);
//                AppCoreBase.showLog(data.toString());

                mAdapter = new AdtReasonLst(getActivity(), data);
                mAdapter.setOnClickItem(new AdtReasonLst.onClickItem() {
                    @Override
                    public void onClickItem(String str) {
                        strReason = str;
                    }
                });
                mRecyclerView.setAdapter(mAdapter);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Utils.hideKeyboard(getCurrentActivity());
    }

    private List<String> getCancelList(String respond) {
        try {
            JSONObject jsonObject = new JSONObject(respond);
            JSONArray array = jsonObject.getJSONArray("cancel_reasons");
            if (array != null) {
                List<String> list = new ArrayList<String>();
                for(int i = 0; i < array.length(); i++){
                    list.add(array.getString(i));
                }
                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void cmdOnCancelJob() {
        super.cmdOnCancelJob();
        getCurrentActivity().getSupportFragmentManager().popBackStack(1, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}
