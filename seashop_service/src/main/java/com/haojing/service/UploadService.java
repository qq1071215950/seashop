package com.haojing.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    /**
     * 商品图片上传
     * @param file
     * @return
     */
    String upload(MultipartFile file);
}
