package com.strategy.intecom.vtc.fixuser.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.adt.adtnormal.AdtPlaceSlidesDate;
import com.strategy.intecom.vtc.fixuser.model.VtcModelNewOrder;
import com.strategy.intecom.vtc.fixuser.utils.Logger;
import com.strategy.intecom.vtc.fixuser.utils.Utils;
import com.strategy.intecom.vtc.fixuser.view.base.AppCore;
import com.strategy.intecom.vtc.fixuser.view.custom.calendarview.CustomTimePicker;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by PHAMLUAN on 5/31/2016.
 * Class này thực hiện việc hiển thị giao diện chọn thời gian cho người dùng để chọn lịch hẹn với thợ
 */
@SuppressLint("ValidFragment")
public class DLTime extends DialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener
{
    private static final String TAG = DLTime.class.getSimpleName();

    private static final int FIRST_ITEM = 0;

    private View viewRoot;

    private FragmentActivity context;
    private int width = 0;

    private AdtPlaceSlidesDate mAdapter;
    private ViewPager mPager;
    private PageIndicator mIndicator;

    private onClickItem onClickItem;

    private String sDate = "";
    private String sThu = "";
    private String sNgay = "";

    private String[] dayOfWeek = new String[7];
    private String[] dayOfDay = new String[7];
    private String[] dateStr = new String[7];

    @SuppressLint("ValidFragment")
    public DLTime(Context context, int width) {
        this.context = (FragmentActivity) context;
        this.width = (int) (width - (context.getResources().getDimension(R.dimen.confirm_ui_padding_w) * 2));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.dl_time, container, false);

        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams
                .FLAG_FULLSCREEN);
        getDialog().getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        setCancelable(false);

        initController(view);
    }

    CustomTimePicker timePicker;

    private void initController(View view) {

        Calendar currentDate = Calendar.getInstance();
        Locale.setDefault(new Locale("vi", "VN"));
        SimpleDateFormat fmDayOfWeek = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat fmDayOfDay = new SimpleDateFormat("d", Locale.getDefault());
        SimpleDateFormat date = new SimpleDateFormat(":MM:yyyy", Locale.getDefault());
        Date day = new Date();
        dayOfWeek[0] = fmDayOfWeek.format(day);
        dayOfDay[0] = fmDayOfDay.format(day);
        dateStr[0] = date.format(day);
        for (int i = 1; i < 7; i++) {
            currentDate.add(Calendar.DAY_OF_MONTH, 1);
            Date newDay = currentDate.getTime();
            dayOfWeek[i] = fmDayOfWeek.format(newDay);
            dayOfDay[i] = fmDayOfDay.format((currentDate.getTime()));
            dateStr[i] = date.format((currentDate.getTime()));
        }
        sDate = dateStr[0];
        sThu = dayOfWeek[0];
        sNgay = dayOfDay[0];

        mAdapter = new AdtPlaceSlidesDate(getChildFragmentManager(), dayOfWeek, dayOfDay);
        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        mPager = (ViewPager) view.findViewById(R.id.pagerDate);

        Button btnNow = (Button) view.findViewById(R.id.btn_Now);
        Button btnAccept = (Button) view.findViewById(R.id.btn_Accept);
        Button btnExit = (Button) view.findViewById(R.id.btn_Exit);

        btnNow.setOnClickListener(this);
        btnAccept.setOnClickListener(this);
        mIndicator.setOnPageChangeListener(this);
        btnExit.setOnClickListener(this);

        timePicker = (CustomTimePicker) view.findViewById(R.id.custom_time_picker);

        long currentTime = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(currentTime);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        timePicker.updateLimitedHour(hour);

        initData();
    }

    private void initData() {

        mPager.setAdapter(mAdapter);

        mIndicator.setViewPager(mPager);

        ((CirclePageIndicator) mIndicator).setSnap(true);
    }

    private void setLimitTimeForDay(boolean isCurrentDay) {
        if (isCurrentDay) {
            long currentTime = System.currentTimeMillis();
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(currentTime);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            timePicker.updateLimitedHour(hour);
        } else {
            timePicker.updateLimitedHour(1);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Now:
                String dateNow = Utils.initGetDateDefault(null);
                getOnClickItem().onClickItemType(VtcModelNewOrder.TYPE_BOOKING_FAST, context.getResources().getString
                        (R.string.btn_TranInfo_Now), dateNow);
                this.dismiss();
                break;
            case R.id.btn_Accept:
                int hour;
                int minute;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                String s = sThu + " " + sNgay + sDate + " " + hour + ":" + minute;
//                AppCore.showLog("----------- : " + s);
                AppCore.showLog("----------- : " + Utils.initConvertTimeDisplay(s));
//                AppCore.showLog("----------- : " + Utils.initConvertTime(s));

                getOnClickItem().onClickItemType(VtcModelNewOrder.TYPE_BOOKING_NORMAL, Utils.initConvertTimeDisplay
                        (s), Utils.initConvertTime(s));
                this.dismiss();
                break;
            case R.id.btn_Exit:
                this.dismiss();
                break;
            default:
                return;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        sDate = dateStr[position];
        sNgay = dayOfDay[position];
        Logger.i(TAG, DLTime.this, "setOnPageChangeListener onPageSelected position" + position);
        setLimitTimeForDay(position == FIRST_ITEM);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public DLTime.onClickItem getOnClickItem() {
        return onClickItem;
    }

    public void setOnClickItem(DLTime.onClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public interface onClickItem {
        void onClickItemType(String type, String strDisplay, String strSendSV);
    }
}
