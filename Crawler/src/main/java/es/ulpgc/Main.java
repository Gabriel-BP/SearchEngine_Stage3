package es.ulpgc;

import com.hazelcast.collection.IQueue;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class Main {
    private static final String PROGRESS_MAP_FILE = "progressMap.dat";

    public static void main(String[] args) {
        // Initialize Hazelcast
        HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance();
        IQueue<Integer> taskQueue = hazelcast.getQueue("bookIdQueue");
        IMap<Integer, Boolean> progressMap = hazelcast.getMap("progressMap");

        // Initialize resources
        ResourceInitializer resourceInitializer = new ResourceInitializer(taskQueue, progressMap, PROGRESS_MAP_FILE);
        resourceInitializer.initialize();

        // Start crawling process
        CrawlerManager crawlerManager = new CrawlerManager(taskQueue, progressMap, PROGRESS_MAP_FILE);
        crawlerManager.startCrawling();

        // Shutdown Hazelcast
        hazelcast.shutdown();
    }
}
