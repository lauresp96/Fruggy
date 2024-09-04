package com.eoi.Fruggy.Util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

//  Utilidad para la conversión entre LocalDate y Date.
public class DateConversion {

    public static Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        Date date = convertToDate(localDate);
    }
}
