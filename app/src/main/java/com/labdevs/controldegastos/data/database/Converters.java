package com.labdevs.controldegastos.data.database;

import androidx.room.TypeConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Converters {

    private static final ThreadLocal<SimpleDateFormat> ISO = ThreadLocal.withInitial(() -> {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf;
    });

    @TypeConverter
    public static String fromDate(Date date) {
        return date == null ? null : ISO.get().format(date);
    }

    @TypeConverter
    public static Date toDate(String value) {
        if (value == null) return null;
        try {
            return ISO.get().parse(value);
        } catch (ParseException e) {
            e.printStackTrace(); // o loguear
            return null;
        }
    }
}