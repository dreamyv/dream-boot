package com.dream.util;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 */
@SuppressWarnings("all")
public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private final static String YYYYDDMMSS = "YYYYDDMMSS";
    private final static String YYYY_DD_MM_SS = "YYYY-DD-MM-SS";
    public static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat SDF_YYYY_DD_MM_SS = new SimpleDateFormat(YYYY_DD_MM_SS);

    private static final DateFormat[] dateFormat = {
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
            new SimpleDateFormat("yyyy-MM-dd HH:mm"),
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK),
            new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"),
            new SimpleDateFormat("yyyyMMddHHmmss"),
            new SimpleDateFormat("yyyyMMddHHmm"),
            new SimpleDateFormat("yyyyMMddHH"),
            new SimpleDateFormat("yyyyMMdd")
    };

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

    public static String getDateFomate(Date date) {
        return yyyyMMddHHmmss.format(date);
    }

    /**
     * String转Date
     */
    public static Date parse(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        Date parse = null;
        for (int i = 0; i < dateFormat.length; i++) {
            try {
                parse = dateFormat[i].parse(dateStr);
            } catch (ParseException ex) {
                continue;
            }
            break;
        }
        if (parse == null) {
            logger.error("时间转换Date出错!str:[{}]",dateStr);
        }
        return parse;
    }

}
