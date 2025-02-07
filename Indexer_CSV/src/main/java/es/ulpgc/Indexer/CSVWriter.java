package es.ulpgc.Indexer;

import es.ulpgc.Cleaner.Book;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CSVWriter {
    private static final String INDEX_METADATA_FILE = "index_metadata.csv";
    private static final String INDEX_CONTENT_FILE = "index_content.csv";

    public void saveMetadataToCSV(Iterable<Book> books) {
        File file = new File(INDEX_METADATA_FILE);
        Set<String> existingEbookNumbers = Collections.newSetFromMap(new ConcurrentHashMap<>());

        // Leer números de libros existentes (si el archivo existe)
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                reader.readLine(); // Saltar la cabecera
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 2);
                    if (parts.length > 0) {
                        existingEbookNumbers.add(parts[0].trim().toLowerCase());
                    }
                }
            } catch (IOException e) {
                System.err.println("Error leyendo metadatos: " + e.getMessage());
            }
        }

        // Escritura concurrente
        try (BufferedWriter metadataWriter = new BufferedWriter(new FileWriter(file, true))) {
            for (Book book : books) {
                String normalizedEbookNumber = book.ebookNumber.trim().toLowerCase();
                if (!existingEbookNumbers.contains(normalizedEbookNumber)) {
                    synchronized (metadataWriter) {
                        metadataWriter.write(String.format("%s,%s,%s,%s,%s,%s%n",
                                book.ebookNumber, book.title, book.author, book.date, book.language, book.credits));
                    }
                    existingEbookNumbers.add(normalizedEbookNumber);
                }
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo en CSV: " + e.getMessage());
        }
    }

    public void saveContentToCSV(Map<String, Set<String>> wordToEbookNumbers) {
        File file = new File(INDEX_CONTENT_FILE);
        ConcurrentHashMap<String, Set<String>> existingData = new ConcurrentHashMap<>();

        // Leer datos existentes si el archivo existe
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                reader.readLine(); // Saltar cabecera
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 2);
                    if (parts.length == 2) {
                        existingData.put(parts[0], new HashSet<>(Arrays.asList(parts[1].split(","))));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error leyendo índice: " + e.getMessage());
            }
        }

        // Escritura concurrente
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Word,EbookNumbers\n");
            for (Map.Entry<String, Set<String>> entry : wordToEbookNumbers.entrySet()) {
                writer.write(entry.getKey() + "," + String.join(",", entry.getValue()) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo en CSV: " + e.getMessage());
        }
    }
}
