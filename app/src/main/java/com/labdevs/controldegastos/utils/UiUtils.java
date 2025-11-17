package com.labdevs.controldegastos.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class UiUtils {


    public static class Formats {
        public static final String amountFormat = "#,###";
        
        public static String getAmountFormatedStr(double amount){
            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
            decimalFormatSymbols.setGroupingSeparator('.');
            return "$ " + new DecimalFormat(amountFormat, decimalFormatSymbols).format(amount);
        }

    }

}
