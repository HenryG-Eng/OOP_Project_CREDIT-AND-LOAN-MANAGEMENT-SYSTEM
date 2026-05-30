/* PROGRAM: A application that calculates the monthly payment for a loan based on the loan amount, interest rate, and loan term. It also generates an amortization table showing the breakdown of each payment over the life of the loan. 
 * the application purpose is to help users understand the financial implications of taking out a loan and to assist them in making informed decisions about their borrowing options.
 * @author:Henry Garrido
 * @date: 29-05-2026
 */

package com.fincredit.model;

import java.time.LocalDateTime;

/**
 * Abstract class representing a person in the financial system, serving as a base for specific roles like clients and employees. It includes common attributes such as ID, 
 * name, document, email, and creation timestamp, along with abstract methods for role-specific information.
 */

public abstract class Person {

    protected final String id;
    protected String name;
    protected String document;
    protected String email;
    protected final LocalDateTime createdAt;//create a variable type LocalDateTime to store the date and time when the person was created

    protected Person(String id, String name, String document, String email) {
        this.id        = id;
        this.name      = name;
        this.document  = document;
        this.email     = email;
        this.createdAt = LocalDateTime.now();
    }
    //methods to be implemented by subclasses
    public abstract String getRole();
    public abstract String getSummary();
    //utility methods for display
    public String getDisplayName() { return name + " [" + document + "]"; }
    public String getHeader()      { return "[" + getRole() + "] " + id + "   " + name; }
    
    
    //getters and setters
    public void setName(String name)  { this.name = name; }
    public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDocument() {
		return document;
	}
	public String getEmail() {
		return email;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setDocument(String document) { this.document = document; }
    public void setEmail(String email)       { this.email = email; }
}