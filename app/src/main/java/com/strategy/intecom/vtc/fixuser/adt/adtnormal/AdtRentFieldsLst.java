package com.strategy.intecom.vtc.fixuser.adt.adtnormal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strategy.intecom.vtc.fixuser.R;

/**
 * Created by Luan Pham on 5/30/6.
 */
public class AdtRentFieldsLst extends RecyclerView.Adapter<AdtRentFieldsLst.ViewHolder> {
    private onClickItem onClickItem;

    public AdtRentFieldsLst.onClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(AdtRentFieldsLst.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
        }
    }

    public AdtRentFieldsLst() {
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdtRentFieldsLst.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tmp_call_fixer, parent, false);
        ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getOnClickItem().onClickIten();
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        if(holder.mTextView.getParent()!=null)
//            ((ViewGroup)holder.mTextView.getParent()).removeView(holder.mTextView);

    }

    @Override
    public int getItemCount() {
        return 50;
    }

    public interface onClickItem {
        void onClickIten();
    }

}

