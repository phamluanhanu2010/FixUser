package com.strategy.intecom.vtc.fixuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.enums.StatusBookingJob;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNewOrder;
import com.strategy.intecom.vtc.fixuser.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad.MediaAsync;

/**
 * Created by Mr. Ha on 6/1/16.
 */
public class DlRatingRepairer extends Dialog {

    private Context context;

    private VtcModelOrder vtcModelOrder;

    private LinearLayout lout_container;
    private RelativeLayout lout_Repairer_Infor;
    private TextView tv_title_message;
    private TextView tv_info;
    private ImageView img_avatar;
    private TextView tv_Repairer_Name;
    private TextView tv_Rank;
    private ImageView img_Rank_Icon;

    public DlRatingRepairer(Context context, VtcModelOrder vtcModelOrder) {
        super(context, R.style.DialogTheme);
        this.vtcModelOrder = vtcModelOrder;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dl_rating_repairer);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setCancelable(false);

        initController();
    }

    private void initController() {

        lout_container = (LinearLayout) findViewById(R.id.lout_container);

        if (vtcModelOrder != null) {
            lout_Repairer_Infor = (RelativeLayout) findViewById(R.id.lout_Repairer_Infor);
            tv_title_message = (TextView) findViewById(R.id.tv_title_message);
            tv_info = (TextView) findViewById(R.id.tv_info);

            lout_Repairer_Infor.setVisibility(RelativeLayout.GONE);
            tv_info.setVisibility(TextView.GONE);

            String message = "";
            String message_content = "";

            if (vtcModelOrder.getStatus().equals(StatusBookingJob.STATUS_ACCEPTED.getValuesStatus())) {
                lout_Repairer_Infor.setVisibility(RelativeLayout.VISIBLE);
                message = context.getResources().getString(R.string.title_rating_repairer);
            } else if (vtcModelOrder.getStatus().equals(StatusBookingJob.STATUS_COMING.getValuesStatus())) {
                lout_Repairer_Infor.setVisibility(RelativeLayout.VISIBLE);

                if (vtcModelOrder.getType().equals(VtcModelNewOrder.TYPE_BOOKING_FAST)) {
                    message = context.getResources().getString(R.string.title_rating_repairer_goto);
                } else {
                    message = context.getResources().getString(R.string.title_rating_repairer_goto);
                }
            } else if (vtcModelOrder.getStatus().equals(StatusBookingJob.STATUS_WORKING.getValuesStatus())) {
                tv_info.setVisibility(TextView.VISIBLE);
                message = context.getResources().getString(R.string.title_rating_repairer_start);
                message_content = context.getResources().getString(R.string.title_rating_repairer_start_content);
            } else if (vtcModelOrder.getStatus().equals(StatusBookingJob.STATUS_USER_CANCEL.getValuesStatus())) {
                tv_info.setVisibility(TextView.VISIBLE);
                message = context.getResources().getString(R.string.title_rating_repairer_cancel);
                message_content = context.getResources().getString(R.string.title_rating_repairer_cancel_content);
            } else if (vtcModelOrder.getStatus().equals(StatusBookingJob.STATUS_AGENCY_CANCEL.getValuesStatus())) {
                tv_info.setVisibility(TextView.VISIBLE);
                message = context.getResources().getString(R.string.title_rating_repairer_agency_cancel);

                VtcModelOrder.Field field = vtcModelOrder.getField();
                if(field != null) {
                    message_content = context.getResources().getString(R.string.title_rating_repairer_agency_cancel_content, Utils.initTextBold(field.getName() + " - " + field.getCategory_name()));
                }
            }

            tv_info.setText(Html.fromHtml(message_content));
            tv_title_message.setText(message);

            if (vtcModelOrder.getAgency() != null) {

                VtcModelOrder.Agency agency = vtcModelOrder.getAgency();

                img_avatar = (ImageView) findViewById(R.id.img_avatar);
                tv_Repairer_Name = (TextView) findViewById(R.id.tv_Repairer_Name);
                tv_Rank = (TextView) findViewById(R.id.tv_Rank);
                img_Rank_Icon = (ImageView) findViewById(R.id.img_Rank_Icon);

                ImageLoadAsync loadAsyncAvatar = new ImageLoadAsync(context, img_avatar, 100);
                loadAsyncAvatar.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, String.valueOf(agency.getAvatar()));

                tv_Repairer_Name.setText(agency.getName());

                initRate(agency);
            }
        }

        lout_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    private void initRate(VtcModelOrder.Agency agency) {
        String sLV = agency.getLevel();
        if (sLV.equals("")) {
            sLV = "0";
        }
        int level = Integer.parseInt(sLV);

        switch (level) {
            case 1:
                // Thợ cấp 1
                sLV = context.getResources().getString(R.string.title_rating_level_1);
                level = R.drawable.ic_level_1;
                break;

            case 2:
                // Thợ cấp 2
                sLV = context.getResources().getString(R.string.title_rating_level_2);
                level = R.drawable.ic_level_2;
                break;

            case 3:
                // Thợ cấp 3
                sLV = context.getResources().getString(R.string.title_rating_level_3);
                level = R.drawable.ic_level_3;
                break;

            case 4:
                // Thợ cấp 4
                sLV = context.getResources().getString(R.string.title_rating_level_4);
                level = R.drawable.ic_level_4;
                break;

            case 5:
                // Thợ cấp 5
                sLV = context.getResources().getString(R.string.title_rating_level_5);
                level = R.drawable.ic_level_5;
                break;
        }

        tv_Rank.setText(sLV);
        img_Rank_Icon.setBackgroundResource(level);

    }
}
