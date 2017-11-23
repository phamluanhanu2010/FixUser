package com.strategy.intecom.vtc.fixuser.adt.adtnormal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.config.VtcDBConnect;
import com.strategy.intecom.vtc.fixuser.enums.StatusBookingJob;
import com.strategy.intecom.vtc.fixuser.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Luan Pham on 5/30/6.
 */
public class AdtNavHistoryLst extends RecyclerView.Adapter<AdtNavHistoryLst.ViewHolder> {

    private Context context;
    private onClickItem onClickItem;
    private List<VtcModelOrder> lst;

    public AdtNavHistoryLst.onClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(AdtNavHistoryLst.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_History_Date;
        private TextView txt_History_Custom;
        private ImageView img_type;
        private TextView btn_History_VoucherID;
        private TextView txt_History_Address;
        private View itemLayoutView;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.itemLayoutView = itemLayoutView;
            txt_History_Date = (TextView) itemLayoutView.findViewById(R.id.txt_History_Date);
            txt_History_Custom = (TextView) itemLayoutView.findViewById(R.id.txt_History_Custom);
            img_type = (ImageView) itemLayoutView.findViewById(R.id.img_type);
            btn_History_VoucherID = (TextView) itemLayoutView.findViewById(R.id.btn_History_VoucherID);
            txt_History_Address = (TextView) itemLayoutView.findViewById(R.id.txt_History_Address);
        }
    }

    public AdtNavHistoryLst(Context context, List<VtcModelOrder> lst) {
        if (context == null) {
            throw new IllegalArgumentException("context could not be null");
        }
        this.lst = lst;
        this.context = context;
    }

    @Override
    public AdtNavHistoryLst.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tmp_history, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(lst != null) {
            final VtcModelOrder vtcModelOrder = lst.get(position);

            if(vtcModelOrder != null){
                holder.txt_History_Date.setText(Utils.initConvertTimeDisplayHistory(vtcModelOrder.getOrder_time()));
                holder.txt_History_Custom.setText(vtcModelOrder.getField() == null ? "" : vtcModelOrder.getField().getCategory_name() + " - " + vtcModelOrder.getField().getName());
                holder.txt_History_Address.setText(vtcModelOrder.getAddress() == null ? "" : vtcModelOrder.getAddress().getName());

                holder.btn_History_VoucherID.setVisibility(Button.VISIBLE);

                if(vtcModelOrder.getCoupon_code() != null && !vtcModelOrder.getCoupon_code().getCode().isEmpty()){
                    holder.btn_History_VoucherID.setText(context.getResources().getString(R.string.title_status_my_order_coupon));
                }else if(vtcModelOrder.getStatus().equals(StatusBookingJob.STATUS_FINDING.getValuesStatus())) {
                    holder.btn_History_VoucherID.setText(context.getResources().getString(R.string.title_status_my_order_waiting));
                }else if(vtcModelOrder.getStatus().equals(StatusBookingJob.STATUS_USER_CANCEL.getValuesStatus())) {
                    holder.btn_History_VoucherID.setText(context.getResources().getString(R.string.title_status_my_order_user_cancel));
                }else if(vtcModelOrder.getStatus().equals(StatusBookingJob.STATUS_AGENCY_CANCEL.getValuesStatus())) {
                    holder.btn_History_VoucherID.setText(context.getResources().getString(R.string.title_status_my_order_agency_cancel));
                }else if(vtcModelOrder.getStatus().equals(StatusBookingJob.STATUS_FINISH.getValuesStatus())) {
                    holder.btn_History_VoucherID.setText(context.getResources().getString(R.string.title_status_my_order_finish));
                }else if(vtcModelOrder.getStatus().equals(StatusBookingJob.STATUS_EXPIRED.getValuesStatus())) {
                    holder.btn_History_VoucherID.setText(context.getResources().getString(R.string.title_status_my_order_ex));
                }else if(vtcModelOrder.getStatus().equals(StatusBookingJob.STATUS_COMING.getValuesStatus())) {
                    holder.btn_History_VoucherID.setText(context.getResources().getString(R.string.title_status_my_order_coming));
                }else if(vtcModelOrder.getStatus().equals(StatusBookingJob.STATUS_WORKING.getValuesStatus())) {
                    holder.btn_History_VoucherID.setText(context.getResources().getString(R.string.title_status_my_order_working));
                }else {
                    holder.btn_History_VoucherID.setVisibility(Button.GONE);
                }

                holder.itemLayoutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getOnClickItem().onClickItem(vtcModelOrder);
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        if(lst == null) {
            return 0;
        }
        return lst.size();
    }

    public interface onClickItem {
        void onClickItem(VtcModelOrder vtcModelOrder);
    }

}

