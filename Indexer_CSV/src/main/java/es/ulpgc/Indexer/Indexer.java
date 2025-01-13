package es.ulpgc.Indexer;

import es.ulpgc.Cleaner.Book;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        ExecutorService executor = Executors.newFixedThreadPool(4);  // Adjust the number of threads based on the system

        // Submit indexing tasks to the thread pool
        for (Book book : books) {
            executor.submit(() -> {
                bookIndexer.indexBook(book);
            });
        }

        // Shut down the ExecutorService after completing the tasks
        executor.shutdown();
    }

    // Method to index the books and write the results
    public void indexBooks(List<Book> books, String outputType) {
        try {
            buildIndexes(books);  // Index the books in parallel

            // Write the results in the desired format (CSV or DataMart)
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
