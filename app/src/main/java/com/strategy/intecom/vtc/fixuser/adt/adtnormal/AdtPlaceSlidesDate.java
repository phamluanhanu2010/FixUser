package com.strategy.intecom.vtc.fixuser.adt.adtnormal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMPlaceSlideDate;
import com.strategy.intecom.vtc.fixuser.view.fragment.FMPlaceSlideImage;
import com.viewpagerindicator.IconPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mr. Ha on 6/1/16.
 */
public class AdtPlaceSlidesDate extends FragmentPagerAdapter implements
        IconPagerAdapter {

    private String[] dayOfWeek = new String[7];
    private String[] dayOfMonth = new String[7];

    public AdtPlaceSlidesDate(FragmentManager fm, String[] dayOfWeek, String[] dayOfMonth) {
        super(fm);
        this.dayOfWeek = dayOfWeek;
        this.dayOfMonth = dayOfMonth;
    }

    @Override
    public Fragment getItem(int position) {
        return new FMPlaceSlideDate(dayOfMonth[position], dayOfWeek[position]);
    }

    @Override
    public int getCount() {
        return dayOfWeek.length;
    }

    @Override
    public int getIconResId(int index) {
        return index;
    }

}