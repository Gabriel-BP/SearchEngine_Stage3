package es.ulpgc;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String DOWNLOAD_FOLDER = "datalake";

    public static String getDate() {
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyy");
        return hoy.format(format);
    }

    public static void createFolder(String folderPath) {
        try {
            Files.createDirectories(Paths.get(folderPath));
        } catch (IOException e) {
            System.err.println("Could not create folder: " + e.getMessage());
        }
    }

    public static List<String> getFoldersInPath() {
        List<String> folders = new ArrayList<>();
        File folder = new File(DOWNLOAD_FOLDER);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        folders.add(file.getName());
                    }
                }
            }
        }
        return folders;
    }

    public static String getLatestNonEmptyFolder() {
        List<String> folders = getFoldersInPath();
        String latestFolder = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");

        for (String folderName : folders) {
            try {
                LocalDate folderDate = LocalDate.parse(folderName, formatter);
                File folder = new File(DOWNLOAD_FOLDER, folderName);
                if (folder.listFiles() != null && folder.listFiles().length > 0) {
                    if (latestFolder == null || folderDate.isAfter(LocalDate.parse(latestFolder, formatter))) {
                        latestFolder = folderName;
                    }
                }
            } catch (Exception e) {
                System.out.println("Skipping invalid folder format: " + folderName);
            }
        }
        return latestFolder;
    }

    public static String getFileWithLargestBookID(String folderName) {
        if (folderName == null) {
            System.err.println("No non-empty folder available.");
            return "010.txt";
        }

        File folder = new File(DOWNLOAD_FOLDER, folderName);
        File[] files = folder.listFiles();
        String largestFile = null;
        int largestBookID = -1;

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String filename = file.getName();
                    try {
                        String bookIDStr = filename.substring(2, filename.lastIndexOf('.'));
                        int bookID = Integer.parseInt(bookIDStr);
                        if (bookID > largestBookID) {
                            largestBookID = bookID;
                            largestFile = filename;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid file format: " + filename);
                    }
                }
            }
        }
        return largestFile;
    }
}