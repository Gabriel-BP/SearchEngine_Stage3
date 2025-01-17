package es.ulpgc.Indexer;

import es.ulpgc.Cleaner.Book;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Indexer {
    private final BookIndexer bookIndexer;
    private final DataMartWriter dataMartWriter;

    public Indexer() {
        this.bookIndexer = new BookIndexer();
        this.dataMartWriter = new DataMartWriter();
    }

    public void buildIndexes(List<Book> books) {
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
            executor.shutdown();
        }
    }

    public void indexBooks(List<Book> books, String outputType) {
        try {
            buildIndexes(books);

            switch (outputType.toLowerCase()) {
                case "datamart":
                    dataMartWriter.saveMetadataToDataMart(books);
                    dataMartWriter.saveContentToDataMart(bookIndexer.getHashMapIndexer().getIndex());
                    break;
                default:
                    System.err.println("Unsupported output type: " + outputType);
            }
        } catch (Exception e) {
            dataMartWriter.saveContentToDataMart(bookIndexer.getHashMapIndexer().getIndex());
            System.err.println("Error during indexing: " + e.getMessage());
        }
    }
}
