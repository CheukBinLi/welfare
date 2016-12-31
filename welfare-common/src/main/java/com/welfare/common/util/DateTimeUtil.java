package com.welfare.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static final String DATE_NUMBER_FORMAT = "yyyyMMdd";
    public static final String DATE_TIME_NUMBER_FORMAT = "yyyyMMddHHmmss";
    public static final String DATA_FORMAT = "yyyy-MM-dd";
    public static final String DATA_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 获得日期相加月份之后的第一天开始时间
     */
    public static Timestamp getMonthRangeStart(Object date, int monthsLater) {
        DateTime dateTime = new DateTime(date);
        DateTime plusMonthsDateTime = dateTime.plusMonths(monthsLater).withTime(0, 0, 0, 0);

        return new Timestamp(plusMonthsDateTime.dayOfMonth().withMinimumValue().getMillis());
    }

    /**
     * 获得日期相加月份之后的最后一天结束时间
     */
    public static Timestamp getMonthRangeEnd(Object date, int monthsLater) {
        DateTime dateTime = new DateTime(date);
        DateTime plusMonthsDateTime = dateTime.plusMonths(monthsLater).withTime(23, 59, 59, 999);

        return new Timestamp(plusMonthsDateTime.dayOfMonth().withMaximumValue().getMillis());
    }

    public static String formatDate2String(Object date, String formatString) {
        DateTime dateTime = new DateTime(date);
        DateTimeFormatter fmt = DateTimeFormat.forPattern(formatString);

        return fmt.print(dateTime);
    }

    /**
     * 获取两个日期区间每一天的日期
     *
     * @return List
     */
    public static List<String> getEveryDayDateFromStartDateAndEndDate(Object startDate, Object endDate, String format) throws ParseException {
        List<String> dateTimeList = new ArrayList<String>();
        DateTimeFormatter fmt = DateTimeFormat.forPattern(format);

        DateTime start = new DateTime(startDate);
        DateTime end = new DateTime(endDate);
        int daysInterval = Days.daysBetween(start, end).getDays();

        for (int i = 0; i <= daysInterval; i++) {
            DateTime nextDateTime = start.plusDays(i);
            dateTimeList.add(fmt.print(nextDateTime));
        }

        return dateTimeList;
    }

    /**
     * 获取两个日期区间相差的月份数
     */
    public static int getMonthInterval(Object startDateTime, Object endDateTime) {
        DateTime start = new DateTime(startDateTime);
        DateTime end = new DateTime(endDateTime);

        return Months.monthsBetween(start, end).getMonths();
    }

    /**
     * 获取两个日期区间相差的分钟数
     */
    public static int getMinutesInterval(Object startDateTime, Object endDateTime) {
        DateTime start = new DateTime(startDateTime);
        DateTime end = new DateTime(endDateTime);

        return Minutes.minutesBetween(start, end).getMinutes();
    }

    /**
     * 获取两个日期区间相差的小时数
     */
    public static int getHoursInterval(Object startDateTime, Object endDateTime) {
        DateTime start = new DateTime(startDateTime);
        DateTime end = new DateTime(endDateTime);

        return Hours.hoursBetween(start, end).getHours();
    }

    /**
     * 时间计算（传入的时间加上分钟数，返回相加之后的时间）
     */
    public static Timestamp addMinuteToTimestamp(Object dateTime, int minutes) {
        DateTime dateTimeObj = new DateTime(dateTime);
        DateTime dateTimePlusObj = dateTimeObj.plusMinutes(minutes);

        Timestamp timestamp = new Timestamp(dateTimePlusObj.getMillis());

        return timestamp;
    }

    /**
     * 开始时间是否在结束时间之前
     */
    public static boolean isBefore(Object startDateTime, Object endDateTime) {
        DateTime start = new DateTime(startDateTime);
        DateTime end = new DateTime(endDateTime);

        return start.isBefore(end.getMillis());
    }

    /**
     * 获取日期当天结束的时间
     * */
    public static DateTime getDateTimeByStart(Object date) {
        DateTime dateTime = new DateTime(date);
        DateTime newDateTime = dateTime.withTime(0, 0, 0, 0);

        return newDateTime;
    }

    /**
     * 获取日期当天结束的时间
     * */
    public static DateTime getDateTimeByEnd(Object date) {
        DateTime dateTime = new DateTime(date);
        DateTime newDateTime = dateTime.withTime(23, 59, 59, 999);

        return newDateTime;
    }
}
