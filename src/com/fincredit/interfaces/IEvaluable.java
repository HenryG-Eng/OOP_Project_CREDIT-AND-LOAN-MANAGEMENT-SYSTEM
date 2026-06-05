/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido - Cristian Castro
 * @date: 29-05-2026
 */


package com.fincredit.interfaces;

import com.fincredit.model.Client;
import com.fincredit.model.EvaluationResult;
/**
 * Interface for evaluating a client's eligibility for a loan based on their financial information and the proposed monthly payment.
 */
public interface IEvaluable {
    EvaluationResult evaluate(Client client, double monthlyPayment);
    double getCapacityThreshold();
    String getEvaluatorName();
}