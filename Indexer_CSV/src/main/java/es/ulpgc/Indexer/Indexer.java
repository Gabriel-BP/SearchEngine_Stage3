package es.ulpgc.Indexer;

import es.ulpgc.Cleaner.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Indexer {
    private final BookIndexer bookIndexer;
    private final CSVWriter csvWriter;

    // Constructor
    public Indexer() {
        this.bookIndexer = new BookIndexer();
        this.csvWriter = new CSVWriter();
    }

    // Method that will use ExecutorService to parallelize indexing
    public void buildIndexes(List<Book> books) {
        // Crear un único ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Callable<Void>> tasks = new ArrayList<>();

        for (Book book : books) {
            tasks.add(() -> {
                bookIndexer.indexBook(book);
                return null;
            });
        }

        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            System.err.println("Error during parallel execution: " + e.getMessage());
        } finally {
            // Apagar el ExecutorService después de completar las tareas
            executor.shutdown();
        }
    }

    // Method to index the books and write the results
    public void indexBooks(List<Book> books, String outputType) {
        try {
            buildIndexes(books);

            switch (outputType.toLowerCase()) {
                case "csv":
                    csvWriter.saveMetadataToCSV(books);
                    csvWriter.saveContentToCSV(bookIndexer.getHashMapIndexer().getIndex());
                    break;
                default:
                    System.err.println("Unsupported output type: " + outputType);
            }
        } catch (Exception e) {
            csvWriter.saveContentToCSV(bookIndexer.getHashMapIndexer().getIndex());
            System.err.println("Error during indexing: " + e.getMessage());
        }
    }
}
