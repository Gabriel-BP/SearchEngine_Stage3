package es.ulpgc;

public class Main {
    // Create a new instance of the GutenbergCrawler class
    public static void main(String[] args) {
        GutenbergCrawler crawler = new GutenbergCrawler();
        int bookCount = 1;

        System.out.println("Starting crawling process...");

        // Loop to crawl books one by one every 30 seconds
        while (true) {
            System.out.println("Crawling book #" + bookCount + "...");
            crawler.crawlBooks(1); // Crawl one book at a time
            System.out.println("Book #" + bookCount + " crawled.");

            bookCount++;

            try {
                // Wait for 30 seconds before crawling the next book
                Thread.sleep(5000); // 30 seconds = 30000 milliseconds
            } catch (InterruptedException e) {
                // Handle any interruptions that occur during the sleep period
                System.out.println("Crawling process interrupted.");
                break; // Exit the loop if interrupted
            }
        }

        System.out.println("Crawling process stopped.");
    }
}
