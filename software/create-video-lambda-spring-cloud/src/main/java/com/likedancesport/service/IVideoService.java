package com.likedancesport.service;


import com.likedancesport.model.CreateVideoRequest;
import com.likedancesport.model.CreateVideoResult;

public interface IVideoService {
    CreateVideoResult createVideo(CreateVideoRequest createVideoRequest);
}
