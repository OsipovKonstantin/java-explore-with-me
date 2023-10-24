package ru.practicum.ewm.constants;

import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class Constants {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_PATTERN_MILIS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    public static final int MIN_PAGE_FROM = 0;
    public static final int MIN_PAGE_SIZE = 1;
}
