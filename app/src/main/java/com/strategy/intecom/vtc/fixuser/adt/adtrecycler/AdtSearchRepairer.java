package com.strategy.intecom.vtc.fixuser.adt.adtrecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.model.VtcModelAgencyNearBy;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad.MediaAsync;

import java.util.List;

/**
 * Created by Mr. Ha on 5/18/16.
 */
public class AdtSearchRepairer extends RecyclerView.Adapter<AdtSearchRepairer.ViewHolder> {
    private Context context;
    private onClickItem onClickItem;
    private List<VtcModelAgencyNearBy> lst;

    private int width = 0;

    public AdtSearchRepairer.onClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(AdtSearchRepairer.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_home;
        private TextView tv_description_app;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            img_home = (ImageView) itemLayoutView.findViewById(R.id.img_home);
            tv_description_app = (TextView) itemLayoutView.findViewById(R.id.tv_description_app);
        }
    }

    public AdtSearchRepairer(Context context, List<VtcModelAgencyNearBy> lst, int width) {
        this.context = context;
        this.lst = lst;
        this.width = width;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tmp_search_repairer, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(lst != null) {
            VtcModelAgencyNearBy modelAgencyNearBy = lst.get(position);
            if(modelAgencyNearBy != null){
                holder.tv_description_app.setText(modelAgencyNearBy.getName());

                ImageLoadAsync loadAsync = new ImageLoadAsync(context, holder.img_home, width);
                loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, String.valueOf(modelAgencyNearBy.getImage()));
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

    public interface onClickItem {
        void onClickIten();
    }

}

