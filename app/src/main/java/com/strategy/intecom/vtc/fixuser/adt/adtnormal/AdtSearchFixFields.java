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
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad.MediaAsync;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMSearchFixFieldText;

import java.util.List;

/**
 * Created by Luan Pham on 5/30/6.
 */
public class AdtSearchFixFields extends RecyclerView.Adapter<AdtSearchFixFields.ViewHolder> {

    private Context context;
    private int width = 0;

    private List<FMSearchFixFieldText.Holder> lst;

    private OnClickItem onClickItem;

    public AdtSearchFixFields(Context context, List<FMSearchFixFieldText.Holder> list) {
        this.context = context;
        lst = list;
        if(context != null) {
            this.width = (int) context.getResources().getDimension(R.dimen.size_icon_in_app_menu);
        }
    }

    @Override
    public AdtSearchFixFields.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tmp_call_fixer, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (lst != null) {
            final FMSearchFixFieldText.Holder dataHolder = lst.get(position);
            if (dataHolder != null) {

                viewHolder.txt_Titile.setText(dataHolder.parent.getName());
                viewHolder.txt_Detail.setText(dataHolder.child.getName());

                ImageLoadAsync loadAsync = new ImageLoadAsync(context, viewHolder.img_Icon, width);
                loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, dataHolder.parent.getImage());

                viewHolder.lout_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickItem != null) {
                            onClickItem.onClickItem(dataHolder);
                        }
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

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public void updateData(List<FMSearchFixFieldText.Holder> lst) {
        this.lst = lst;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout lout_container;
        private ImageView img_Icon;
        private TextView txt_Titile;
        private TextView txt_Detail;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            lout_container = (LinearLayout) itemLayoutView.findViewById(R.id.lout_container);
            img_Icon = (ImageView) itemLayoutView.findViewById(R.id.img_Icon);
            txt_Titile = (TextView) itemLayoutView.findViewById(R.id.txt_Titile);
            txt_Detail = (TextView) itemLayoutView.findViewById(R.id.txt_Detail);
        }
    }

    public interface OnClickItem {
        void onClickItem(FMSearchFixFieldText.Holder vtcModelFields);
    }
}

