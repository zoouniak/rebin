package com.rebin.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RebinApplication {

    public static void main(String[] args) {
        SpringApplication.run(RebinApplication.class, args);
    }

}
