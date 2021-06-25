package com.eidland.auxilium.voice.only.ui;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Helper {
    public static String getFormattedText(String coin) {
        DecimalFormat formatter;
        BigDecimal val = new BigDecimal(coin);
        formatter = new DecimalFormat("#,###,###");
        return formatter.format(val);
    }

}
