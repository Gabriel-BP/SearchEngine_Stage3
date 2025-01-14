package es.ulpgc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "es.ulpgc")
public class ApiRestMain {
    public static void main(String[] args) {
        SpringApplication.run(ApiRestMain.class, args);
    }
}
