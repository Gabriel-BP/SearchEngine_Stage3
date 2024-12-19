package es.ulpgc;

import com.hazelcast.collection.IQueue;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;


public class Main {
    private static final String PROGRESS_MAP_FILE = "progressMap.dat";

    public static void main(String[] args) {
        // Initialize Hazelcast
        HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance();

        IQueue<Integer> taskQueue = hazelcast.getQueue("bookIdQueue");
        IMap<Integer, Boolean> progressMap = hazelcast.getMap("progressMap");

        // Load progressMap from disk
        FileManager.loadProgressMap(progressMap, PROGRESS_MAP_FILE);

        // Populate taskQueue with unprocessed tasks
        if (taskQueue.isEmpty()) {
            System.out.println("Initializing task queue...");
            for (int i = 1; i <= 10000; i++) { // Adjust range as needed
                if (!progressMap.getOrDefault(i, false)) {
                    boolean offered = taskQueue.offer(i);
                    if (!offered) {
                        System.err.println("Failed to add book ID #" + i + " to the queue.");
                    }
                }
            }
        }

        // Crawler instance
        GutenbergCrawler crawler = new GutenbergCrawler();
        System.out.println("Starting crawling process...");

        while (true) {
            try {
                Integer bookId = taskQueue.poll();
                if (bookId == null) {
                    System.out.println("No more tasks available. Exiting...");
                    break;
                }

                if (progressMap.getOrDefault(bookId, false)) {
                    System.out.println("Book ID #" + bookId + " already processed. Skipping...");
                    continue;
                }

                System.out.println("Crawling book ID #" + bookId);
                crawler.crawlBooks(bookId, 1); // Process one book
                progressMap.put(bookId, true);
                FileManager.saveProgressMap(progressMap, PROGRESS_MAP_FILE);
                System.out.println("Book ID #" + bookId + " crawled successfully.");

            } catch (Exception e) {
                System.err.println("Error during crawling: " + e.getMessage());
            }
        }

        // Save progress map before exiting
        FileManager.saveProgressMap(progressMap, PROGRESS_MAP_FILE);
        System.out.println("Crawling process completed.");
    }
}



