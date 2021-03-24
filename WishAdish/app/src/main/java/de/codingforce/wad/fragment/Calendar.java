package de.codingforce.wad.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.Date;

import de.codeingforce.wad.R;
import de.codingforce.wad.activity.MainActivity;

public class Calendar extends NameAwareFragment {
    private static final String LOG_TAG = "Calender";

    private CalendarView calendarView;
    private int  curDate;
    private int  Year;
    private int  Month;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_calender, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");
        MainActivity.main.change_title("Kalender");

        calendarView = view.findViewById(R.id.calendarView2);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                curDate = dayOfMonth;
                Year = year;
                Month = month;
                Log.e(LOG_TAG, "date : " +Year+"/"+Month+"/"+curDate);
                MainActivity.tag = new Date(year,month,curDate);
                //Show wishes for selected Date
                Class Dishes_from_date = WishesFromDate.class;
                MainActivity.main.placeFragment(Dishes_from_date, R.id.mainFrame);
            }
        });
    }
}
