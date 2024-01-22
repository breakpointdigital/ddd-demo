package pl.bpd.ddd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class DddDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DddDemoApplication.class, args);
    }
}
