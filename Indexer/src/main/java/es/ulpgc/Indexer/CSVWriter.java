package es.ulpgc.Indexer;

import es.ulpgc.Cleaner.Book;

import java.io.*;
import java.util.*;

public class CSVWriter {
    private static final String INDEX_METADATA_FILE = "index_metadata.csv";
    private static final String INDEX_CONTENT_FILE = "index_content.csv";

    public void saveMetadataToCSV(Iterable<Book> books) {
        File file = new File(INDEX_METADATA_FILE);
        Set<String> existingEbookNumbers = new HashSet<>();

        // Step 1: Read existing ebookNumbers if the file exists
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                reader.readLine(); // Skip header
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 2); // Only need the ebookNumber
                    if (parts.length > 0) {
                        existingEbookNumbers.add(parts[0].trim().toLowerCase()); // Normalize and add to the set
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading existing metadata: " + e.getMessage());
            }
        }

        // Step 2: Write only new entries to the file
        boolean isFileNew = !file.exists(); // Check if the file is new
        try (FileWriter metadataWriter = new FileWriter(file, true)) { // Open in append mode
            if (isFileNew) {
                metadataWriter.append("ebookNumber,Title,Author,Date,Language,Credits\n"); // Write header if file is new
            }
            for (Book book : books) {
                String normalizedEbookNumber = book.ebookNumber.trim().toLowerCase();
                if (!existingEbookNumbers.contains(normalizedEbookNumber)) { // Skip duplicates
                    existingEbookNumbers.add(normalizedEbookNumber); // Avoid duplicate writing
                    metadataWriter.append(book.ebookNumber)
                            .append(",").append(book.title)
                            .append(",").append(book.author)
                            .append(",").append(book.date)
                            .append(",").append(book.language)
                            .append(",").append(book.credits)
                            .append("\n");
                }
            }
            System.out.println("Metadata saved to " + INDEX_METADATA_FILE);
        } catch (IOException e) {
            System.err.println("Error writing metadata to CSV: " + e.getMessage());
        }
    }


    public void saveContentToCSV(Map<String, Set<String>> wordToEbookNumbers) {
        File file = new File(INDEX_CONTENT_FILE);
        Map<String, Set<String>> existingData = new HashMap<>();

        // Step 1: Read existing data if the file exists
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                reader.readLine(); // Skip header
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 2);
                    if (parts.length == 2) {
                        String word = parts[0];
                        Set<String> ebookNumbers = new HashSet<>(Arrays.asList(parts[1].split(",")));
                        existingData.put(word, ebookNumbers);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading existing content index: " + e.getMessage());
            }
        }

        // Step 2: Merge new data into the existing map
        for (Map.Entry<String, Set<String>> entry : wordToEbookNumbers.entrySet()) {
            String word = entry.getKey();
            Set<String> newEbookNumbers = entry.getValue();

            // Merge with existing data
            existingData.computeIfAbsent(word, k -> new HashSet<>()).addAll(newEbookNumbers);
        }

        // Step 3: Write the updated data back to the CSV file
        try (FileWriter writer = new FileWriter(file)) {
            writer.append("Word,EbookNumbers\n"); // Write header
            for (Map.Entry<String, Set<String>> entry : existingData.entrySet()) {
                String word = entry.getKey();
                String ebookNumbers = String.join(",", entry.getValue());
                writer.append(word)
                        .append(",").append(ebookNumbers)
                        .append("\n");
            }
            System.out.println("Content index updated and saved to " + INDEX_CONTENT_FILE);
        } catch (IOException e) {
            System.err.println("Error writing updated content index: " + e.getMessage());
        }
    }

}
