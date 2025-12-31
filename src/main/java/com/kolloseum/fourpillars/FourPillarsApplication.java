package com.kolloseum.fourpillars;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;

@EnableAdminServer
@SpringBootApplication(exclude = { RedisRepositoriesAutoConfiguration.class })
public class FourPillarsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FourPillarsApplication.class, args);
    }

}
