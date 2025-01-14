package es.ulpgc.Indexer;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import es.ulpgc.Cleaner.Book;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Start a Hazelcast instance
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

        // Define the task
        Runnable task = () -> {
            try {
                es.ulpgc.Cleaner.Cleaner cleaner = new es.ulpgc.Cleaner.Cleaner();
                List<Book> books = cleaner.processAllBooks("datalake");
                Indexer indexer = new Indexer();
                indexer.indexBooks(books, "csv");
                System.out.println("Books processed and indexed successfully.");
            } catch (IOException e) {
                System.err.println("Error processing books: " + e.getMessage());
            }
        };

        // Execute the task immediately
        System.out.println("Starting task execution...");
        task.run();
        System.out.println("Task completed.");
    }
}