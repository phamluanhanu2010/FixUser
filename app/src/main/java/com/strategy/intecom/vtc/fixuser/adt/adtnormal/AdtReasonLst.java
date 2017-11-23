package com.strategy.intecom.vtc.fixuser.adt.adtnormal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;

import java.util.List;

/**
 * Created by Luan Pham on 5/30/6.
 */
public class AdtReasonLst extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private onClickItem onClickItem;
    private List<String> lstField;
    private String strChoice = "";


    public AdtReasonLst(Context context, List<String> lstField) {
        this.context = context;
        this.lstField = lstField;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public String getItem(int position) {
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

            convertView = mLayoutInflater.inflate(R.layout.tmp_cancel_reason, parent, false);
            viewHolder = new ViewHolder(convertView);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final String item = getItem(position);

        if (item != null) {

            if(strChoice.equals(item)){
                viewHolder.checkBox.setChecked(true);
            }else {
                viewHolder.checkBox.setChecked(false);
            }

            viewHolder.tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHolder.checkBox.isChecked()) {
                        viewHolder.checkBox.setChecked(false);

                        getOnClickItem().onClickItem("");
                    } else {
                        viewHolder.checkBox.setChecked(true);
                        getOnClickItem().onClickItem(item);
                    }

                    strChoice = item;

                    notifyDataSetChanged();
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHolder.checkBox.isChecked()) {
                        viewHolder.checkBox.setChecked(false);
                        getOnClickItem().onClickItem("");
                    } else {
                        viewHolder.checkBox.setChecked(true);
                        getOnClickItem().onClickItem(item);
                    }
                    strChoice = item;

                    getOnClickItem().onClickItem(item);

                    notifyDataSetChanged();
                }
            });

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (viewHolder.checkBox.isChecked()) {
                        getOnClickItem().onClickItem(item);
                    } else {
                        getOnClickItem().onClickItem("");
                    }

                    strChoice = item;

                    getOnClickItem().onClickItem(item);

                    notifyDataSetChanged();
                }
            });

            viewHolder.tv_title.setText(item);
        }
        return convertView;
    }


    public static class ViewHolder {

        private CheckBox checkBox;
        private TextView tv_title;

        public ViewHolder(View itemLayoutView) {
            itemLayoutView.setTag(this);
            checkBox = (CheckBox) itemLayoutView.findViewById(R.id.checkBox);
            tv_title = (TextView) itemLayoutView.findViewById(R.id.tv_title);
        }
    }

    public interface onClickItem {
        void onClickItem(String str);
    }
}

