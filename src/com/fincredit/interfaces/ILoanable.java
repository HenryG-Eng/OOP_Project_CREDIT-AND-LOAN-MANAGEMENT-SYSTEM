/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido - Cristian Castro
 * @date: 29-05-2026
 */


package com.fincredit.interfaces;

import com.fincredit.model.AmortizationRow;

/**
 * Interface for loanable products, defining methods for calculating monthly payments, generating amortization tables, and providing product information.
 */
import java.util.List;
/**
 * The ILoanable interface defines the contract for loanable products, including methods for calculating monthly payments, generating amortization tables, and providing product information such as maximum term, base rate, description, type, and icon.
 * 
 */
public interface ILoanable {
    double calculateMonthlyPayment(double principal, double annualRate, int terms);
    List<AmortizationRow> generateAmortizationTable(double principal, double annualRate, int terms);
    int getMaxTerm();
    double getBaseRate();
    String getDescription();
    String getType();
    String getIcon();
}