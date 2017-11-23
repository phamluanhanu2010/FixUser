package com.strategy.intecom.vtc.fixuser.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.strategy.intecom.vtc.fixuser.R;

/**
 * Created by Mr. Ha on 6/1/16.
 */
@SuppressLint("ValidFragment")
public final class FMPlaceSlideDate extends Fragment {
    String dayOfMonth;
    String dayOfWeek;

    public FMPlaceSlideDate(String i, String date) {
        dayOfMonth = i;
        dayOfWeek = date;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.tmp_sliding_date_item, container, false);

        TextView tvDayOfMonth = (TextView) viewRoot.findViewById(R.id.tv_DateofMonth);
        tvDayOfMonth.setText(dayOfMonth);

        TextView tvDayOfWeek = (TextView) viewRoot.findViewById(R.id.tv_DayofWeek);
        tvDayOfWeek.setText(dayOfWeek);

        return viewRoot;
    }

}
