package com.strategy.intecom.vtc.fixuser.adt.adtnormal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.model.VtcModelFields;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad.MediaAsync;

import java.util.List;

/**
 * Created by Luan Pham on 5/30/6.
 */
public class AdtFixFieldsLst extends RecyclerView.Adapter<AdtFixFieldsLst.ViewHolder> {

    private onClickItem onClickItem;
    private Context context;

    private List<VtcModelFields> lst;
    private int checkedID = -1;

    private int width = 0;

    public AdtFixFieldsLst.onClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(AdtFixFieldsLst.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout lout_container;
        private ImageView img_Icon;
        private TextView txt_Titile;
        private TextView txt_Detail;
        private ImageView imgChecked;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            lout_container = (LinearLayout) itemLayoutView.findViewById(R.id.lout_container);
            img_Icon = (ImageView) itemLayoutView.findViewById(R.id.img_Icon);
            txt_Titile = (TextView) itemLayoutView.findViewById(R.id.txt_Titile);
            txt_Detail = (TextView) itemLayoutView.findViewById(R.id.txt_Detail);
            imgChecked = (ImageView) itemLayoutView.findViewById(R.id.checked);

        }
    }

    public AdtFixFieldsLst(Context context) {
        this.context = context;
        if(context != null) {
            this.width = (int) context.getResources().getDimension(R.dimen.size_icon_in_app_menu);
        }
    }

    public void initSetData(List<VtcModelFields> lst){
        this.lst = lst;
    }

    @Override
    public AdtFixFieldsLst.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tmp_call_fixer, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (lst != null) {
            final VtcModelFields vtcModelFields = lst.get(position);
            if (vtcModelFields != null) {

                holder.txt_Titile.setText(vtcModelFields.getName());
                holder.txt_Detail.setText(vtcModelFields.getDescription());
                holder.imgChecked.setVisibility((position == checkedID)? View.VISIBLE: View.GONE);

                ImageLoadAsync loadAsync = new ImageLoadAsync(context, holder.img_Icon, width);
                loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, vtcModelFields.getImage());

                holder.lout_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getOnClickItem().onClickIten(vtcModelFields);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        if(lst == null){
            return 0;
        }
        return lst.size();
    }

    public void setChecked(String field) {
        for (int i = 0; i < lst.size(); i++) {
            if (lst.get(i).getName().equals(field)) {
                checkedID = i;
                notifyDataSetChanged();
                return;
            }
        }
        checkedID = -1;
    }

    public interface onClickItem {
        void onClickIten(VtcModelFields vtcModelFields);
    }
}

