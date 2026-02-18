package org.isuncy.wtet_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@MapperScan("org.isuncy.wtet_backend.mapper")
@SpringBootApplication
public class WtetBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WtetBackendApplication.class, args);
    }

}
