package org.zhangrui.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zhangrui.common.result.Result;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 *
 * @author zhangrui
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/file")
public class FileUploadController {

    @Value("${file.upload-path:./uploads/}")
    private String uploadPath;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public Result<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        // 获取原始文件名和扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 生成新的文件名
        String newFilename = UUID.randomUUID().toString() + extension;

        // 按日期分目录存储
        String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        
        // 使用项目根目录作为基准
        File baseDir = new File("").getAbsoluteFile();
        String fullPath = baseDir.getPath() + File.separator + uploadPath + File.separator + datePath;

        // 创建目录
        File directory = new File(fullPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            // 保存文件
            File destFile = new File(directory, newFilename);
            file.transferTo(destFile);

            // 构建返回的URL
            String fileUrl = "/uploads/" + datePath.replace("\\", "/") + "/" + newFilename;

            Map<String, String> data = new HashMap<>();
            data.put("url", fileUrl);
            data.put("filename", newFilename);

            return Result.success(data);
        } catch (IOException e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }
}
