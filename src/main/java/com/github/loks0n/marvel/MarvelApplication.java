package com.github.loks0n.marvel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@SpringBootApplication
@EnableCaching
public class MarvelApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarvelApplication.class, args);
    }

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
