package com.strategy.intecom.vtc.fixuser.adt.adtnormal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNotification;
import com.strategy.intecom.vtc.fixuser.utils.Const;
import com.strategy.intecom.vtc.fixuser.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luan Pham on 5/30/6.
 */
public class AdtNavNotificationLst extends RecyclerView.Adapter<AdtNavNotificationLst.ViewHolder> {
    private final ArrayList<VtcModelNotification> mNotiList;
    private OnClickItem mOnClickItem;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTxtTitle;
        private TextView mTxtMessageTime;
        private ImageView mImgRead;
        private View.OnClickListener mOnClickListener;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            mTxtTitle = (TextView) itemLayoutView.findViewById(R.id
                    .txt_Title);
            mTxtMessageTime = (TextView) itemLayoutView.findViewById(R.id
                    .txt_Message);
            mImgRead = (ImageView) itemLayoutView.findViewById(R.id.img_readed);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onClick(v);
        }

        public View.OnClickListener getOnClickListener() {
            return mOnClickListener;
        }

        public void setOnClickListener(
                View.OnClickListener onClickListener) {
            this.mOnClickListener = onClickListener;
        }
    }

    public AdtNavNotificationLst(ArrayList<VtcModelNotification>
            notiList) {
        if (notiList == null) {
            throw new IllegalArgumentException("Argument could not be null!");
        }

        mNotiList = notiList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdtNavNotificationLst.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tmp_notification, parent, false);
        ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(new CustomOnClickListener(vh));

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mNotiList != null) {
            final VtcModelNotification noti = mNotiList.get(position);

            if (noti != null) {
                List<VtcModelNotification.Message> messages = noti
                        .getMessages();
                VtcModelNotification.Message msg = null;
                if (messages != null && messages.size() > 0) {
                    msg = messages.get(messages.size() - 1);
                }
                if (msg != null) {
                    holder.mTxtMessageTime.setText(Utils
                            .initConvertTimeDisplayHistory(msg.getCreateAt()));
                    holder.mTxtTitle.setText(msg.getMessageContent());
                    holder.mImgRead.setVisibility(View.GONE);
                    holder.mTxtTitle.setAlpha(Const.ALPHA_DISABLE);

                    for (VtcModelNotification.Message message : noti.getMessages
                            ()) {
                        if (!message.isReaded() && Const
                                .KEY_MESSAGE_OWNER_ADMIN
                                .equalsIgnoreCase(message.getFrom())) {
                            holder.mImgRead.setVisibility(View.VISIBLE);
                            holder.mTxtTitle.setAlpha(Const.ALPHA_ENABLE);
                            break;
                        }
                    }
                }

                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnClickItem != null) {
                            mOnClickItem.onClickItem(mNotiList, noti.getId());
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mNotiList.size();
    }

    public OnClickItem getOnClickItem() {
        return mOnClickItem;
    }

    public void setOnClickItem(
            OnClickItem onClickItem) {
        this.mOnClickItem = onClickItem;
    }

    public interface OnClickItem {
        void onClickItem(ArrayList<VtcModelNotification> notiList, String noticeId);
    }

    private static class CustomOnClickListener implements View.OnClickListener {
        private View.OnClickListener mOnClickListener;

        public CustomOnClickListener(View.OnClickListener listener) {
            mOnClickListener = listener;
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v);
            }
        }
    }
}

