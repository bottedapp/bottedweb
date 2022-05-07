package app.botted;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot class
 */
@EnableScheduling
@SpringBootApplication
public class web extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(web.class);
    }

    /**
     * Main method
     * @param args args
     * @throws Exception Exception
     */
    public static void main(String[] args) throws Exception {
        SpringApplication.run(web.class, args);
    }
}
