package com.dream.util;

import org.joda.time.DateTime;

import java.util.Date;

@SuppressWarnings("all")
public class DateUtil {

    /**
     * 将年月日时分秒转换为date
     */
    public static Date parseDate(int year, int month, int day, int hour, int minute, int seconds) {
        return DateTime.now().withDate(year, month, day)
                .withTime(hour, minute, seconds, 0).toDate();
    }
}
