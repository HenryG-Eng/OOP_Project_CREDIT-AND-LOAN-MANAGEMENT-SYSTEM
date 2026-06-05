/** the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido - Cristian Castro
 * @date: 29-05-2026
 */
package com.fincredit.registry;

import com.fincredit.interfaces.IEvaluable;
import com.fincredit.logic.CreditEvaluator;
import com.fincredit.logic.LoanProductFactory;
import com.fincredit.model.*;
import com.fincredit.persistence.ClientFileManager;
import com.fincredit.persistence.LoanFileManager;

import java.util.*;
/**
 * LoanRegistry — Centralized management of clients and loans, implemented as a singleton to ensure a single instance throughout the application.
 *  It provides methods for registering clients, requesting loans, and retrieving client and loan information. The registry also handles loading and saving data to files, as well as seeding initial data if no records are found.
 */
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

        loadFromFiles();

        if (clients.isEmpty()) {
            seedData();
        }
    }

    public static LoanRegistry getInstance() {
        if (instance == null) instance = new LoanRegistry();
        return instance;
    }

    /**
     * Loads clients and loans from their respective files. For each loaded client and loan, it updates the internal maps and ensures that the sequence numbers for generating new IDs 
     * are set correctly to avoid collisions with existing records.
     */

    private void loadFromFiles() {
        List<Client> savedClients = ClientFileManager.loadClients();// Load clients first to ensure we can link loans to existing clients
        for (Client client : savedClients) {
            clients.put(client.getId(), client);
            try {
                int num = Integer.parseInt(client.getId().replace("C", ""));
                if (num >= clientSeq) clientSeq = num + 1;
            } catch (NumberFormatException e) {
                System.err.println("Could not parse client ID: " + client.getId());
            }
        }

        List<Loan> savedLoans = LoanFileManager.loadLoans();
        // Load loans and link them to clients
        for (Loan loan : savedLoans) {
            loans.put(loan.getId(), loan);
            Client client = clients.get(loan.getClientId());
            if (client != null) {
                client.addLoanId(loan.getId());
            }
            try {
                int num = Integer.parseInt(loan.getId().replace("L", ""));
                if (num >= loanSeq) loanSeq = num + 1;
            } catch (NumberFormatException e) {
                System.err.println("Could not parse loan ID: " + loan.getId());
            }
        }
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

    /**
     * Registers a new client with the provided information, generates a unique ID for the client, and saves the updated list of clients to disk.
     *  The method returns the newly created Client object.
     * @param name
     * @param document
     * @param email
     * @param monthlyIncome
     * @param monthlyExpenses
     * @return
     */

    public Client registerClient(String name, String document, String email,
                                 double monthlyIncome, double monthlyExpenses) {
        String id = String.format("C%04d", clientSeq++);
        Client client = new Client(id, name, document, email,
                                   monthlyIncome, monthlyExpenses);
        clients.put(id, client);
        ClientFileManager.saveClients(getAllClients());
        return client;
    }

    /**
     * Deletes a client and all their associated loans from the registry.
     * Also persists the updated lists to disk.
     *
     * @param clientId The ID of the client to remove.
     * @return true if the client was found and removed, false otherwise.
     */
    public boolean deleteClient(String clientId) {
        Client client = clients.get(clientId);
        if (client == null) return false;

        // Remove all loans belonging to this client
        List<String> loanIdsToRemove = new ArrayList<>(client.getLoanIds());
        for (String loanId : loanIdsToRemove) {
            loans.remove(loanId);
        }

        // Remove the client
        clients.remove(clientId);

        // Persist changes
        ClientFileManager.saveClients(getAllClients());
        LoanFileManager.saveLoans(getAllLoans());

        return true;
    }

    /**
     * Processes a loan request for a given client and product type, calculating the monthly payment and evaluating the client's creditworthiness using the provided evaluator.
     * @param clientId
     * @param productType
     * @param principal
     * @param terms
     * @param customRate
     * @return the created Loan object, which will have its status set to approved or rejected based on the evaluation result. The loan is also saved to disk and linked to the client.
     */

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

        LoanFileManager.saveLoans(getAllLoans());

        return loan;
    }

    /**
     * Retrieves a client or loan by their unique ID from the registry. If the ID does not exist, it returns null.
     * @param id
     * @return
     */
    

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