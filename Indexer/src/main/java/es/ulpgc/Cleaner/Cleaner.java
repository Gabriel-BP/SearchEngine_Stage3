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

        boolean startProcessing = (lastProcessedBaseName == null); // Start immediately if no file is recorded.

        if (rootFolder.exists() && rootFolder.isDirectory()) {
            // List all subfolders in the root directory
            File[] subfolders = rootFolder.listFiles(File::isDirectory);
            if (subfolders != null) {
                Arrays.sort(subfolders, Comparator.comparing(File::getName)); // Ensure folders are processed in order

                for (File folder : subfolders) {
                    File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt") || name.endsWith(".html"));
                    if (files != null) {
                        Arrays.sort(files, Comparator.comparing(File::getName)); // Ensure files are processed in order

                        for (File file : files) {
                            String baseName = file.getName().replaceFirst("[.][^.]+$", ""); // Remove the extension

                            if (!startProcessing) {
                                // Skip files until we find the last processed base name.
                                if (baseName.equals(lastProcessedBaseName)) {
                                    startProcessing = true;
                                }
                                continue;
                            }

                            System.out.println("Processing " + file.getName() + " in folder " + folder.getName());
                            books.add(processBook(file));
                            tracker.updateLastProcessed(file.getName()); // Update with the full name (extension ignored internally).
                        }
                    }
                }
            }
        }
        return books;
    }


}
