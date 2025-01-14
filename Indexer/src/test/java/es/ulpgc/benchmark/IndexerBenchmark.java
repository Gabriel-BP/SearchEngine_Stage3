package es.ulpgc.benchmark;

import es.ulpgc.Cleaner.Book;
import es.ulpgc.Indexer.BookIndexer;
import es.ulpgc.Indexer.HashMapIndexer;
import es.ulpgc.Indexer.Trie;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Setup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(org.openjdk.jmh.annotations.Scope.Thread)
public class IndexerBenchmark {

    private BookIndexer bookIndexer;
    private List<Book> books;

    @Setup
    public void setUp() {
        // Prepare test data
        books = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            List<String> words = new ArrayList<>();
            for (int j = 0; j < 100; j++) {
                words.add("word" + j);
            }
            books.add(new Book("Book" + i, "Author" + i, "2025", "English", "Credits", "ebook" + i, words, ""));
        }
        bookIndexer = new BookIndexer();
    }

    @Benchmark
    public void benchmarkHashMapIndexer() {
        HashMapIndexer hashMapIndexer = new HashMapIndexer();
        for (Book book : books) {
            for (String word : book.words) {
                hashMapIndexer.addWord(word, book.ebookNumber);
            }
        }
    }

    @Benchmark
    public void benchmarkTrieIndexer() {
        Trie trie = new Trie();
        for (Book book : books) {
            for (String word : book.words) {
                trie.insert(word, book.ebookNumber);
            }
        }
    }

    @Benchmark
    public void benchmarkBookIndexer() {
        for (Book book : books) {
            bookIndexer.indexBook(book);
        }
    }
}
