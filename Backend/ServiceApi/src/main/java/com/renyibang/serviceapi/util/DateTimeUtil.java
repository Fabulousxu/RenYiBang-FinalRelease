package com.renyibang.serviceapi.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    // 定义日期时间格式
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 定义一个足够早的时间
    private static final LocalDateTime DEFAULT_EARLY_TIME = LocalDateTime.of(1970, 1, 1, 0, 0, 0);

    /**
     * 将字符串转换为LocalDateTime，如果字符串为空或格式错误，则返回默认值
     *
     * @param timeString 时间字符串
     * @param defaultValue 默认值
     * @return 转换后的LocalDateTime对象
     */
    public static LocalDateTime convertToDateTime(String timeString, LocalDateTime defaultValue) {
        if (timeString.isEmpty()) {
            return defaultValue;
        }

        try {
            return LocalDateTime.parse(timeString, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            // 解析失败，返回默认值
            return null;
        }
    }

    /**
     * 获取转换后的开始时间，若timeBegin为空则返回默认的足够早的时间
     *
     * @param timeBegin 开始时间字符串
     * @return 转换后的LocalDateTime对象
     */
    public static LocalDateTime getBeginDateTime(String timeBegin) {
        return convertToDateTime(timeBegin, DEFAULT_EARLY_TIME);
    }

    /**
     * 获取转换后的结束时间，若timeEnd为空则返回当前时间的第二天
     *
     * @param timeEnd 结束时间字符串
     * @return 转换后的LocalDateTime对象
     */
    public static LocalDateTime getEndDateTime(String timeEnd) {
        return convertToDateTime(timeEnd, LocalDateTime.now().plusDays(1));
    }

    /**
     * 将LocalDateTime格式化为字符串，使用自定义的日期时间格式
     *
     * @param dateTime LocalDateTime对象
     * @return 格式化后的字符串
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }
}
