package com.strategy.intecom.vtc.fixuser.adt.adtnormal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.dialog.DLImage;
import com.strategy.intecom.vtc.fixuser.dialog.DlRatingRepairer;
import com.strategy.intecom.vtc.fixuser.enums.TypeShowDialog;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;

/**
 * Created by Luan Pham on 5/30/6.
 */
public class AdtRentFieldsDetailLst extends RecyclerView.Adapter<AdtRentFieldsDetailLst.ViewHolder> {
    private onClickItem onClickItem;


    public AdtRentFieldsDetailLst.onClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(AdtRentFieldsDetailLst.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckBox check_select;
        ImageView imgTool;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            check_select = (CheckBox) itemLayoutView.findViewById(R.id.check_select);
            imgTool = (ImageView) itemLayoutView.findViewById(R.id.img_Tool);
            imgTool.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_Tool:
                    /*if(isImageFitToScreen) {
                        isImageFitToScreen=false;
                        imgTool.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        imgTool.setAdjustViewBounds(true);

                    }else{
                        isImageFitToScreen=true;
                        imgTool.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        imgTool.setScaleType(ImageView.ScaleType.FIT_XY);
                    }*/
                    DLImage dl = new DLImage(v.getContext());
                    dl.show();

                    //initShowDialogOption(getActivity(), TypeShowDialog.TYPE_SHOW_MESSAGE_INFO_REPAIRER);

                    break;
                default:
                    return;
            }
        }
    }

    public AdtRentFieldsDetailLst() {
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdtRentFieldsDetailLst.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tmp_rentfields_detail, parent, false);
        final ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getOnClickItem().onClickIten();
                if (vh.check_select.isChecked()) {
                    vh.check_select.setChecked(false);
                } else {
                    vh.check_select.setChecked(true);
                }

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
        return 10;
    }

    public interface onClickItem {
        void onClickIten();
    }

}

