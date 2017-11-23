package com.strategy.intecom.vtc.fixuser.view.custom.calendarview;

import java.util.Date;

/**
 * Created by Mr. Ha on 19/05/16.
 */
public interface CalendarListener {
    void onDateSelected(Date date);

    void onMonthChanged(Date time);
}
