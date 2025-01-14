package es.ulpgc.Cleaner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Cleaner {

    private final TextCleaner textCleaner;

    public Cleaner() {
        Set<String> stopwords = StopwordsLoader.loadStopwords();
        this.textCleaner = new TextCleaner(stopwords);
    }

    public Book processBook(File file) throws IOException {
        String content = new String(Files.readAllBytes(file.toPath()));

        Map<String, String> metadata = MetadataExtractor.extractMetadata(content);

        int startIdx = content.indexOf("*** START OF THIS PROJECT GUTENBERG EBOOK");
        if (startIdx != -1) {
            content = content.substring(startIdx);
        }
        String fullContent = content;
        List<String> words = textCleaner.cleanText(content);
        String ebookNumber = file.getName().replaceFirst("[.][^.]+$", "");  // Eliminar la extensión
        return new Book(
                metadata.get("title"),
                metadata.get("author"),
                metadata.get("date"),
                metadata.get("language"),
                metadata.get("credits"),
                ebookNumber,
                words,
                fullContent
        );
    }

    public List<Book> processAllBooks(String rootPath) throws IOException {
        File rootFolder = new File(rootPath);
        List<Book> books = new ArrayList<>();
        LastProcessedTracker tracker = new LastProcessedTracker("LastProcessed.txt");
        String lastProcessedBaseName = tracker.getLastProcessed();

        boolean startProcessing = (lastProcessedBaseName == null);
        int lastProcessedId = -1;

        if (lastProcessedBaseName != null) {
            try {
                lastProcessedId = Integer.parseInt(lastProcessedBaseName.substring(2));
            } catch (NumberFormatException e) {
                System.err.println("Error parsing last processed ID: " + e.getMessage());
            }
        }

        if (rootFolder.exists() && rootFolder.isDirectory()) {
            File[] subfolders = rootFolder.listFiles(File::isDirectory);
            if (subfolders != null) {
                Arrays.sort(subfolders, Comparator.comparing(File::getName));

                for (File folder : subfolders) {
                    File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt") || name.endsWith(".html"));
                    if (files != null) {
                        // Sort files numerically by their IDs
                        Arrays.sort(files, (f1, f2) -> {
                            String baseName1 = f1.getName().replaceFirst("[.][^.]+$", "");
                            String baseName2 = f2.getName().replaceFirst("[.][^.]+$", "");
                            try {
                                int id1 = Integer.parseInt(baseName1.substring(2));
                                int id2 = Integer.parseInt(baseName2.substring(2));
                                return Integer.compare(id1, id2);
                            } catch (NumberFormatException e) {
                                System.err.println("Error parsing file IDs: " + e.getMessage());
                                return baseName1.compareTo(baseName2);
                            }
                        });

                        for (File file : files) {
                            String baseName = file.getName().replaceFirst("[.][^.]+$", "");
                            int currentId = -1;

                            try {
                                currentId = Integer.parseInt(baseName.substring(2));
                            } catch (NumberFormatException e) {
                                System.err.println("Error parsing file ID for " + file.getName() + ": " + e.getMessage());
                                continue;
                            }

                            if (!startProcessing) {
                                if (currentId == lastProcessedId) {
                                    startProcessing = true;
                                }
                                continue;
                            }

                            System.out.println("Processing " + file.getName() + " in folder " + folder.getName());
                            books.add(processBook(file));
                            tracker.updateLastProcessed(file.getName());
                        }
                    }
                }
            }
        }
        return books;
    }

}
