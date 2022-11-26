package com.bhu19.movie.reco;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.bhu19.movie.reco.persistance.dao"})
public class BhuMovieRecoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BhuMovieRecoApplication.class, args);
    }

}
