package com.iweb.demo.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    String uploadAlyiVideo(MultipartFile file);

    void removeVideoList(List videoIdList);
}
