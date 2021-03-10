package de.codingforce.wad.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import de.codeingforce.wad.R;
import de.codingforce.wad.fragment.NameAwareFragment;

public class Calendar extends NameAwareFragment {
    private static final String LOG_TAG = "Calender";

    private CalendarView calendarView;
    private String  curDate;
    private String  Year;
    private String  Month;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_calender, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onViewCreated--");

        calendarView = view.findViewById(R.id.calendarView2);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                curDate = String.valueOf(dayOfMonth);
                Year = String.valueOf(year);
                Month = String.valueOf(month);
                Log.e(LOG_TAG, "date : " +Year+"/"+Month+"/"+curDate);
            }
        });
    }
}
