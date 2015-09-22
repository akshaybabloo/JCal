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

    public double[] simpleInterest(double principal, double rate, double duration) {
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

    public double[][] compoundInterest(double principal, double rate, double duration, double compounded) {
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

    public double fixedRateMortgageMonthly(double loanAmount, double termInMonths, double interestRate){

        interestRate /= 100.0;

        double monthlyRate = interestRate / 12.0;

        double monthlyPayment =
                (loanAmount*monthlyRate) /
                        (1-Math.pow(1+monthlyRate, -termInMonths));

        return monthlyPayment;
    }

    public double fixedRateMortgageYearly(double loanAmount, int termInYears, double interestRate){

        interestRate /= 100.0;

        double monthlyRate = interestRate;

        int termInMonths = termInYears;


        double monthlyPayment =
                (loanAmount*monthlyRate) /
                        (1-Math.pow(1+monthlyRate, -termInMonths));

        return monthlyPayment;
    }

    public double[][] fixedRateMortgageMonthlyChart(double principal, double interest, double term){

        double[][] total = new double[5][30];
        Calculate calculate = new Calculate();
        BigDecimal bd;
        BigDecimal bd1;
        BigDecimal bd2;
        BigDecimal bd3;
        BigDecimal bd4;
        for (int i = 0; i < 1; i++) {
            double interest1 = interest/100.0;
            bd = new BigDecimal(interest1 / 12.0).setScale(2, RoundingMode.HALF_DOWN);
            total[4][i] = bd.doubleValue();

            bd1 = new BigDecimal(interest/100).setScale(2, RoundingMode.HALF_DOWN);
            total[0][i] = bd1.doubleValue();

            bd2 = new BigDecimal((total[0][i]/12)*principal).setScale(2, RoundingMode.HALF_DOWN);
            total[1][i]= bd2.doubleValue();

            bd3 = new BigDecimal(calculate.fixedRateMortgageMonthly(principal, term, interest) - total[1][i]).setScale(2, RoundingMode.HALF_DOWN);
            total[2][i] = bd3.doubleValue();

            bd4 = new BigDecimal(principal - total[2][i]).setScale(2, RoundingMode.HALF_DOWN);
            total[3][i] = bd4.doubleValue();
        }

        return total;

    }
}
