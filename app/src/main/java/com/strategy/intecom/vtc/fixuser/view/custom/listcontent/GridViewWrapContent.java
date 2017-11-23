package com.strategy.intecom.vtc.fixuser.view.custom.listcontent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * Created by Mr. Ha on 5/25/16.
 *
 * GRIDVIEW TỰ ĐỘNG GIÃN CHIỀU CAO THEO CHÍNH CÁC LISTITEM BÊN TRONG NÓ
 * override lại phần tính toán chiều cao của ListView khi render. Khi render, nó sẽ tự get Adapter và tính toán chiều cao các tmp_search_history bên trong + phần padding của chính ListView.
 *
 */
public class GridViewWrapContent extends GridView {


    public GridViewWrapContent(Context context, AttributeSet attrs) {
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
        int totalHeight = getGridViewHeightBasedOnChildren(this, getNumColumns());

        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.height = totalHeight;
        this.setLayoutParams(params);
    }

    private int getGridViewHeightBasedOnChildren(GridView gridView, int numColumn) {
        int totalHeight = gridView.getPaddingTop() + gridView.getPaddingBottom();

        ListAdapter adapter = gridView.getAdapter();
        if (adapter != null) {
            int numRow = (int) Math.ceil(adapter.getCount() * 1.0 / numColumn);

            for (int i = 0; i < numRow; i++) {
                View listItem = adapter.getView(i, null, gridView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();

                if (i != 0) {
                    totalHeight += gridView.getVerticalSpacing();
                }
            }
        }
        return totalHeight;
    }
}