package de.htw.saar.frontend.service;

import de.htw.saar.frontend.model.TimelineDateMonth;

import java.util.ArrayList;
import java.util.Calendar;

public class TimelineDateService {

    ArrayList<Integer> yearList = new ArrayList<>();
    ArrayList<TimelineDateMonth> monthList = new ArrayList<>();

    public ArrayList<Integer> getYearList(){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int yearLimit = 2016;
        yearList.clear();

        while(currentYear != yearLimit){
            yearList.add(currentYear);
            currentYear--;
        }
        return yearList;
    }

    public ArrayList<TimelineDateMonth> getMonthList(){
        TimelineDateMonth m1 =new TimelineDateMonth(1,"Januar");
        TimelineDateMonth m2 =new TimelineDateMonth(2,"Februar");
        TimelineDateMonth m3 =new TimelineDateMonth(3,"März");
        TimelineDateMonth m4 =new TimelineDateMonth(4,"April");
        TimelineDateMonth m5 =new TimelineDateMonth(5,"Mai");
        TimelineDateMonth m6 =new TimelineDateMonth(6,"Juni");
        TimelineDateMonth m7 =new TimelineDateMonth(7,"Juli");
        TimelineDateMonth m8 =new TimelineDateMonth(8,"August");
        TimelineDateMonth m9 =new TimelineDateMonth(9,"September");
        TimelineDateMonth m10 =new TimelineDateMonth(10,"Oktober");
        TimelineDateMonth m11 =new TimelineDateMonth(11,"November");
        TimelineDateMonth m12 =new TimelineDateMonth(12,"Dezember");

        monthList.clear();

        monthList.add(m1);
        monthList.add(m2);
        monthList.add(m3);
        monthList.add(m4);
        monthList.add(m5);
        monthList.add(m6);
        monthList.add(m7);
        monthList.add(m8);
        monthList.add(m9);
        monthList.add(m10);
        monthList.add(m11);
        monthList.add(m12);

        return monthList;
    }


}
