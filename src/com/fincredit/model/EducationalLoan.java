/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido
 * @date: 29-05-2026
 */

package com.fincredit.model;
/**
 * Class representing an educational loan product, extending the LoanProduct class and providing specific details such as base interest rate, maximum term, and description for student and academic financing.
 */
public class EducationalLoan extends LoanProduct {
    public EducationalLoan() { super("EDUCATIONAL", "🎓"); }
    // Overridden methods to provide specific details for the educational loan product
    @Override public double getBaseRate()    { return 5.0; }
    @Override public int getMaxTerm()        { return 120; }
    @Override public String getDescription() { return "Student and academic financing"; }
}