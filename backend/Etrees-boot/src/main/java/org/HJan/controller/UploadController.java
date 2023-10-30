package org.HJan.controller;

import lombok.extern.slf4j.Slf4j;
import org.HJan.common.Result;
import org.HJan.utils.AliOSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
/**
 * 文件上传Controller
 */
@Slf4j
@RestController

public class UploadController {

    @Autowired
    private AliOSSUtils aliOSSUtils;


    /**
     * 图片上传
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public Result<String> upload( MultipartFile file) throws IOException {
        log.info("文件上传，文件名：{}",file.getOriginalFilename());
        //调用aliyunOSS工具类(ALIOSSUtils)
        String url = aliOSSUtils.upload(file);
        log.info("文件上传完成，url：",url);
        return Result.success(url);
    }

}
