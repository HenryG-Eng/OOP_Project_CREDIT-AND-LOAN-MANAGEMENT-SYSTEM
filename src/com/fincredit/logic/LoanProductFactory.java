/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido
 * @date: 29-05-2026
 */
package com.fincredit.logic;

import com.fincredit.model.*;
import java.util.*;

public class LoanProductFactory {

    private static LoanProductFactory instance;
    private final Map<String, LoanProduct> catalog;

    private LoanProductFactory() {
        catalog = new LinkedHashMap<>();
        register(new MortgageLoan());
        register(new EducationalLoan());
        register(new ConsumerLoan());
        register(new FreeInvestmentLoan());
    }

    public static LoanProductFactory getInstance() {
        if (instance == null) instance = new LoanProductFactory();
        return instance;
    }

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