package com.likedancesport;

import com.likedancesport.common.model.impl.Video;
import com.likedancesport.service.IVideoEncodingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        IVideoEncodingService bean = SpringApplication.run(Main.class, args).getBean(IVideoEncodingService.class);
    }
}