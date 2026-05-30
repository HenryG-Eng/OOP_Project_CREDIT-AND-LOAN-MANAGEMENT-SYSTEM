 /** the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido
 * @date: 29-05-2026
 */
package com.fincredit.registry;

import com.fincredit.interfaces.IEvaluable;
import com.fincredit.logic.CreditEvaluator;
import com.fincredit.logic.LoanProductFactory;
import com.fincredit.model.*;

import java.util.*;

public class LoanRegistry {

    private static LoanRegistry instance;

    private final Map<String, Client> clients;
    private final Map<String, Loan>   loans;
    private final LoanProductFactory  productFactory;
    private IEvaluable                evaluator;

    private int clientSeq = 1;
    private int loanSeq   = 1;

    private LoanRegistry() {
        clients        = new LinkedHashMap<>();
        loans          = new LinkedHashMap<>();
        productFactory = LoanProductFactory.getInstance();
        evaluator      = new CreditEvaluator();
        seedData();
    }

    public static LoanRegistry getInstance() {
        if (instance == null) instance = new LoanRegistry();
        return instance;
    }

    private void seedData() {
        Client ana    = registerClient("Ana Martínez",  "CC-1001",
                "ana@email.com",    5_000_000, 1_500_000);
        Client carlos = registerClient("Carlos Rivera", "CC-1002",
                "carlos@email.com", 8_000_000, 3_200_000);
        Client laura  = registerClient("Laura Torres",  "CC-1003",
                "laura@email.com",  3_200_000, 2_600_000);
        Client diego  = registerClient("Diego Suárez",  "CC-1004",
                "diego@email.com", 12_000_000, 4_000_000);

        requestLoan(ana.getId(),    "MORTGAGE",       180_000_000, 240, null);
        requestLoan(carlos.getId(), "EDUCATIONAL",     25_000_000,  60, null);
        requestLoan(laura.getId(),  "CONSUMER",        15_000_000,  36, null);
        requestLoan(diego.getId(),  "FREE_INVESTMENT", 50_000_000,  48, null);
    }

    public Client registerClient(String name, String document, String email,
                                 double monthlyIncome, double monthlyExpenses) {
        String id = String.format("C%04d", clientSeq++);
        Client client = new Client(id, name, document, email,
                                   monthlyIncome, monthlyExpenses);
        clients.put(id, client);
        return client;
    }

    public Loan requestLoan(String clientId, String productType,
                            double principal, int terms, Double customRate) {
        Client client       = clients.get(clientId);
        LoanProduct product = productFactory.getProduct(productType);
        double rate         = (customRate != null) ? customRate : product.getBaseRate();
        String loanId       = String.format("L%04d", loanSeq++);

        Loan loan               = new Loan(loanId, clientId, product,
                                           principal, terms, rate);
        EvaluationResult result = evaluator.evaluate(client, loan.getMonthlyPayment());

        if (result.isApproved()) loan.approve(result);
        else                     loan.reject(result);

        loans.put(loanId, loan);
        client.addLoanId(loanId);
        return loan;
    }

    public Client getClient(String id)  { return clients.get(id); }
    public Loan   getLoan(String id)    { return loans.get(id); }

    public List<Client> getAllClients() {
        return Collections.unmodifiableList(new ArrayList<>(clients.values()));
    }

    public List<Loan> getAllLoans() {
        return Collections.unmodifiableList(new ArrayList<>(loans.values()));
    }

    public void setEvaluator(IEvaluable evaluator) {
        this.evaluator = evaluator;
    }
}