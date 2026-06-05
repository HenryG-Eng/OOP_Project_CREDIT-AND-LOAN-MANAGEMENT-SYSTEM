package com.fincredit.persistence;

import com.fincredit.logic.LoanProductFactory;
import com.fincredit.model.Loan;
import com.fincredit.model.LoanProduct;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * LoanFileManager — Management of loan data in a text file (loans.txt).
 *
 * File management: use BufferedReader/BufferedWriter for efficient file operations, ensuring proper resource management with try-with-resources.
 * Errors management: try/catch/finally

 */
public class LoanFileManager {

    private static final String FILE_PATH = "data/loans.txt";
    private static final String SEPARATOR = ",";

    //SAVE

    /**
     * Saves the list of loans to the file. Each loan is written as a line in the specified format.
     *
     * @param loans List of loans to save
     */
    public static void saveLoans(List<Loan> loans) {
        createDataFolder();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (Loan loan : loans) {
                // Polimorfismo
                String line = String.join(SEPARATOR,
                    loan.getId(),
                    loan.getClientId(),
                    loan.getProduct().getType(),
                    String.valueOf(loan.getPrincipal()),
                    String.valueOf(loan.getTerms()),
                    String.valueOf(loan.getAnnualRate()),
                    loan.getStatus().toString()
                );
                writer.write(line);
                writer.newLine();
            }

            System.out.println("Loans saved: " + loans.size() + " records.");

        } catch (IOException e) {
            System.err.println("Error saving loans: " + e.getMessage());
        }
    }

    //LOAD

    /**
     * Load the loans from the file. If the file doesn't exist, returns an empty list.
     * Builds Loan objects using the LoanProductFactory to ensure correct product types are instantiated.
     *
     * @return List of loans loaded from the file
     */
    public static List<Loan> loadLoans() {
        List<Loan> loans = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.out.println("No loans file found. Starting fresh.");
            return loans;
        }

        // Polimorfismo
        LoanProductFactory factory = LoanProductFactory.getInstance();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {

            String line;
            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                try {
                    String[] parts = line.split(SEPARATOR);

                    if (parts.length < 7) {
                        System.err.println("Skipping malformed line: " + line);
                        continue;
                    }

                    String loanId      = parts[0].trim();
                    String clientId    = parts[1].trim();
                    String productType = parts[2].trim();
                    double principal   = Double.parseDouble(parts[3].trim());
                    int terms          = Integer.parseInt(parts[4].trim());
                    double rate        = Double.parseDouble(parts[5].trim());
                    String statusStr   = parts[6].trim();

                   //Polimorfismo
                    LoanProduct product = factory.getProduct(productType);

                    if (product == null) {
                        System.err.println("Unknown product type: " + productType);
                        continue;
                    }

                    // Object manager: build Loan object with all data, then set status
                    Loan loan = new Loan(loanId, clientId, product,
                                        principal, terms, rate);

                    // Restart state
                    switch (statusStr) {
                        case "APPROVED" -> loan.approve(null);
                        case "REJECTED" -> loan.reject(null);
                        default         -> { } // PENDING — no cambia
                    }

                    loans.add(loan);

                } catch (NumberFormatException e) {
                    System.err.println("Error parsing line: " + line + " → " + e.getMessage());
                }
            }

            System.out.println("Loans loaded: " + loans.size() + " records.");

        } catch (IOException e) {
            System.err.println("Error loading loans: " + e.getMessage());
        }

        return loans;
    }

    // ── HELPER ────────────────────────────────────────────────

    private static void createDataFolder() {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}