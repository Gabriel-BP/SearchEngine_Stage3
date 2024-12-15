package es.ulpgc.benchmark;

import es.ulpgc.GutenbergCrawler;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
@Fork(1)
public class GutenbergBenchmark {

    private GutenbergCrawler crawler;

    @Setup
    public void setup() {
        crawler = new GutenbergCrawler();
    }

    @Benchmark
    public void benchmarkCrawlBooks10() {
        crawler.crawlBooks(10);
    }

    @Benchmark
    public void benchmarkCrawlBooks100() {
        crawler.crawlBooks(100);
    }

    @Benchmark
    public void benchmarkCrawlBooks1000() {
        crawler.crawlBooks(1000);
    }
}
