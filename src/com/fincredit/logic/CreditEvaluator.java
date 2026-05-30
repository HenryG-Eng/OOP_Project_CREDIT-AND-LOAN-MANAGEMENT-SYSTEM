/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido
 * @date: 29-05-2026
 */

package com.fincredit.logic;

import com.fincredit.interfaces.IEvaluable;
import com.fincredit.model.Client;
import com.fincredit.model.EvaluationResult;

public class CreditEvaluator implements IEvaluable {

    private static final double DEFAULT_THRESHOLD = 0.30;
    private final double capacityThreshold;

    public CreditEvaluator() {
        this.capacityThreshold = DEFAULT_THRESHOLD;
    }

    public CreditEvaluator(double capacityThreshold) {
        this.capacityThreshold = capacityThreshold;
    }

    @Override
    public EvaluationResult evaluate(Client client, double monthlyPayment) {
        double maxCapacity   = client.getMaxPaymentCapacity();
        double monthlyIncome = client.getMonthlyIncome();
        double paymentRatio  = (monthlyIncome > 0)
                ? monthlyPayment / monthlyIncome : Double.MAX_VALUE;
        boolean approved     = monthlyPayment <= maxCapacity;

        String reason = approved
            ? String.format("APPROVED — Payment ($%.2f) is within the %.0f%% limit ($%.2f).",
                monthlyPayment, capacityThreshold * 100, maxCapacity)
            : String.format("REJECTED — Payment ($%.2f) exceeds the %.0f%% limit ($%.2f) by $%.2f.",
                monthlyPayment, capacityThreshold * 100, maxCapacity,
                monthlyPayment - maxCapacity);

        return new EvaluationResult(approved, monthlyPayment, maxCapacity,
                paymentRatio, reason, getEvaluatorName());
    }

    @Override public double getCapacityThreshold() { return capacityThreshold; }
    @Override public String getEvaluatorName()     { return "StandardCreditEvaluator"; }
}