package es.ulpgc.benchmark;

import es.ulpgc.Cleaner.Book;
import es.ulpgc.Cleaner.Cleaner;
import es.ulpgc.Indexer.Indexer;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime) // Measure average time
@OutputTimeUnit(TimeUnit.MILLISECONDS) // Results in milliseconds
@State(Scope.Thread) // State per thread
@Warmup(iterations = 1) // Warm-up iteration
@Measurement(iterations = 1) // Measure once after warm-up
@Fork(1) // Number of forks
public class CSVIndexerBenchmark {

    private Cleaner cleaner;
    private Indexer indexer;
    private List<Book> books;

    @Setup(Level.Trial) // Initial setup
    public void setup() throws IOException {
        cleaner = new Cleaner();
        indexer = new Indexer();
        books = cleaner.processAllBooks("datalake");
    }

    @Benchmark
    public void benchmarkCSVIndexing() throws IOException {
        indexer.indexBooks(books, "csv");
    }
}
