package es.ulpgc.Indexer;

import es.ulpgc.Cleaner.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BookIndexer {
    private final HashMapIndexer hashMapIndexer;
    private final Trie trie;

    public BookIndexer() {
        this.hashMapIndexer = new HashMapIndexer();
        this.trie = new Trie();
    }

    // Método para indexar los libros en paralelo
    public void indexBooks(List<Book> books) {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Callable<Void>> tasks = new ArrayList<>();

        for (Book book : books) {
            tasks.add(() -> {
                indexBook(book);
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

    public void indexBook(Book book) {
        for (String word : book.words) {
            hashMapIndexer.addWord(word, book.ebookNumber);
            trie.insert(word, book.ebookNumber);
        }
    }

    public HashMapIndexer getHashMapIndexer() {
        return hashMapIndexer;
    }

    public Trie getTrie() {
        return trie;
    }
}