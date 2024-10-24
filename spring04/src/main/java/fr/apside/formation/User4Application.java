package fr.apside.formation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class User4Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(User4Application.class);

    public static void main(String[] args) {
        LOGGER.info("Lancement de l'application");
        SpringApplication.run(User4Application.class, args);
    }

}
