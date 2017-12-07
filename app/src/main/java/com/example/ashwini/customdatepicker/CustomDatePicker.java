package com.example.ashwini.customdatepicker;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.Calendar;

public class CustomDatePicker extends AppCompatActivity implements DatePicker.OnDateChangedListener {

    private DatePicker _datePicker;
    private Typeface _fontAwesomeFont;
    private EditText _date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_date_picker);

        _fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesomeicon.ttf");
        _date = (EditText) findViewById(R.id.date);

        _date.setFocusableInTouchMode(false);
        _date.setFocusable(false);
        _date.setCursorVisible(false);

        initDatePicker();
        initSetDate();
        initClearDate();
    }

    private void initDatePicker(){
        _datePicker = (DatePicker) findViewById(R.id.myDatePicker);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        _datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);

        if (_datePicker.getChildCount() > 0) {
            LinearLayout mLinearLayoutOne = (LinearLayout) _datePicker.getChildAt(0);
            if (mLinearLayoutOne.getChildCount() > 0) {
                LinearLayout mLinearLayoutTwo = (LinearLayout) mLinearLayoutOne.getChildAt(0);
                if (mLinearLayoutTwo.getChildCount() > 0) {
                    final int childCount = mLinearLayoutTwo.getChildCount();
                    for (int j = 0; j < childCount; j++) {
                        NumberPicker mNumberPicker = (NumberPicker) mLinearLayoutTwo.getChildAt(j);

                        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
                        for (java.lang.reflect.Field pf : pickerFields) {
                            if (pf.getName().equals("mSelectionDivider")) {
                                pf.setAccessible(true);
                                try {
                                    ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0062c7"));
                                    pf.set(mNumberPicker, colorDrawable);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }

                        final int count = mNumberPicker.getChildCount();
                        for (int i = 0; i < count; i++) {
                            View child = mNumberPicker.getChildAt(i);
                            if (child instanceof EditText) {
                                try {
                                    Field selectorWheelPaintField = mNumberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                                    selectorWheelPaintField.setAccessible(true);
                                    ((Paint) selectorWheelPaintField.get(mNumberPicker)).setColor(Color.parseColor("#323e48"));
                                    ((EditText) child).setTextColor(Color.parseColor("#323e48"));
                                    mNumberPicker.invalidate();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }


    }

    @Override
    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

    }

    private void initSetDate(){
        Button mSetDate = (Button) findViewById(R.id.btn_set_date);
        mSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mNewDate = getDateString(_datePicker.getDayOfMonth(), _datePicker.getMonth() + 1, _datePicker.getYear());
                _date.setText(mNewDate);
            }
        });
    }

    private String getDateString(int pDay, int pMonth, int pYear) {
        String mDay = String.format("%02d", pDay);
        String mMonth = String.format("%02d", pMonth);
        String mYear = String.format("%04d", pYear);
        String mDateStr = mDay + "/" + mMonth + "/" + mYear;
        return mDateStr;
    }

    private void initClearDate(){
        TextView mDateClearAll = (TextView) findViewById(R.id.date_clear_all);
        mDateClearAll.setTypeface(_fontAwesomeFont);

        mDateClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _date.setText("");
            }
        });
    }
}
