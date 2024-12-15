package es.ulpgc.Indexer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HashMapIndexer {
    private final Map<String, Set<String>> hashMapIndex;

    public HashMapIndexer() {
        this.hashMapIndex = new HashMap<>();
    }

    public void addWord(String word, String ebookNumber) {
        hashMapIndex.computeIfAbsent(word, k -> new HashSet<>()).add(ebookNumber);
    }

    public Set<String> search(String word) {
        return hashMapIndex.getOrDefault(word, new HashSet<>());
    }

    public Map<String, Set<String>> getIndex() {
        return hashMapIndex;
    }

}
