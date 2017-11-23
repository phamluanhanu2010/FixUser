package com.strategy.intecom.vtc.fixuser.adt.adtnormal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.enums.StatusBookingJob;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNewOrder;
import com.strategy.intecom.vtc.fixuser.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

import java.util.List;

/**
 * Created by Luan Pham on 5/30/6.
 */
public class AdtNavBookingScheduleLst extends RecyclerView.Adapter<AdtNavBookingScheduleLst.ViewHolder> {

    private Context context;
    private List<VtcModelOrder> lstOrder;
    private onClickItem onClickItem;

    public AdtNavBookingScheduleLst.onClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(AdtNavBookingScheduleLst.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView btn_History_VoucherID;
        private TextView txt_History_Date;
        private TextView txt_BookingShedule_Address;
        private TextView txt_BookingShedule_TypePay;
        private TextView txt_BookingShedule_Custom;
        private View view;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.view = itemLayoutView;
            btn_History_VoucherID = (TextView) itemLayoutView.findViewById(R.id.btn_History_VoucherID);
            txt_History_Date = (TextView) itemLayoutView.findViewById(R.id.txt_History_Date);
            txt_BookingShedule_Address = (TextView) itemLayoutView.findViewById(R.id.txt_BookingShedule_Address);
            txt_BookingShedule_TypePay = (TextView) itemLayoutView.findViewById(R.id.txt_BookingSchedule_TypePay);
            txt_BookingShedule_Custom = (TextView) itemLayoutView.findViewById(R.id.txt_BookingShedule_Custom);
        }
    }

    public AdtNavBookingScheduleLst(Context context, List<VtcModelOrder> lstOrder) {
        this.context = context;
        this.lstOrder = lstOrder;
    }

    @Override
    public AdtNavBookingScheduleLst.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tmp_bookingschedule, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(lstOrder != null){

            final VtcModelOrder vtcModelOrder = lstOrder.get(position);
            if(vtcModelOrder != null){

                holder.txt_History_Date.setText(context.getResources().getString(R.string.title_info_my_job_time, Utils.initConvertTimeDisplayHistory(vtcModelOrder.getOrder_time())));
                holder.txt_BookingShedule_Address.setText(vtcModelOrder.getAddress() == null ? "" : vtcModelOrder.getAddress().getName());

                vtcModelOrder.setPayment_method(VtcModelNewOrder.TYPE_BOOKING_PAY_TIENMAT);

                holder.txt_BookingShedule_TypePay.setText(context.getResources().getString(R.string.nav_History_Favarite_content_Paytype));
                holder.txt_BookingShedule_Custom.setText(vtcModelOrder.getField() == null ? "" : vtcModelOrder.getField().getCategory_name() + " - " + vtcModelOrder.getField().getName());

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

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getOnClickItem().onClickIten(vtcModelOrder);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if(lstOrder ==  null){
            return 0;
        }
        return lstOrder.size();
    }

    public interface onClickItem {
        void onClickIten(VtcModelOrder vtcModelOrder);
    }

}

