package com.strategy.intecom.vtc.fixuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog;

/**
 * Created by PHAMLUAN on 5/31/2016.
 */
public class DLInputData extends Dialog implements View.OnClickListener {

    private Context context;
    private onClickAccept onClickAccept;
    private EditText etxt_input_promo;
    private EditText etxt_input_description;

    private int width = 0;

    private String content;

    private TypeShowDialog typeShowDialog;

    public DLInputData(Context context, TypeShowDialog typeShowDialog, String content, int width) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.typeShowDialog = typeShowDialog;
        this.content = content;
        this.width = (int) (width - (context.getResources().getDimension(R.dimen.confirm_ui_padding_w) * 2));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dl_input_data);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        setCancelable(false);

        initControler();

    }

    public void initControler(){

        etxt_input_promo = (EditText) findViewById(R.id.etxt_input_promo);
        etxt_input_description = (EditText) findViewById(R.id.etxt_input_description);

        Button btnCancel = (Button)this.findViewById(R.id.btn_cancel);
        Button btnAccept = (Button)this.findViewById(R.id.btn_Accept);

        btnCancel.setOnClickListener(this);
        btnAccept.setOnClickListener(this);


        switch (typeShowDialog){
            case TYPE_SHOW_MESSAGE_INFO_NOTE:
                etxt_input_description.setText(content);
                etxt_input_promo.setVisibility(EditText.GONE);
                etxt_input_description.setVisibility(EditText.VISIBLE);
                break;

            case TYPE_SHOW_MESSAGE_INFO_PROMOTION:
                etxt_input_promo.setText(content);
                etxt_input_promo.setVisibility(EditText.VISIBLE);
                etxt_input_description.setVisibility(EditText.GONE);
                break;

        }
    }

    @Override
    public void show() {
        super.show();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                this.dismiss();
                break;
            case R.id.btn_Accept:

                String strData = "";

                switch (typeShowDialog) {
                    case TYPE_SHOW_MESSAGE_INFO_NOTE:
                        if (etxt_input_description != null) {
                            strData = etxt_input_description.getText().toString().trim();
                        }
                        break;

                    case TYPE_SHOW_MESSAGE_INFO_PROMOTION:
                        if (etxt_input_promo != null) {
                            strData = etxt_input_promo.getText().toString().trim();
                        }
                        break;

                }
                if (getOnClickAccept() != null) {

                    getOnClickAccept().onClick(strData);
                }
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

    public interface onClickAccept{
        void onClick(String str);
    }
}
