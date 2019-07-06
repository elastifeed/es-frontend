package de.htw.saar.frontend.service;

import de.htw.saar.frontend.model.TimelineDateMonth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class TimelineDateService {

    ArrayList<Integer> yearList = new ArrayList<>();
    ArrayList<TimelineDateMonth> monthList = new ArrayList<>();

    /**
     * Schreibt alle Jahre ab dem aktuellen Jahr bis zu einem festgelegten
     * zurückliegenden Jahr in eine Liste und gibt diese zurueck
     *
     * @return yearList
     */
    public ArrayList<Integer> getYearList()
    {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int yearLimit = 2016;
        yearList.clear();

        while (currentYear != yearLimit) {
            yearList.add(currentYear);
            currentYear--;
        }
        return yearList;
    }

    /**
     * Schreibt die Namen und Nummern aller Monate in eine Liste und
     * gibt diese zurueck
     *
     * @return monthList
     */
    public ArrayList<TimelineDateMonth> getMonthList() {

        if (monthList == null || monthList.isEmpty()) {
            Calendar c = Calendar.getInstance();
            Map<String, Integer> originalMap = c.getDisplayNames(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            Map<Integer, String> monthsMap = originalMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

            for (int i = 0; i < 12; i++) {
                TimelineDateMonth monthData = new TimelineDateMonth(i + 1, monthsMap.get(i));
                this.monthList.add(monthData);
            }
        }
        return this.monthList;
    }
}
