package com.strategy.intecom.vtc.fixuser.view;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.config.VtcNWConnection;
import com.strategy.intecom.vtc.fixuser.dialog.DLPopup;
import com.strategy.intecom.vtc.fixuser.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixuser.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixuser.interfaces.RequestListener;
import com.strategy.intecom.vtc.fixuser.utils.ParserJson;
import com.strategy.intecom.vtc.fixuser.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.socket.client.Socket;

/**
 * Created by Luan Pham on 6/7/16.
 */
public class UISupport extends Activity implements View.OnClickListener, RequestListener {
    private EditText mFeedbackContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_support);

        mFeedbackContent = (EditText) findViewById(R.id.etxt_Support_Feedback);

        initControler();
    }

    private void initControler() {
        ImageView btnExit = (ImageView)findViewById(R.id.btn_back);
        btnExit.setOnClickListener(this);

        Button btnCallSupport = (Button)findViewById(R.id.btn_Call_Support);
        btnCallSupport.setOnClickListener(this);
        Button btnSupportAccept = (Button)findViewById(R.id.btn_Support_Accept);
        btnSupportAccept.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_Support_Accept:
                sendFeedback();
                this.finish();
                break;
            case R.id.btn_Call_Support:
//                int []size = Utils.getSizeScreen(this);
//                DLPopup dlPopup = new DLPopup(this, size[0]);
//                TextView sad = (TextView)findViewById(R.id.txt_Edit_Profile_Notification);
//                sad.setText("dsgsdhsdkfhfhsdkhfksdhkjfhs");
//                dlPopup.show();
                break;
            default:
                return;
        }
    }

    private void sendFeedback() {

        String message = mFeedbackContent.getText().toString();

        if (TextUtils.isEmpty(message)) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(ParserJson.API_PARAMETER_AUTH_TOKEN,
                AppCore.getPreferenceUtil(this)
                        .getValueString(PreferenceUtil.AUTH_TOKEN));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject
                    .put(ParserJson.API_PARAMETER_NAME, AppCore.getVtcModelUser
                            ().getName());
            jsonObject.put(ParserJson.API_PARAMETER_PHONE, AppCore.getVtcModelUser
                    ().getPhone());
            jsonObject.put(ParserJson.API_PARAMETER_MESSAGE, message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AppCore.getConnection(this).onExcuteProcess
                (TypeActionConnection.TYPE_ACTION_SEND_FEEDBACK,
                        RequestListener
                                .API_CONNECTION_SEND_FEEDBACK +
                                VtcNWConnection
                                        .urlEncodeUTF8
                                                (map),
                        jsonObject);
    }

    @Override
    public void onPostExecuteError(TypeActionConnection keyTypeAction,
            TypeErrorConnection keyType, String msg) {
        Toast.makeText(UISupport.this, "Thông báo của bạn bị lỗi trong khi gửi. Hãy kiểm tra lại kết nối mạng của mình!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPostExecuteSuccess(TypeActionConnection keyType,
            String response, String message) {
        Toast.makeText(UISupport.this, "Thông báo của bạn đã được gửi!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResultSocket(TypeActionConnection keyType, Socket socket,
            String response) {

    }
}
