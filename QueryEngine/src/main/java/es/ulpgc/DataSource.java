package es.ulpgc;

import java.util.Map;
import java.util.Set;

public interface DataSource {
    Map<String, Set<String>> loadIndex();
}
