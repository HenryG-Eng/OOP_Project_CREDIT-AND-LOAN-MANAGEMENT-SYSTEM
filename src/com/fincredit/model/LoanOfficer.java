/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido
 * @date: 29-05-2026
 */

package com.fincredit.model;

/**
 * Class representing a loan officer in the financial system, extending the Person class and including attributes specific to their role,
 * such as branch affiliation and counts of approved and rejected loans. It provides methods to record loan decisions and retrieve summary information about the officer's performance.
 */
public class LoanOfficer extends Person {

    private String branch;
    private int approvedLoansCount;
    private int rejectedLoansCount;

    public LoanOfficer(String id, String name, String document,
                       String email, String branch) {
        super(id, name, document, email);
        this.branch             = branch;
        this.approvedLoansCount = 0;
        this.rejectedLoansCount = 0;
    }

    @Override
    public String getRole() { return "LOAN_OFFICER"; }
    /**
	 * Method to get a summary of the loan officer's performance, including role, branch affiliation, and counts of approved and rejected loans.
	 * @return A formatted string summarizing the loan officer's role, branch, and loan decision counts.
	 */
    @Override
    public String getSummary() {
        return String.format(
            "Role: %s | Branch: %s | Approved: %d | Rejected: %d",
            getRole(), branch, approvedLoansCount, rejectedLoansCount
        );
    }
    // Methods to record loan decisions, incrementing the respective counts for approvals and rejections.
    public void recordApproval()  { approvedLoansCount++; }
    public void recordRejection() { rejectedLoansCount++; }

    public String getBranch()            { return branch; }
    public int getApprovedLoansCount()   { return approvedLoansCount; }
    public int getRejectedLoansCount()   { return rejectedLoansCount; }
    public void setBranch(String branch) { this.branch = branch; }
}