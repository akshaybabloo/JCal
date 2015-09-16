package com.gollahalli.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by akshayrajgollahalli on 15/09/15.
 */
public class Calculate {

    public static final Logger logger = LoggerFactory.getLogger(Calculate.class);

    public double percentage(double rate) {
        return rate / 100;
    }

    public double[] simple_interest(double principal, double rate, double duration) {
        logger.info("simple interest called with principal = " + principal + ", rate = " + rate + " and duration = " + duration);
        double[] total = new double[2];
        if (rate == 0) {
            logger.error("Rate cannot be 0");
        }
        double percent_rate = percentage(rate);
        total[0] = principal * percent_rate * duration;
        total[1] = total[0] - principal;
        return total;
    }

    public double[][] compound_interest(double principal, double rate, double duration, double compounded) {
        logger.info("compound interest called with principal = " + principal + ", rate = " + rate + ", duration = " + duration +
                " and compounded for " + compounded);
        int durationInt = (int) duration;
        double[][] total = new double[2][durationInt + 1];
        double percent_rate = percentage(rate);
        BigDecimal bd;
        BigDecimal bd1;
        for (int i = 0; i < duration + 1; i++) {
            total[0][i] = principal * Math.pow(1 + (percent_rate / compounded), compounded * i);
            bd = new BigDecimal(total[0][i]).setScale(2, RoundingMode.HALF_DOWN);
            total[0][i] = bd.doubleValue();
            total[1][i] = total[0][i] - principal;
            bd1 = new BigDecimal(total[1][i]).setScale(2, RoundingMode.HALF_DOWN);
            total[1][i] = bd1.doubleValue();
        }
        return total;
    }
}
