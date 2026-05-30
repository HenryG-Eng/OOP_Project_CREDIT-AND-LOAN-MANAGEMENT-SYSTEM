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