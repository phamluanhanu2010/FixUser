package com.strategy.intecom.vtc.fixuser.adt.adtnormal;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;


import com.strategy.intecom.vtc.fixuser.R;

import java.util.List;

/**
 * Created by Mr. Ha on 5/27/16.
 */
public class AdtSearchHistory extends CursorAdapter {

    private List<String> items;

    private ViewHolder viewHolder;

    private LayoutInflater mLayoutInflater;

    public AdtSearchHistory(Context context, Cursor cursor, List<String> items) {
        super(context, cursor, false);

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.items = items;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        if(viewHolder !=null && viewHolder.text !=null && items != null && cursor != null) {
            viewHolder.text.setText(items.get(cursor.getPosition()));
        }

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = mLayoutInflater.inflate(R.layout.tmp_search_history, parent, false);

        viewHolder = new ViewHolder(view);

        return view;

    }

    class ViewHolder{

        private TextView text;

        public ViewHolder(View v) {
            v.setTag(this);

            text = (TextView) v.findViewById(R.id.tv_search_history);
        }
    }

}
