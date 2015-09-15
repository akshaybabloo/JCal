package com.gollahalli.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by akshayrajgollahalli on 15/09/15.
 */
public class Calculate {

    public static final Logger logger = LoggerFactory.getLogger(Calculate.class);

    public double percentage(double rate){
        return rate/100;
    }

    public double[] simple_interest(double principal, double rate, double duration){
        logger.info("simple interest called with principal = " + principal +", rate = " + rate + " and duration = " + duration);
        double[] total = new double[2];
        if (rate == 0){
            logger.error("Rate cannot be 0");
        }
        double percent_rate = percentage(rate);
        total[0] = principal * percent_rate * duration;
        total[1] = total[0] - principal;
        return total;
    }

    public double[] compound_interest(double principal, double rate, double duration, double compounded){
        logger.info("compound interest called with principal = " + principal +", rate = " + rate + ", duration = " + duration +
        " and compounded for " + compounded);
        double[] total = new double[2];
        double percent_rate = percentage(rate);
        total[0] =  principal * Math.pow(1 + (percent_rate/compounded), compounded * duration);
        total[1] = total[0] - principal;
        return total;
    }
}
