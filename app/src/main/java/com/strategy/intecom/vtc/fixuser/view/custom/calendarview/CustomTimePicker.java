package com.strategy.intecom.vtc.fixuser.view.custom.calendarview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.strategy.intecom.vtc.fixuser.utils.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lugen on 9/7/16.
 * Kiểm soát toàn bộ logic của time picker
 *
 * Thiếu: cập nhật am -> pm nếu spinner chuyển từ 12:00 -> 12:01 và chiều sang sáng hôm sau nếu 00:00 -> 00:01
 */
public class CustomTimePicker extends TimePicker {
    private static final String TAG = CustomTimePicker.class.getSimpleName();
    private final static int TIME_PICKER_MINUTE_INTERVAL = 15;
    private static final int MINUTES_IN_HOUR = 60;
    private static final int HOUR_IN_HALF_DAY = 12;
    private static final int PM = 1;
    private static final int DEFAULT_MIN = 1;

    private boolean isAttacted = false;
    private int minHour = DEFAULT_MIN;
    NumberPicker mHourSpinner;
    NumberPicker mAmPmPicker;
    NumberPicker mMinuteSpinner;

    public CustomTimePicker(Context context) {
        super(context);
    }

    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomTimePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttacted = true;
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");

            Field hourField = classForid.getField("hour");
            mHourSpinner = (NumberPicker) findViewById(hourField.getInt(null));

            Field field = classForid.getField("minute");
            mMinuteSpinner = (NumberPicker) findViewById(field.getInt(null));

            Field amPmField = classForid.getField("amPm");
            mAmPmPicker = (NumberPicker) findViewById(amPmField.getInt(null));

            // Update minute spinner to 0-15-30-45
            // value set from 0 to 3
            mMinuteSpinner.setMinValue(0);
            mMinuteSpinner.setMaxValue((MINUTES_IN_HOUR / TIME_PICKER_MINUTE_INTERVAL) - 1);

            // init display value : 0-15-30-45
            List<String> displayedValues = new ArrayList<>();
            for (int i = 0; i < MINUTES_IN_HOUR; i += TIME_PICKER_MINUTE_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            mMinuteSpinner.setDisplayedValues(displayedValues
                    .toArray(new String[0]));

            updateMinHour();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException exc) {
            exc.printStackTrace();
        }
    }

    public void updateLimitedHour(int minHour) {
        this.minHour = minHour;
        if (isAttacted) {
            updateMinHour();
        }

    }

    private void updateMinHour() {
        if (is24HourView()) {
            mHourSpinner.setMinValue(minHour);
        } else {
            Logger.i(TAG, this, "updateMinHour minHour = " + minHour);

            // is after noon or not ?
            if (minHour > HOUR_IN_HALF_DAY) {
                // if current hour smaller than min hour
                if (mHourSpinner.getValue() < (minHour - HOUR_IN_HALF_DAY)) {
                    mHourSpinner.setValue(minHour - HOUR_IN_HALF_DAY);
                }
                // absolute PM
                if (mAmPmPicker.getValue() < PM) {
                    mAmPmPicker.setValue(PM);
                }

                // auto match to min value if user set to smaller
                mHourSpinner.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                        if (newVal < (minHour - HOUR_IN_HALF_DAY)) {
                            mHourSpinner.setValue(minHour - HOUR_IN_HALF_DAY);
                        }
                    }
                });

                // fix after noon value because of no before null
                mAmPmPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                        if (newVal < PM) {
                            mAmPmPicker.setValue(PM);
                        }
                    }
                });
            } else {
                if (mHourSpinner.getValue() < (minHour)) {
                    mHourSpinner.setValue(minHour);
                }

                // auto match to min value if user set to smaller
                mHourSpinner.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                        if (newVal < (minHour) && mAmPmPicker.getValue() < PM) {
                            mHourSpinner.setValue(minHour);
                        }
                    }
                });

                // update hour if change from pm to am
                mAmPmPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                        if (newVal < PM) {
                            if (mHourSpinner.getValue() < minHour) {
                                mHourSpinner.setValue(minHour);
                            }
                        }
                    }
                });
            }

        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttacted = false;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public int getMinute() {
        int minute = super.getMinute() * TIME_PICKER_MINUTE_INTERVAL;
        return minute;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Integer getCurrentMinute() {
        return super.getCurrentMinute() * TIME_PICKER_MINUTE_INTERVAL;
    }

    @Override
    public int getHour() {
        if (isAttacted) {
            return getHourValue();
        } else {
            return super.getHour();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public Integer getCurrentHour() {
        if (isAttacted) {
            return getHourValue();}
        else {
            return super.getCurrentHour();
        }
    }

    private int getHourValue() {
        if (mAmPmPicker.getValue() == PM) {
            if (mHourSpinner.getValue() == HOUR_IN_HALF_DAY && mMinuteSpinner.getValue() > 0) {
                return mHourSpinner.getValue();
            } else {
                return mHourSpinner.getValue() + HOUR_IN_HALF_DAY;
            }
        } else {
            if (mHourSpinner.getValue() == HOUR_IN_HALF_DAY && mMinuteSpinner.getValue() > 0) {
                return mHourSpinner.getValue() - HOUR_IN_HALF_DAY;
            } else {
                return mHourSpinner.getValue();
            }
        }
    }
}
