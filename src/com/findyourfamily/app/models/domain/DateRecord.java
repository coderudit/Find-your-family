package com.findyourfamily.app.models.domain;

/**
 * Type for storing date related information.
 */
public class DateRecord {

    //Stores the year for the date.
    private int year;

    //Stores the month for the date.
    private int month;

    //Stores the day for the date.
    private int day;

    public DateRecord() {
        this.day = 1;
        this.month = 1;
        this.year = 1;
    }

    public DateRecord(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * Gets the year of the date.
     *
     * @return year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the year of the date.
     *
     * @param year of the date
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets the month of the date
     *
     * @return month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Sets the month of the date.
     *
     * @param month of the date
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Gets the day of the month.
     *
     * @return day
     */
    public int getDay() {
        return day;
    }

    /**
     * Sets the day of the month.
     *
     * @param day of the date
     */
    public void setDay(int day) {
        this.day = day;
    }
}
