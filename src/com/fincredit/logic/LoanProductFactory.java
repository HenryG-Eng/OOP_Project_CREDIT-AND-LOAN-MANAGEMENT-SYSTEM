/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido
 * @date: 29-05-2026
 */
package com.fincredit.logic;

import com.fincredit.model.*;
import java.util.*;
/**
 * The LoanProductFactory class is a singleton that manages the creation and retrieval of different loan products. It maintains a catalog of available loan products and provides methods to access them, ensuring that only one instance of the factory exists throughout the application.
 */
public class LoanProductFactory {

    private static LoanProductFactory instance;// Singleton instance
    private final Map<String, LoanProduct> catalog;// Using LinkedHashMap to preserve insertion order for display purposes

    private LoanProductFactory() {
        catalog = new LinkedHashMap<>();
        register(new MortgageLoan());
        register(new EducationalLoan());
        register(new ConsumerLoan());
        register(new FreeInvestmentLoan());
    }
    /**
     * Provides access to the singleton instance of the LoanProductFactory.
     * @return
     */
    public static LoanProductFactory getInstance() {
        if (instance == null) instance = new LoanProductFactory();
        return instance;
    }
    /**
     * Registers a loan product in the factory's catalog. This method is private to ensure that products are only added through the constructor, maintaining control over the available products.
     * @param product
     */
    private void register(LoanProduct product) {
        catalog.put(product.getType(), product);
    }

    public LoanProduct getProduct(String type) { return catalog.get(type); }
    public boolean hasProduct(String type)     { return catalog.containsKey(type); }

    public List<LoanProduct> getAllProducts() {
        return Collections.unmodifiableList(new ArrayList<>(catalog.values()));
    }

    public Set<String> getProductTypes() {
        return Collections.unmodifiableSet(catalog.keySet());
    }
}