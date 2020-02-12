package com.haojing.service.impl;

import com.haojing.entity.Users;
import com.haojing.result.ResponseResult;
import com.haojing.service.UploadService;
import com.haojing.utlis.CookieUtils;
import com.haojing.utlis.DateUtil;
import com.haojing.utlis.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author jiange
 * @version 1.0
 * @date 2020/1/15 15:19
 */
@Service
public class UploadServiceImpl implements UploadService {
    private static final Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);
    // 支持的文件类型
    // 支持的文件类型
    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String upload(MultipartFile file) {

        try {
            // 1、图片信息校验
            // 1)校验文件类型
            String type = file.getContentType();
            if (!suffixes.contains(type)) {
                logger.info("上传失败，文件类型不匹配：{}", type);
                throw new RuntimeException("图片上传的类型不正确");
            }
            // 2)校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                logger.info("上传失败，文件内容不符合要求");
                throw new RuntimeException("图片上传的内容不符合要求");
            }
            // 2、保存图片
            // 2.1、生成保存目录
            File dir = new File("C:\\opt\\plate");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String newFileName = System.currentTimeMillis() + file.getOriginalFilename();
            // 2.2、保存图片
            file.transferTo(new File(dir, newFileName));
            // 2.3、拼接图片地址
            String url = "http://www.seashop.com"+"/"+ newFileName;
            return url;
        } catch (Exception e) {
            throw new RuntimeException("上传失败，系统出了点问题");
        }


    }
}
