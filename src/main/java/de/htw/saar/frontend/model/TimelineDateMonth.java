package de.htw.saar.frontend.model;

public class TimelineDateMonth {
    private int monthNumber;
    private String monthName;

    public TimelineDateMonth(int monthNumber, String monthName) {
        this.monthNumber = monthNumber;
        this.monthName = monthName;

    }

    /**
     * Gibt die Nummer des Monats zurueck
     * @return monthNumber
     */
    public int getMonthNumber() {
        return monthNumber;
    }

    /**
     * Setzt die Nummer des Monats auf den uebergebenen int-wert
     * @param monthNumber
     */
    public void setMonthNumber(int monthNumber) {
        this.monthNumber = monthNumber;
    }

    /**
     * Gibt den Namen des Monats zurueck
     * @return monthName
     */
    public String getMonthName() {
        return monthName;
    }

    /**
     * Setzt den Namen des Monats auf den uebergebenen string
     * @param monthName
     */
    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }
}
