package com.honglu.future.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.widget.wheelview.AbstractWheelTextAdapter;
import com.honglu.future.widget.wheelview.OnWheelChangedListener;
import com.honglu.future.widget.wheelview.WheelView;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by zhuaibing on 2017/11/2
 */

public class SingleDateDialog extends Dialog{
    private Context mContext;
    private TextView mCancel;
    private TextView mDetermine;
    private WheelView mYear;
    private WheelView mMonth;
    private WheelView mDay;

    private ArrayList<String>  mYearList = new ArrayList<>();
    private ArrayList<String>  mMonthList = new ArrayList<>();
    private ArrayList<String>  mDayList = new ArrayList<>();

    //是否初始化 当天数据
    private boolean isInitData = false;
    //当前月份下的天数
    private int mDayData;
    //当天的日期
    private int mCurrentDay = getDay();
    //选中的年份
    private String mSelectYear;
    //选中的月份
    private String mSelectMonth;
    //选中的天数
    private String mSelectDay;
    //字体选中大小
    private int mMaxTextSize;
    //字体未选中大小
    private int mMinTextSize;

    private String mType;

    private CalendarTextAdapter mYearAdapter;
    private CalendarTextAdapter mMonthAdapter;
    private CalendarTextAdapter mDayAdapter;

    public SingleDateDialog(@NonNull Context context) {
        super(context, R.style.DateDialog);
        this.mContext = context;
    }

    public void showDateDialog(String mType){
        this.mType = mType;
        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_single_date);
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        mCancel = (TextView) findViewById(R.id.tv_cancel);
        mDetermine = (TextView) findViewById(R.id.tv_determine);
        mYear = (WheelView) findViewById(R.id.wv_year);
        mMonth = (WheelView) findViewById(R.id.wv_month);
        mDay = (WheelView) findViewById(R.id.wv_day);

        this.mMaxTextSize = 18;
        this.mMinTextSize = 18;
        if (!isInitData){
          setDate(getYear(),getMonth(),getDay());
        }
        initYearData();
        initMonthData();
        initDayData(mDayData);

        int yearIndex = getYearIndex(getYear());
        mYearAdapter = new CalendarTextAdapter(mContext,mYearList,yearIndex,mMaxTextSize,mMinTextSize);
        mYear.setVisibleItems(3);
        mYear.setViewAdapter(mYearAdapter);
        mYear.setCurrentItem(yearIndex);

        int monthIndex = getMonthIndex(getMonth());
        mMonthAdapter = new CalendarTextAdapter(mContext,mMonthList,monthIndex,mMaxTextSize,mMinTextSize);
        mMonth.setVisibleItems(3);
        mMonth.setViewAdapter(mMonthAdapter);
        mMonth.setCurrentItem(monthIndex);

        int dayIndex = getDayIndex(getDay());
        mDayAdapter = new CalendarTextAdapter(mContext,mDayList,dayIndex,mMaxTextSize,mMinTextSize);
        mDay.setVisibleItems(3);
        mDay.setViewAdapter(mDayAdapter);
        mDay.setCurrentItem(dayIndex);

        mYear.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String mYearValue = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                mSelectYear = mYearValue;
                setTextviewSize(mSelectYear, mYearAdapter);

                int oldDayListSize = mDayList.size();
                calDays(Integer.parseInt(mSelectYear),Integer.parseInt(mSelectMonth));
                initDayData(mDayData);
                if (oldDayListSize  != mDayList.size()) {
                    int currentItem = mDayData - mCurrentDay >= 0 ? mCurrentDay - 1 : 15;
                    mDayAdapter = new CalendarTextAdapter(mContext, mDayList, currentItem, mMaxTextSize, mMinTextSize);
                    mDay.setVisibleItems(3);
                    mDay.setViewAdapter(mDayAdapter);
                    mDay.setCurrentItem(currentItem);
                }
            }
        });

        mMonth.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String mMonthValue = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
                mSelectMonth = mMonthValue;
                setTextviewSize(mSelectMonth, mMonthAdapter);
                int oldDayListSize = mDayList.size();
                calDays(Integer.parseInt(mSelectYear),Integer.parseInt(mSelectMonth));
                initDayData(mDayData);
                if (oldDayListSize  != mDayList.size()) {
                    int currentItem = mDayData - mCurrentDay >= 0 ? mCurrentDay - 1 : 15;
                    mDayAdapter = new CalendarTextAdapter(mContext, mDayList, currentItem, mMaxTextSize, mMinTextSize);
                    mDay.setVisibleItems(3);
                    mDay.setViewAdapter(mDayAdapter);
                    mDay.setCurrentItem(currentItem);
                }
            }
        });

        mDay.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String mDayValue = (String) mDayAdapter.getItemText(wheel.getCurrentItem());
                mSelectDay = mDayValue;
                setTextviewSize(mSelectDay, mDayAdapter);
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBirthdayListener !=null){
                    onBirthdayListener.onBirthday(mType,mSelectYear+"-"+mSelectMonth+"-"+mSelectDay);
                }
            }
        });
    }


    public void setOnBirthdayListener(OnBirthdayListener listener){
        this.onBirthdayListener = listener;
    }
    private OnBirthdayListener onBirthdayListener;
    public interface OnBirthdayListener{
        void onBirthday(String type,String time);
    }

    public void setDate(int year, int month, int day) {
        this.mSelectYear = year+"";
        this.mSelectMonth = month < 10 ? "0"+month : month+"";
        this.mSelectDay = day < 10 ? "0" +day : day +"";
        this.isInitData = true;
         calDays(year, month);
    }


    //初始化 年份数据
     private void initYearData(){
         for (int i = 2000; i <= getYear(); i++) {
             mYearList.add(i+"");
         }
     }

     //初始化 月份数据
     private void initMonthData(){
         for (int i = 1; i <= 12; i++) {
             if (i < 10){
                 mMonthList.add("0"+i);
             }else {
                mMonthList.add(i+"");
             }
         }
     }

     //初始化当前月份下的天数
     private void initDayData(int days){
         mDayList.clear();
         for (int i = 1; i <= days; i++) {
            if (i < 10){
                mDayList.add("0"+i);
            }else {
                mDayList.add(i+"");
            }
         }
     }

    //初始化天数
    public void calDays(int year, int month) {
        boolean leayyear = false;
        leayyear = year % 4 == 0 && year % 100 != 0;
        for (int i = 1; i <= 12; i++) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    this.mDayData = 31;
                    break;
                case 2:
                    if (leayyear) {
                        this.mDayData = 29;
                    } else {
                        this.mDayData = 28;
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    this.mDayData = 30;
                    break;
            }
        }
//        if (year == getYear() && month == getMonth()) {
//            this.day = getDay();
//        }
    }

    private int getYearIndex(int year){
        int yearIndex = 0;
        for (int i = 2000; i <= getYear(); i++) {
            if (i == year) {
                break;
            }
            yearIndex++;
        }
        return yearIndex;
    }

    private int getMonthIndex(int month){
        int monthIndex = 0;
        for (int j = 1; j <= getMonth(); j++) {
            if (j == month) {
                break;
            }
            monthIndex++;
        }
        return monthIndex;
    }

    private int getDayIndex(int day){
        int dayIndex = 0;
        for (int i = 1 ; i <= mDayData;i++){
            if (i == day){
                break;
            }
            dayIndex ++;
        }
        return dayIndex;
    }


    public int getYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public int getMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    public int getDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DATE);
    }


    public void setTextviewSize(String curriteItemText, SingleDateDialog.CalendarTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(mMaxTextSize);
            } else {
                textvew.setTextSize(mMinTextSize);
            }
        }
    }

    private class CalendarTextAdapter extends AbstractWheelTextAdapter {
       private ArrayList<String> list;

        protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_single_date_layout, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
            setItemLineResourceId(R.id.lineTop,R.id.lineBottom);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }
}
