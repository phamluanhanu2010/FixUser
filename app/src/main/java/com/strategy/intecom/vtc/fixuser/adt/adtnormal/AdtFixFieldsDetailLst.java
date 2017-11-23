package com.strategy.intecom.vtc.fixuser.adt.adtnormal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.model.VtcModelFields;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad.MediaAsync;

import java.util.List;

/**
 * Created by Luan Pham on 5/30/6.
 */
public class AdtFixFieldsDetailLst extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private onClickItem onClickItem;
    private List<VtcModelFields> lstField;
    private int width = 0;

    public AdtFixFieldsDetailLst(Context context, List<VtcModelFields> lstField) {
        if (context == null) {
            throw new IllegalArgumentException("context could not be null!");
        }
        this.context = context;
        this.lstField = lstField;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.width = (int) context.getResources().getDimension(R.dimen.size_icon_in_app_menu);
    }

    public onClickItem getOnClickItem() throws NullPointerException {
        return onClickItem;
    }

    public void setOnClickItem(onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    @Override
    public int getCount() {
        if(lstField == null){
            return 0;
        }
        return lstField.size();
    }

    @Override
    public VtcModelFields getItem(int position) {
        if(lstField == null){
            return null;
        }
        return lstField.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {

            convertView = mLayoutInflater.inflate(R.layout.tmp_fixfields_detail, parent, false);
            viewHolder = new ViewHolder(convertView);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final VtcModelFields vtcModelFields = getItem(position);

        if (vtcModelFields != null) {

            viewHolder.tv_title.setSelected(true);

            viewHolder.checkBox.setChecked(vtcModelFields.isChoice());
            ImageLoadAsync loadAsync = new ImageLoadAsync(context, viewHolder.img_Icon, width);
            loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, vtcModelFields.getImage());

            viewHolder.tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.checkBox.setChecked(true);

                    initCheck(vtcModelFields.getId(), viewHolder.checkBox.isChecked());

                    getOnClickItem().onClickItem(vtcModelFields);
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.checkBox.setChecked(true);

                    initCheck(vtcModelFields.getId(), viewHolder.checkBox.isChecked());

                    getOnClickItem().onClickItem(vtcModelFields);
                }
            });

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    initCheck(vtcModelFields.getId(), viewHolder.checkBox.isChecked());

                    getOnClickItem().onClickItem(vtcModelFields);
                }
            });

            viewHolder.tv_title.setText(vtcModelFields.getName());
        }
        return convertView;
    }

    private void initCheck(String id, boolean isCheck){
        if(getCount() > 0) {
            for (VtcModelFields vtcModelFields : lstField){
                if(vtcModelFields.getId().equals(id)){
                    vtcModelFields.setChoice(isCheck);
                }else {
                    vtcModelFields.setChoice(false);
                }
            }
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder {

        private CheckBox checkBox;
        private TextView tv_title;
        private ImageView img_Icon;

        public ViewHolder(View itemLayoutView) {
            itemLayoutView.setTag(this);
            checkBox = (CheckBox) itemLayoutView.findViewById(R.id.checkBox);
            tv_title = (TextView) itemLayoutView.findViewById(R.id.tv_title);
            img_Icon = (ImageView) itemLayoutView.findViewById(R.id.img_Icon);
        }
    }

    public interface onClickItem {
        void onClickItem(VtcModelFields vtcModelFields);
    }
}

