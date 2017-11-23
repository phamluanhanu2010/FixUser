package com.strategy.intecom.vtc.fixuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

/**
 * Created by Mr. Ha on 5/20/16.
 */
public class DlConfirmDialog extends Dialog {

    private TextView tv_message_content;
    private TextView btn_cancel;
    private TextView btn_ok;
    String mOKString;
    String mCancelString;

    private String content = "";

    private onClickDialogItem onClickDialogItem;

    private int width;

    public DlConfirmDialog(Context context) {
        super(context, R.style.DialogTheme);
        int screen[] = Utils.getSizeScreen(AppCore.getCurrentActivity());
        this.width = (int) (screen[0] - (context.getResources().getDimension(R.dimen.confirm_ui_padding_w) * 2));
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dl_message);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        setCancelable(false);

        initControler();

    }

    public void initControler() {

        tv_message_content = (TextView) findViewById(R.id.tv_message_content);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        btn_ok = (TextView) findViewById(R.id.btn_Accept);

        if (!TextUtils.isEmpty(mCancelString)) {
            btn_cancel.setText(mCancelString);
        }

        if (!TextUtils.isEmpty(mOKString)) {
            btn_ok.setText(mOKString);
        }
        initData();
    }

    private void initData() {

        tv_message_content.setText(content);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnClickDialogItem().onClickDialogItemCancel();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getOnClickDialogItem().onClickDialogItemOK();
            }
        });

    }

    public DlConfirmDialog.onClickDialogItem getOnClickDialogItem() {
        return onClickDialogItem;
    }

    public void setOnClickDialogItem(DlConfirmDialog.onClickDialogItem onClickDialogItem) {
        this.onClickDialogItem = onClickDialogItem;
    }

    public static class Builder {
        DlConfirmDialog dl;

        public Builder(Context context) {
            dl = new DlConfirmDialog(context);
        }

        public Builder content(String content) {
            dl.setContent(content);
            return this;
        }

        public Builder okString(String okString) {
            dl.mOKString = okString;
            return this;
        }

        public Builder cancelString(String cancelString) {
            dl.mCancelString = cancelString;
            return this;
        }

        public Builder onClickistener(onClickDialogItem onClickDialogItem) {
            dl.setOnClickDialogItem(onClickDialogItem);
            return this;
        }

        public DlConfirmDialog build() {
            return dl;
        }
    }

    public interface onClickDialogItem{
        void onClickDialogItemCancel();
        void onClickDialogItemOK();
    }

}
