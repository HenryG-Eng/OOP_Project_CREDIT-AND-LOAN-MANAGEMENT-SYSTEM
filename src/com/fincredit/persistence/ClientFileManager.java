package com.fincredit.persistence;

import com.fincredit.model.Client;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ClientFileManager — Management of client data in a text file (clients.txt).
 *
 * File Management: use BufferedReader/BufferedWriter for efficient file operations, ensuring proper resource management with try-with-resources.
 *
 * Errors Management: try/catch/finally
 *
 */
public class ClientFileManager {

    //data folder and file path
    private static final String FILE_PATH = "data/clients.txt";
    private static final String SEPARATOR = ",";

    //SAVE

    /**
     * Saves the list of clients to the file. Each client is written as a line in the specified format.
     * @param clients
     */
    public static void saveClients(List<Client> clients) {
        createDataFolder();

        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (Client client : clients) {
                
                String line = String.join(SEPARATOR,
                    client.getId(),
                    client.getName(),
                    client.getDocument(),
                    client.getEmail(),
                    String.valueOf(client.getMonthlyIncome()),
                    String.valueOf(client.getMonthlyExpenses())
                );
                writer.write(line);
                writer.newLine();
            }

            System.out.println("Clients saved: " + clients.size() + " records.");

        } catch (IOException e) {
            System.err.println("Error saving clients: " + e.getMessage());
        }
    }

    //LOAD

    /**
     * Loads clients from the file. If the file doesn't exist, returns an empty list.
     * @return List of clients loaded from the file
     */
    public static List<Client> loadClients() {
        List<Client> clients = new ArrayList<>();//
        File file = new File(FILE_PATH);//check if the file exists before attempting to read

   
        if (!file.exists()) {
            System.out.println("No clients file found. Starting fresh.");
            return clients;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {

            String line;
            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                try {
                    String[] parts = line.split(SEPARATOR);

                    if (parts.length < 6) {
                        System.err.println("Skipping malformed line: " + line);
                        continue;
                    }
                    // Parse client data from the line, trimming whitespace and converting numeric values

                    String id       = parts[0].trim();
                    String name     = parts[1].trim();
                    String document = parts[2].trim();
                    String email    = parts[3].trim();
                    double income   = Double.parseDouble(parts[4].trim());
                    double expenses = Double.parseDouble(parts[5].trim());

                    Client client = new Client(id, name, document, email, income, expenses);
                    clients.add(client);

                } catch (NumberFormatException e) {
                    System.err.println("Error parsing line: " + line + " → " + e.getMessage());
                }
            }

            System.out.println("Clients loaded: " + clients.size() + " records.");

        } catch (IOException e) {
            System.err.println("Error loading clients: " + e.getMessage());
        }

        return clients;
    }

    // ── HELPER ────────────────────────────────────────────────

    private static void createDataFolder() {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdirs();
            System.out.println("Created data folder.");
        }
    }
}