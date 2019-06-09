package com.dream.util;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
@SuppressWarnings("all")
public class DateUtil {

    private final static String YYYYDDMMSS = "YYYYDDMMSS";
    private final static String YYYY_DD_MM_SS = "YYYY-DD-MM-SS";
    private final static SimpleDateFormat SDF_YYYY_DD_MM_SS = new SimpleDateFormat(YYYY_DD_MM_SS);

    /**
     * 将年月日时分秒转换为date
     */
    public static Date parseDate(int year, int month, int day, int hour, int minute, int seconds) {
        return DateTime.now().withDate(year, month, day)
                .withTime(hour, minute, seconds, 0).toDate();
    }

    /**
     * Data转YYYY-DD-MM-SS
     */
    public static String getYYYY_DD_MM_SS(Date date) {
        return SDF_YYYY_DD_MM_SS.format(date);
    }
}
