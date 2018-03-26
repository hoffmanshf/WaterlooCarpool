package com.hoffman.carpool.domain;

import java.util.GregorianCalendar;

public class CalendarEvent {

    private String title;
    private GregorianCalendar startDate;
    private GregorianCalendar endDate;
    private String summary;
    private String name;
    private String canonicalHandleUUID;

    public CalendarEvent(String title, GregorianCalendar startDate, GregorianCalendar endDate, String summary, String name, String canonicalHandleUUID) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.summary = summary;
        this.name = name;
        this.canonicalHandleUUID = canonicalHandleUUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GregorianCalendar getStartDate() {
        return startDate;
    }

    public void setStartDate(GregorianCalendar startDate) {
        this.startDate = startDate;
    }

    public GregorianCalendar getEndDate() {
        return endDate;
    }

    public void setEndDate(GregorianCalendar endDate) {
        this.endDate = endDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCanonicalHandleUUID() {
        return canonicalHandleUUID;
    }

    public void setCanonicalHandleUUID(String canonicalHandleUUID) {
        this.canonicalHandleUUID = canonicalHandleUUID;
    }
}
