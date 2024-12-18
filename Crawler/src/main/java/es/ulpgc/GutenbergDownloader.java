package es.ulpgc;

import org.jsoup.Jsoup;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

public class GutenbergDownloader {
    private static final String BASE_URL = "https://www.gutenberg.org/";

    public static boolean downloadBook(String bookId, String targetFolder, String webID) {
        String[] formats = {"txt", "html", "epub", "mobi"};
        for (String format : formats) {
            String url = BASE_URL + "cache/epub/" + bookId + "/pg" + bookId + "." + format;
            String filename = targetFolder + "/" + webID + bookId + "." + format;
            try (InputStream in = Jsoup.connect(url).ignoreContentType(true).execute().bodyStream()) {
                Files.copy(in, Paths.get(filename), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Downloaded " + filename);
                return true;
            } catch (IOException e) {
                System.err.println("Could not download " + url + ": " + e.getMessage());
            }
        }
        return false;
    }
}
