package main.Start;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = {"main", "Repository"})
@Configuration
@SpringBootApplication()
public class StartRestServices {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(StartRestServices.class, args);
    }
}
