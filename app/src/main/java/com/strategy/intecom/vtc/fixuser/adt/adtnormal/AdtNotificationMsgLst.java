package com.strategy.intecom.vtc.fixuser.adt.adtnormal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNotification;

import java.util.List;

/**
 * Created by Lugen Developer on 8/4/2016.
 */
public class AdtNotificationMsgLst extends RecyclerView
        .Adapter<AdtNotificationMsgLst.ViewHolder>{
    List<VtcModelNotification.Message> mMsgLst;

    public AdtNotificationMsgLst(
            List<VtcModelNotification.Message> msgLst) {
        if (msgLst == null) {
            throw new IllegalArgumentException("Argument could not be null!");
        }
        mMsgLst = msgLst;
    }


    @Override
    public AdtNotificationMsgLst.ViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tmp_notification_detail, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(AdtNotificationMsgLst.ViewHolder holder,
            int position) {
        VtcModelNotification.Message msg = mMsgLst.get(position);
        if (msg != null) {
            holder.mTxtFrom.setText(msg.getFrom());
            holder.mTxtDetail.setText(msg.getMessageContent());
        }
    }

    @Override
    public int getItemCount() {
        return mMsgLst.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTxtFrom;
        private TextView mTxtDetail;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            mTxtFrom = (TextView) itemLayoutView.findViewById(R.id.txt_from);
            mTxtDetail = (TextView) itemLayoutView.findViewById(R.id
                    .txt_detail);
        }
    }
}
