package com.strategy.intecom.vtc.fixuser.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.adt.adtnormal.AdtNotificationMsgLst;
import com.strategy.intecom.vtc.fixuser.config.VtcNWConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixuser.interfaces.RequestListener;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNotification;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.ParserJson;
import com.strategy.intecom.vtc.fixuser.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.Socket;

/**
 * Created by Luan Pham on 6/7/16.
 */
public class UINotificationContent extends Activity implements View.OnClickListener, RequestListener {

    private RecyclerView mRecyclerView;
    private AdtNotificationMsgLst mAdapter;
    private ArrayList<VtcModelNotification> notiLst;
    private int currentId = 0;
    private ImageView btnExit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_notification_content);

        Bundle data = getIntent().getExtras();

        String notiId = data.getString(Const.KEY_EXTRACT_NOTI_ID);
        notiLst = data.<VtcModelNotification>getParcelableArrayList(
                Const.KEY_EXTRACT_NOTIFICATION);

        btnExit = (ImageView)findViewById(R.id.btn_back);
        btnExit.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager
                (UINotificationContent.this));

        for (int i = 0; i < notiLst.size(); i++) {
            if (notiId.equalsIgnoreCase(notiLst.get(i).getId())) {
                currentId = i;
                break;
            }
        }

        initController();
    }

    private void initController(){
        if (notiLst == null || notiLst.size()  == 0) {
            mRecyclerView.setAdapter(new AdtNotificationMsgLst(
                    new ArrayList<VtcModelNotification.Message>()));
            return;
        }

        VtcModelNotification noti = notiLst.get(currentId);
        if (noti == null) {
            return;
        }
        mAdapter = new AdtNotificationMsgLst(noti.getMessages());
        mRecyclerView.setAdapter(mAdapter);

        updateReaded(noti);
    }

    private void updateReaded(VtcModelNotification notification) {
        for (VtcModelNotification.Message msg :notification.getMessages()) {
                    if (!msg.isReaded()) {
                        Map<String, String> map = new HashMap<>();
                        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN,
                                AppCore.getPreferenceUtil(this)
                                        .getValueString(PreferenceUtil.AUTH_TOKEN));

                        AppCore.getConnection(this).onExcuteProcess
                                (TypeActionConnection.TYPE_ACTION_MESSAGE_READ,
                                        RequestListener
                                                .API_CONNECTION_MESSAGE_READ
                                        + notification.getId()+ "/markAsReaded" +
                                        VtcNWConnection
                                        .urlEncodeUTF8
                                        (map),
                                true);
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;
            default:
                return;
        }
    }

    @Override
    public void onPostExecuteError(TypeActionConnection keyTypeAction,
            TypeErrorConnection keyType, String msg) {
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType,
            String response, String message) {
    }

    @Override
    public void onResultSocket(TypeActionConnection keyType, Socket socket,
            String response) {

    }
}
