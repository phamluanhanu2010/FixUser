package com.strategy.intecom.vtc.fixuser.adt.adtnormal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.model.VtcModelAddress;

import java.util.List;

/**
 * Created by Luan Pham on 5/30/6.
 */
public class AdtSearchLocationHistory extends RecyclerView.Adapter<AdtSearchLocationHistory.ViewHolder> {

    private Context context;
    private onClickItem onClickItem;
    private List<VtcModelAddress> lst;

    public AdtSearchLocationHistory.onClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(AdtSearchLocationHistory.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_distance;
        private TextView tv_address;
        private TextView tv_description;
        private CheckBox check_save;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tv_distance = (TextView) itemLayoutView.findViewById(R.id.tv_distance);
            tv_address = (TextView) itemLayoutView.findViewById(R.id.tv_address);
            tv_description = (TextView) itemLayoutView.findViewById(R.id.tv_description);
            check_save = (CheckBox) itemLayoutView.findViewById(R.id.check_save);
        }
    }

    public AdtSearchLocationHistory(Context context, List<VtcModelAddress> lst) {
        this.lst = lst;
        this.context = context;
    }

    public void setData(List<VtcModelAddress> lst){
        this.lst = lst;
    }

    @Override
    public AdtSearchLocationHistory.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tmp_search_location_history, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(lst != null) {
            final VtcModelAddress vtcModelAddress = lst.get(position);

            if(vtcModelAddress != null) {
                holder.tv_distance.setText(String.valueOf(vtcModelAddress.getDistance() + " Km"));
                holder.tv_address.setText(vtcModelAddress.getAddress());
                holder.tv_description.setText(vtcModelAddress.getDescription());
                holder.check_save.setChecked(vtcModelAddress.isSave());
                holder.check_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        vtcModelAddress.setSave(holder.check_save.isChecked());

                        getOnClickItem().onClickItem(vtcModelAddress);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getOnClickItem().onClickItemAddress(vtcModelAddress);
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
        void onClickItem(VtcModelAddress modelAddress);
        void onClickItemAddress(VtcModelAddress modelAddress);
    }

}

