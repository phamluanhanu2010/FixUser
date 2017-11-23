package com.strategy.intecom.vtc.fixuser.adt.adtnormal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Ha on 5/27/16.
 */
public class AdtSearchHistory_1 extends ArrayAdapter<String> {

    private List<String> items;

    private LayoutInflater mLayoutInflater;

    public AdtSearchHistory_1(Context context, List<String> items) {
        super(context, 0, items);

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {

            convertView = mLayoutInflater.inflate(R.layout.tmp_search_history, parent, false);
            viewHolder = new ViewHolder(convertView);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (viewHolder != null && viewHolder.text != null && items != null) {
            viewHolder.text.setText(items.get(position));
        }
        return convertView;
    }


    class ViewHolder {

        private TextView text;

        public ViewHolder(View v) {
            v.setTag(this);

            text = (TextView) v.findViewById(R.id.tv_search_history);
        }
    }

    @Override
    public int getCount() {
        if(items == null){
            return 0;
        }
        return items.size();
    }

    @Override
    public String getItem(int position) {
        if(items == null){
            return "";
        }
        return items.get(position);
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = (String) resultValue;
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                FilterResults filterResults = new FilterResults();
                filterResults.values = items;
                filterResults.count = items.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<String> filteredList = (ArrayList<String>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (String c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
