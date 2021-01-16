package net.myapp.Organize4U.note;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;


import net.myapp.Organize4U.R;





public class Calendar extends Fragment {


    public Calendar() {
        // Required empty public constructor
    }
    CalendarView calendarView;
    TextView save;
    Long date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = (CalendarView) view.findViewById(R.id.simpleCalendarView);
        save = (TextView) view.findViewById(R.id.save);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

               String date = dayOfMonth + "." + (month + 1) + "." +year;
                if(dayOfMonth<10){
                    date =  "0"+ dayOfMonth + "." + month + "." +year;
                }
                if(month+1<10){
                    date =   dayOfMonth + "." + "0" + (month +1) + "." + year;
                }
                if(month+1<10 && dayOfMonth<10){
                    date =   "0" + dayOfMonth + "." + "0" + (month +1) + "." + year;
                }
                   save.setText(date);

                    //cal.setBackgroundColor(Color.RED);

            }
        });
        return view;
    }


}
