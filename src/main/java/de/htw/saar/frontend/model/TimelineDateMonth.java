package de.htw.saar.frontend.model;

public class TimelineDateMonth {
    private int monthNumber;
    private String monthName;

    public TimelineDateMonth(int monthNumber, String monthName) {
        this.monthNumber = monthNumber;
        this.monthName = monthName;

    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(int monthNumber) {
        this.monthNumber = monthNumber;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }
}
