package de.htw.saar.frontend.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@ManagedBean(name = "dateRange")
@SessionScoped
public class DateRangeBean {
    private Date startDate;
    private Date endDate;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public DateRangeBean() {
        Calendar c1 = Calendar.getInstance();
        endDate = c1.getTime();
        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.YEAR, c1.get(Calendar.YEAR));
        c2.set(Calendar.DAY_OF_YEAR, 1);
        startDate = c2.getTime();
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getDateRangeString() {
        return String.format(" Von:  %s   Bis: %s%n",
                formatter.format(startDate), formatter.format(endDate));
    }

}
