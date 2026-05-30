/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido
 * @date: 29-05-2026
 */

package com.fincredit.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Class representing a client in the financial system, extending the Person class and including financial attributes such as monthly income and expenses.
 * It provides methods to calculate net income, payment capacity, credit score, and manage associated loans.
 */
public class Client extends Person {

    private double monthlyIncome;
    private double monthlyExpenses;
    private final List<String> loanIds;

    public static final double PAYMENT_CAPACITY_RATIO = 0.30;//30% of income is considered the maximum payment capacity

    public Client(String id, String name, String document, String email,
                  double monthlyIncome, double monthlyExpenses) {
        super(id, name, document, email);
        this.monthlyIncome   = monthlyIncome;
        this.monthlyExpenses = monthlyExpenses;
        this.loanIds         = new ArrayList<>();
    }

    @Override
    public String getRole() { return "CLIENT"; }
    /**
     * Method to get a summary of the client's financial status, including role, income, net income, payment capacity, credit score, and number of associated loans.
     */
    @Override
    public String getSummary() {
        return String.format(
            "Role: %s | Income: $%.2f | Net: $%.2f | Capacity: $%.2f | Score: %s | Loans: %d",
            getRole(), monthlyIncome, getNetIncome(),
            getMaxPaymentCapacity(), getCreditScore(), loanIds.size()
        );
    }
    
    // Financial calculations and loan management methods
    public double getNetIncome()          { return monthlyIncome - monthlyExpenses; }
    public double getMaxPaymentCapacity() { return monthlyIncome * PAYMENT_CAPACITY_RATIO; }
    public boolean canAfford(double pay)  { return pay <= getMaxPaymentCapacity(); }
    /**
     * Method to calculate the client's credit score based on the ratio of net income to monthly income, with predefined thresholds for different score categories.
     * @return A string representing the client's credit score, ranging from "AAA" for the highest score to "C" for the lowest, or "N/A" if income is not positive.
     */
    public String getCreditScore() {
        if (monthlyIncome <= 0) return "N/A";
        double ratio = getNetIncome() / monthlyIncome;
        if (ratio >= 0.50) return "AAA";
        if (ratio >= 0.35) return "AA";
        if (ratio >= 0.20) return "A";
        if (ratio >= 0.05) return "BBB";
        return "C";
    }

    public void addLoanId(String loanId)  { loanIds.add(loanId); }//add a loan ID to the client's list of associated loans
    public List<String> getLoanIds()      { return Collections.unmodifiableList(loanIds); }//return an unmodifiable view of the client's loan IDs to prevent external modification
    //getters and setters for monthly income and expenses
    public double getMonthlyIncome()            { return monthlyIncome; }
    public double getMonthlyExpenses()          { return monthlyExpenses; }
    public void setMonthlyIncome(double income) { this.monthlyIncome = income; }
    public void setMonthlyExpenses(double exp)  { this.monthlyExpenses = exp; }
}