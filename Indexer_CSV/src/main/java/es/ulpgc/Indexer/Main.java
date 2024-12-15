package es.ulpgc.Indexer;

import es.ulpgc.Cleaner.Book;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // Create a ScheduledExecutorService to run the task periodically
        var scheduler = Executors.newScheduledThreadPool(1);

        // Define the task to run periodically
        Runnable task = () -> {
            try {
                es.ulpgc.Cleaner.Cleaner cleaner = new es.ulpgc.Cleaner.Cleaner();
                List<Book> books = cleaner.processAllBooks("datalake");
                Indexer indexer = new Indexer();
                indexer.indexBooks(books, "csv");
            } catch (IOException e) {
                System.err.println("Error processing books: " + e.getMessage());
            }
        };

        // Schedule the task to run every minute (60 seconds)
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
    }
}


