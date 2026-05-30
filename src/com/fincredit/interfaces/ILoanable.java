package com.fincredit.interfaces;

import com.fincredit.model.AmortizationRow;

/**
 * Interface for loanable products, defining methods for calculating monthly payments, generating amortization tables, and providing product information.
 */
import java.util.List;

public interface ILoanable {
    double calculateMonthlyPayment(double principal, double annualRate, int terms);
    List<AmortizationRow> generateAmortizationTable(double principal, double annualRate, int terms);
    int getMaxTerm();
    double getBaseRate();
    String getDescription();
    String getType();
    String getIcon();
}