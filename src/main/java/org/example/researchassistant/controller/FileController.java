package org.example.researchassistant.controller;

import org.example.researchassistant.domain.FileAttachment;
import org.example.researchassistant.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "false")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("cid") Integer cid,
            @RequestParam("uid") Integer uid) {

        try {
            logger.info("接收文件上传请求 - 文件名: {}, cid: {}, uid: {}",
                    file.getOriginalFilename(), cid, uid);

            FileAttachment fileAttachment = fileService.uploadFile(file, cid, uid);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "文件上传成功");
            response.put("data", fileAttachment);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("文件上传失败", e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * 获取会话的所有文件
     */
    @GetMapping("/session/{cid}")
    public ResponseEntity<List<FileAttachment>> getFilesByCid(@PathVariable Integer cid) {
        try {
            List<FileAttachment> files = fileService.getFilesByCid(cid);
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            logger.error("获取会话文件失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 获取用户的所有文件
     */
    @GetMapping("/user/{uid}")
    public ResponseEntity<List<FileAttachment>> getFilesByUid(@PathVariable Integer uid) {
        try {
            List<FileAttachment> files = fileService.getFilesByUid(uid);
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            logger.error("获取用户文件失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/{fid}")
    public ResponseEntity<?> deleteFile(@PathVariable Integer fid) {
        try {
            fileService.deleteFile(fid);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "文件删除成功");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("文件删除失败", e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "文件删除失败");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 获取文件详情
     */
    @GetMapping("/{fid}")
    public ResponseEntity<?> getFileById(@PathVariable Integer fid) {
        try {
            FileAttachment file = fileService.getFileById(fid);
            if (file == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "文件不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            return ResponseEntity.ok(file);

        } catch (Exception e) {
            logger.error("获取文件详情失败", e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取文件详情失败");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}