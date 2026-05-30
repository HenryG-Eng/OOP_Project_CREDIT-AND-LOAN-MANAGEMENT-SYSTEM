/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido
 * @date: 29-05-2026
 */

package com.fincredit.model;


import java.util.ArrayList;
import java.util.List;

import com.fincredit.interfaces.ILoanable;

public abstract class LoanProduct implements ILoanable {

    protected final String type;
    protected final String icon;

    protected LoanProduct(String type, String icon) {
        this.type = type;
        this.icon = icon;
    }

    @Override
    public double calculateMonthlyPayment(double principal,
                                          double annualRate, int terms) {
        double i = annualRate / 100.0 / 12.0;
        if (i == 0) return principal / terms;
        double factor = Math.pow(1 + i, terms);
        return principal * (i * factor) / (factor - 1);
    }

    @Override
    public List<AmortizationRow> generateAmortizationTable(double principal,
                                                            double annualRate,
                                                            int terms) {
        double i       = annualRate / 100.0 / 12.0;
        double payment = calculateMonthlyPayment(principal, annualRate, terms);
        List<AmortizationRow> table = new ArrayList<>();
        double balance = principal;

        for (int period = 1; period <= terms; period++) {
            double interest       = balance * i;
            double capitalPayment = payment - interest;
            balance              -= capitalPayment;
            double remaining      = (period == terms) ? 0.0 : Math.max(0, balance);
            table.add(new AmortizationRow(period, payment, interest,
                                          capitalPayment, remaining));
        }
        return table;
    }

    @Override public String getType() { return type; }
    @Override public String getIcon() { return icon; }

    @Override public abstract double getBaseRate();
    @Override public abstract int getMaxTerm();
    @Override public abstract String getDescription();
}