package es.ulpgc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DatamartDataSource implements DataSource {
    private final String datamartRootPath;
    private final String metadataRootPath;

    public DatamartDataSource(String datamartRootPath, String metadataRootPath) {
        this.datamartRootPath = datamartRootPath;
        this.metadataRootPath = metadataRootPath;
    }

    @Override
    public Map<String, Set<String>> loadIndex() {
        return null; // Not used in Datamart
    }

    public Set<String> searchWord(String word) {
        File currentDir = new File(datamartRootPath);

        StringBuilder prefix = new StringBuilder();
        for (char c : word.toCharArray()) {
            prefix.append(c);
            currentDir = new File(currentDir, prefix.toString());
            if (!currentDir.exists() || !currentDir.isDirectory()) {
                System.out.println("Could not find the word: " + word);
                return new HashSet<>();
            }
        }

        File wordFile = new File(currentDir, word + ".txt");
        if (wordFile.exists() && wordFile.isFile()) {
            return readWordFile(wordFile);
        } else {
            System.out.println("File not found for the word: " + word);
            return new HashSet<>();
        }
    }

    private Set<String> readWordFile(File file) {
        Set<String> references = new HashSet<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);

            Map<String, Object> data = objectMapper.readValue(file, Map.class);

            // Extraer las referencias desde el JSON
            if (data.containsKey("references")) {
                @SuppressWarnings("unchecked")
                var refs = (Iterable<Object>) data.get("references");
                for (Object ref : refs) {
                    references.add(ref.toString());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file " + file.getPath() + ": " + e.getMessage());
        }
        return references;
    }

    public Map<String, Map<String, String>> loadMetadata(Set<String> ebookNumbers) {
        Map<String, Map<String, String>> metadata = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (String ebookNumber : ebookNumbers) {
            File metadataFile = new File(metadataRootPath, ebookNumber + "/metadata.json");
            if (metadataFile.exists()) {
                try {
                    @SuppressWarnings("unchecked")
                    Map<String, String> ebookMetadata = objectMapper.readValue(metadataFile, Map.class);
                    metadata.put(ebookNumber, ebookMetadata);
                } catch (IOException e) {
                    System.err.println("Error reading metadata for ebook " + ebookNumber + ": " + e.getMessage());
                }
            }
        }
        return metadata;
    }
}
