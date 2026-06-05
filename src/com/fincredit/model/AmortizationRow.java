/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido
 * @date: 29-05-2026
 */

package com.fincredit.model;
/**
 * The AmortizationRow class represents a single row in an amortization table, containing details about the payment for a specific period, including the total payment, interest portion, capital payment, and remaining balance.
 */
public class AmortizationRow {

    private final int period;
    private final double payment;
    private final double interest;
    private final double capitalPayment;
    private final double remainingBalance;

    public AmortizationRow(int period, double payment, double interest,
                           double capitalPayment, double remainingBalance) {
        this.period           = period;
        this.payment          = payment;
        this.interest         = interest;
        this.capitalPayment   = capitalPayment;
        this.remainingBalance = remainingBalance;
    }

    public int    getPeriod()           { return period; }
    public double getPayment()          { return payment; }
    public double getInterest()         { return interest; }
    public double getCapitalPayment()   { return capitalPayment; }
    public double getRemainingBalance() { return remainingBalance; }
}