/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido
 * @date: 29-05-2026
 */


package com.fincredit.model;

/**
 * Class representing a mortgage loan product, extending the abstract LoanProduct class and providing specific implementations for base rate, maximum term, and description.
 * This class defines the characteristics of a mortgage loan, including its type, icon, base interest rate, maximum term in months, and a brief description of the product.
 */
public class MortgageLoan extends LoanProduct {
	
    public MortgageLoan() { super("MORTGAGE", "🏠"); }
    /**
	 * Method to get the base interest rate for the mortgage loan product, returning a fixed value of 7.5%.
	 * @return The base interest rate for the mortgage loan.
	 */
    @Override public double getBaseRate()    { return 7.5; }
    /**
     * Method to get the maximum term for the mortgage loan product, returning a fixed value of 360 months (30 years).
     */
    @Override public int getMaxTerm()        { return 360; }
    /**
	 * Method to get a description of the mortgage loan product, returning a brief string that describes the purpose of the loan.
	 * @return A string describing the mortgage loan product.
	 */
    @Override public String getDescription() { return "Long-term real-estate financing"; }
}