package es.ulpgc;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GutenbergCrawler {
    private static final String DOWNLOAD_FOLDER = "datalake";
    private static final String webID = "01";

    public void crawlBooks(int numBooks) {
        String date = FileManager.getDate();
        String folderPath = DOWNLOAD_FOLDER + "/" + date;
        FileManager.createFolder(folderPath);

        String latestFolder = FileManager.getLatestNonEmptyFolder();
        String latestFile = FileManager.getFileWithLargestBookID(latestFolder);
        int startIndex = latestFile != null ? Integer.parseInt(latestFile.substring(2, latestFile.lastIndexOf('.'))) + 1 : 1;
        for (int i = startIndex; i < startIndex + numBooks; i++) {
            boolean success = GutenbergDownloader.downloadBook(String.valueOf(i), folderPath, webID);
            if (!success) {
                System.out.println("Skipping book ID " + i);
            }
        }
    }
}

