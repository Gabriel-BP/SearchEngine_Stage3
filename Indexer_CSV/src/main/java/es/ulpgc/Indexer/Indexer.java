package es.ulpgc.Indexer;

import es.ulpgc.Cleaner.Book;

import java.util.List;

public class Indexer {
    private final BookIndexer bookIndexer;
    private final CSVWriter csvWriter;
    private final DataMartWriter dataMartWriter;

    public Indexer() {
        this.bookIndexer = new BookIndexer();
        this.csvWriter = new CSVWriter();
        this.dataMartWriter = new DataMartWriter();
    }

    public void buildIndexes(List<Book> books) {
        for (Book book : books) {
            bookIndexer.indexBook(book);
        }
    }

    public void indexBooks(List<Book> books, String outputType) {
        try {
            buildIndexes(books); // Index both HashMap and Trie

            if ("csv".equalsIgnoreCase(outputType)) {
                csvWriter.saveMetadataToCSV(books);
                csvWriter.saveContentToCSV(bookIndexer.getHashMapIndexer().getIndex());
                System.out.println("Indexing completed and saved to CSV files.");
            } else if ("datamart".equalsIgnoreCase(outputType)) {
                DataMartWriter dataMartWriter = new DataMartWriter();
                dataMartWriter.saveContentToDataMart(bookIndexer.getHashMapIndexer().getIndex());
                dataMartWriter.saveMetadataToDataMart(books);
                System.out.println("Indexing completed and saved to data marts.");
            } else {
                System.err.println("Unknown output type: " + outputType);
            }
        } catch (Exception e) {
            System.err.println("Error during indexing: " + e.getMessage());
        }
    }


}

