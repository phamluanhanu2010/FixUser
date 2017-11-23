package com.strategy.intecom.vtc.fixuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.strategy.intecom.vtc.fixuser.R;
import com.strategy.intecom.vtc.fixuser.view.custom.calendarview.CalendarListener;
import com.strategy.intecom.vtc.fixuser.view.custom.calendarview.CustomCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mr. Ha on 5/20/16.
 */
public class DlCalendar extends Dialog {

    private CustomCalendarView calendarView;
    private Context context;

    public DlCalendar(Context context) {
        super(context, R.style.DialogTheme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
//        setCancelable(false);
        setContentView(R.layout.dl_calendar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setCancelable(false);

        initControler();
    }

    public void initControler() {
        //Initialize CustomCalendarView from layout
        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);

        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        //Setting custom font
//        final Typeface typeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/Arch_Rival_Bold.ttf");
//        if (null != typeface) {
//            calendarView.setCustomTypeface(typeface);
//            calendarView.refreshCalendar(currentCalendar);
//        }

        //Show Monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);

        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Toast.makeText(context, df.format(date), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                Toast.makeText(context, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
