package com.rabbiter.cm.controller;

import com.rabbiter.cm.common.file.FileUploadUtils;
import com.rabbiter.cm.common.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
////FileUploadController 是一个 Spring 的 RestController，主要负责处理文件上传和文件删除的请求。
// 它使用 FileUploadUtils 工具类来执行文件上传和删除操作，并使用 ResponseResult 类生成响应结果。
// 该控制器提供了针对不同类型文件（用户、电影、影院、演员）的文件上传接口，以及文件删除接口，同时使用日志记录操作信息。
/**
 * 封装图片上传请求
 */
@RestController
@RequestMapping("/upload")
public class FileUploadController {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/user")
    public ResponseResult uploadUser(@RequestParam("file") MultipartFile file) throws IOException {
        FileUploadUtils.setDefaultBaseDir(FileUploadUtils.userPath);
        String filename = FileUploadUtils.upload(file);
        log.debug("上传文件名 : " + filename);
        return ResponseResult.success((Object) filename);
    }

    @PostMapping("/movie")
    public ResponseResult uploadMovie(@RequestParam("file") MultipartFile file) throws IOException {
        FileUploadUtils.setDefaultBaseDir(FileUploadUtils.moviePath);
        String filename = FileUploadUtils.upload(file);
        log.debug("上传文件名 : " + filename);
        return ResponseResult.success((Object) filename);
    }

    @PostMapping("/cinema")
    public ResponseResult uploadCinema(@RequestParam("file") MultipartFile file) throws IOException {
        FileUploadUtils.setDefaultBaseDir(FileUploadUtils.cinemaPath);
        String filename = FileUploadUtils.upload(file);
        log.debug("上传文件名 : " + filename);
        return ResponseResult.success((Object) filename);
    }

    @PostMapping("/actor")
    public ResponseResult uploadActor(@RequestParam("file") MultipartFile file) throws IOException {
        FileUploadUtils.setDefaultBaseDir(FileUploadUtils.actorPath);
        String filename = FileUploadUtils.upload(file);
        log.debug("上传文件名 : " + filename);
        return ResponseResult.success((Object) filename);
    }

    @RequestMapping("/delete")
    public ResponseResult deletePicture(String filePath) {
        try {
            String path = ResourceUtils.getURL("classpath:").getPath().substring(1) + "static" + filePath;
            log.debug("删除文件路径为：" + path);
            boolean flag = FileUploadUtils.deleteFile(path);
            log.debug(flag ? "删除成功" : "删除失败");
        } catch (FileNotFoundException e) {
            System.out.println("删除文件不存在");
        } finally {
            return ResponseResult.success();
        }
    }

}
