/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido
 * @date: 29-05-2026
 */

package com.fincredit.model;

public class EvaluationResult {

    private final boolean approved;
    private final double monthlyPayment;
    private final double maxCapacity;
    private final double paymentRatio;
    private final String reason;
    private final String evaluatorName;

    public EvaluationResult(boolean approved, double monthlyPayment,
                            double maxCapacity, double paymentRatio,
                            String reason, String evaluatorName) {
        this.approved       = approved;
        this.monthlyPayment = monthlyPayment;
        this.maxCapacity    = maxCapacity;
        this.paymentRatio   = paymentRatio;
        this.reason         = reason;
        this.evaluatorName  = evaluatorName;
    }

    public boolean isApproved()       { return approved; }
    public double getMonthlyPayment() { return monthlyPayment; }
    public double getMaxCapacity()    { return maxCapacity; }
    public double getPaymentRatio()   { return paymentRatio; }
    public String getReason()         { return reason; }
    public String getEvaluatorName()  { return evaluatorName; }

    public String getPaymentRatioFormatted() {
        return String.format("%.1f%%", paymentRatio * 100);
    }
}