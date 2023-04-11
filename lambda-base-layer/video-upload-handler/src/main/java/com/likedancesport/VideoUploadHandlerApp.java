package com.likedancesport;

import com.likedancesport.service.IVideoEncodingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VideoUploadHandlerApp {
    public static void main(String[] args) {
        IVideoEncodingService bean = SpringApplication.run(VideoUploadHandlerApp.class, args).getBean(IVideoEncodingService.class);
    }
}