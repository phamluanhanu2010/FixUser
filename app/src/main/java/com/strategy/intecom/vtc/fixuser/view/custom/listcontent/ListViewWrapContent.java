package com.strategy.intecom.vtc.fixuser.view.custom.listcontent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Mr. Ha on 5/25/16.
 * LISTVIEW TỰ ĐỘNG GIÃN CHIỀU CAO THEO CHÍNH CÁC LISTITEM BÊN TRONG NÓ
 * override lại phần tính toán chiều cao của ListView khi render. Khi render, nó sẽ tự get Adapter và tính toán chiều cao các tmp_search_history bên trong + phần padding của chính ListView.
 */
public class ListViewWrapContent extends ListView {


    public ListViewWrapContent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        setHeightWrapContent();
    }

    public void setHeightWrapContent() {
        ListAdapter
                adapter = getAdapter();
        if (adapter == null) {
            return;
        }
        int totalHeight = getPaddingTop() + getPaddingBottom();
        ;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, this);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.height = totalHeight + (this.getDividerHeight() * (adapter.getCount() - 1));
        this.setLayoutParams(params);
    }
}