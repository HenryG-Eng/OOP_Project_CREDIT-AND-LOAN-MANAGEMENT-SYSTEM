/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido
 * @date: 29-05-2026
 */

package com.fincredit.model;
/**
 * Class representing a free investment loan product, extending the LoanProduct class and providing specific details such as base interest rate, maximum term, and description for this type of loan.
 */
public class FreeInvestmentLoan extends LoanProduct {
    public FreeInvestmentLoan() { super("FREE_INVESTMENT", "💼"); }

    @Override public double getBaseRate()    { return 14.0; }
    @Override public int getMaxTerm()        { return 48; }
    @Override public String getDescription() { return "Flexible-purpose personal lending"; }
}