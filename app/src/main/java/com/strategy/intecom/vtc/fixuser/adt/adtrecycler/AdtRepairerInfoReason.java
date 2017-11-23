package com.strategy.intecom.vtc.fixuser.adt.adtrecycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strategy.intecom.vtc.fixuser.R;


/**
 * Created by Mr. Ha on 5/18/16.
 */
public class AdtRepairerInfoReason extends RecyclerView.Adapter<AdtRepairerInfoReason.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
        }
    }

    public AdtRepairerInfoReason() {
    }

    @Override
    public AdtRepairerInfoReason.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tmp_repairer_info_reason, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

}

