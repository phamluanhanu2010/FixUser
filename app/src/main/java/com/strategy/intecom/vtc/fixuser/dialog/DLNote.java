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
public class DLNote extends Dialog implements View.OnClickListener {
    private Context context;

    private int width = 0;

    public DLNote(Context context, int width) {
        super(context);
        this.context = context;
        this.width = (int) (width - (context.getResources().getDimension(R.dimen.confirm_ui_padding_w) * 2));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dl_note);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        setCancelable(false);

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
                this.dismiss();
                break;
            default:
                return;
        }
    }
}
