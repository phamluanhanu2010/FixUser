package com.strategy.intecom.vtc.fixuser.adt.adtnormal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ImageLoadAsync;
import com.strategy.intecom.vtc.fixuser.view.custom.loadimage.ultilLoad.MediaAsync;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 5/27/16.
 */
public class AdtImageOrder extends BaseAdapter {

    private List<String> items;

    private List<String> itemsBase64;

    private LayoutInflater mLayoutInflater;

    private Context context;

    private int width = 0;

    private onClickItem onClickItem;

    public AdtImageOrder(Context context, int width) {

        this.context = context;

        this.width = width;

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        items = new ArrayList<>();
        itemsBase64 = new ArrayList<>();
    }

    public void initSetData(String item, String strBase64) {
        items.add(item);
        itemsBase64.add(strBase64);
        notifyDataSetChanged();
    }

    public void initSetData(List<String> items, List<String> itemsBase64) {
        this.items = items;
        this.itemsBase64 = itemsBase64;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {

            convertView = mLayoutInflater.inflate(R.layout.tmp_image_item, parent, false);
            viewHolder = new ViewHolder(convertView);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String item = getItem(position);

        ImageLoadAsync loadAsync = new ImageLoadAsync(context, viewHolder.img_avatar, width);
        loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, item);

        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(position);
                itemsBase64.remove(position);
                notifyDataSetChanged();
                getOnClickItem().onClickItemDelete();
            }
        });

        return convertView;
    }

    public List<String> initGetListmage(){
        if(items == null){
            items = new ArrayList<>();
        }
        return items;
    }

    public List<String> initGetListmageBase64(){
        return itemsBase64;
    }

    class ViewHolder {

        private ImageView img_avatar;
        private ImageView img_delete;

        public ViewHolder(View v) {
            v.setTag(this);

            img_avatar = (ImageView) v.findViewById(R.id.img_avatar);
            img_delete = (ImageView) v.findViewById(R.id.img_delete);
        }
    }

    @Override
    public int getCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    @Override
    public String getItem(int position) {
        if (items == null) {
            return "";
        }
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public AdtImageOrder.onClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(AdtImageOrder.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public interface onClickItem{
        void onClickItemDelete();
    }
}
