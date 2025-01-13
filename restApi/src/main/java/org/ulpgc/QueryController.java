package org.ulpgc;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    @PostMapping
    public ResponseEntity<List<String>> search(@RequestBody String query) {
        // Simulate query execution
        List<String> results = List.of("Result 1 for: " + query, "Result 2 for: " + query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/metadata")
    public ResponseEntity<Map<String, String>> getMetadata() {
        // Simulate metadata retrieval
        Map<String, String> metadata = Map.of(
                "indexSize", "1000",
                "lastUpdate", "2025-01-13"
        );
        return ResponseEntity.ok(metadata);
    }
}
