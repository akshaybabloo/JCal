package com.gollahalli.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by akshayrajgollahalli on 2/10/15.
 */
public class General {
    public String getDate(){
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("MMMMM dd, yyyy");
        return format.format(date);
    }
}
