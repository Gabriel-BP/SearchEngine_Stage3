package es.ulpgc.Indexer;

import es.ulpgc.Cleaner.Book;

public class BookIndexer {
    private final HashMapIndexer hashMapIndexer;
    private final Trie trie;

    public BookIndexer() {
        this.hashMapIndexer = new HashMapIndexer();
        this.trie = new Trie();
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
