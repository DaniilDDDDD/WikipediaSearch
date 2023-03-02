package com.wikipedia.wikipediasearchservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class WikipediaSearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikipediaSearchServiceApplication.class, args);
    }

}
