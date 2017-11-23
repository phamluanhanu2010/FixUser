package com.strategy.intecom.vtc.fixuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.strategy.intecom.vtc.fixuser.R;

/**
 * Created by PHAMLUAN on 5/31/2016.
 */
public class DLCancel extends Dialog implements View.OnClickListener {
    private Context context;

    private DLInputData.onClickAccept onClickAccept;

    public DLCancel(Context context) {
        super(context);
        this.context = context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dl_cancel);
        setCancelable(true);

        initControler();

    }

    public void initControler(){
        Button btnCancel = (Button)this.findViewById(R.id.btn_Cancel);
        btnCancel.setOnClickListener(this);
        Button btnAccept = (Button)this.findViewById(R.id.btn_Accept);
        btnAccept.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_Cancel:
                this.dismiss();
                break;
            case R.id.btn_Accept:
                getOnClickAccept().onClick(null);
                this.dismiss();
                break;
            default:
                return;
        }
    }

    public DLInputData.onClickAccept getOnClickAccept() {
        return onClickAccept;
    }

    public void setOnClickAccept(DLInputData.onClickAccept onClickAccept) {
        this.onClickAccept = onClickAccept;
    }
}
