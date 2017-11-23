package com.strategy.intecom.vtc.fixuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNewOrder;

/**
 * Created by Luan Pham on 5/30/6.
 */
public class DLPaytype extends Dialog implements View.OnClickListener {

    private Context context;
    private int width = 0;

    public DLPaytype(Context context, int width) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.width = (int) (width - (context.getResources().getDimension(R.dimen.confirm_ui_padding_w) * 2));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dl_paytype);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        setCancelable(false);
        initControler();

    }

    public void initControler() {

        ImageView btnExit = (ImageView) this.findViewById(R.id.btn_Exit);
        btnExit.setOnClickListener(this);

        Button btnCash = (Button) this.findViewById(R.id.btn_Cash);
        btnCash.setOnClickListener(this);

        Button btnVTCPay = (Button) this.findViewById(R.id.btn_VTCPay);
        btnVTCPay.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Exit:
                this.dismiss();
                break;
            case R.id.btn_Cash:
                getOnClick().onClickItem(VtcModelNewOrder.TYPE_BOOKING_PAY_TIENMAT);
                this.dismiss();
                break;
            case R.id.btn_VTCPay:
                getOnClick().onClickItem(VtcModelNewOrder.TYPE_BOOKING_PAY_VTC);
                this.dismiss();
                break;
            default:
                return;
        }
    }

    private onClickItem onClick;

    public DLPaytype.onClickItem getOnClick() {
        return onClick;
    }

    public void setOnClick(DLPaytype.onClickItem onClick) {
        this.onClick = onClick;
    }

    public interface onClickItem {
        void onClickItem(String type);
    }


}
