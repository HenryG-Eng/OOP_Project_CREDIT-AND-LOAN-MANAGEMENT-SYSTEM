/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido
 * @date: 29-05-2026
 */

package com.fincredit.model;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Class representing a loan application, including details such as client ID, loan product, principal amount, terms, interest rate, monthly payment, amortization table, status, and evaluation result.
 */
public class Loan {

    public enum Status { PENDING, APPROVED, REJECTED }//Loan application status

    private final String id;
    private final String clientId;
    private final LocalDateTime createdAt;
    private final LoanProduct product;
    private final double principal;
    private final int terms;
    private final double annualRate;
    private final double monthlyPayment;
    private final List<AmortizationRow> amortizationTable;
    private Status status;
    private EvaluationResult evaluationResult;

    public Loan(String id, String clientId, LoanProduct product, double principal, int terms, double annualRate) {
        this.id               = id;
        this.clientId         = clientId;
        this.product          = product;
        this.principal        = principal;
        this.terms            = terms;
        this.annualRate       = annualRate;
        this.status           = Status.PENDING;
        this.createdAt        = LocalDateTime.now();
        this.monthlyPayment   = product.calculateMonthlyPayment(principal, annualRate, terms);
        this.amortizationTable = product.generateAmortizationTable(principal, annualRate, terms);
    }
    /**
     * Method to approve a loan application, updating the status to APPROVED and storing the evaluation result for reference.
     * @param result
     */
    public void approve(EvaluationResult result) {
        this.status           = Status.APPROVED;
        this.evaluationResult = result;
    }
    /**
     * Method to reject a loan application, updating the status to REJECTED and storing the evaluation result for reference.
     * @param result
     */
    public void reject(EvaluationResult result) {
        this.status           = Status.REJECTED;
        this.evaluationResult = result;
    }

    public double getTotalCost()     { return monthlyPayment * terms; }
    public double getTotalInterest() { return getTotalCost() - principal; }
    public boolean isApproved()      { return status == Status.APPROVED; }

    public String getId()                               { return id; }
    public String getClientId()                         { return clientId; }
    public LoanProduct getProduct()                     { return product; }
    public double getPrincipal()                        { return principal; }
    public int getTerms()                               { return terms; }
    public double getAnnualRate()                       { return annualRate; }
    public double getMonthlyPayment()                   { return monthlyPayment; }
    public List<AmortizationRow> getAmortizationTable() { return amortizationTable; }
    public Status getStatus()                           { return status; }
    public EvaluationResult getEvaluationResult()       { return evaluationResult; }
    public LocalDateTime getCreatedAt()                 { return createdAt; }
}