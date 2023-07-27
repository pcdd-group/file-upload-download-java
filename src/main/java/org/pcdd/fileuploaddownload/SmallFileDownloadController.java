package org.pcdd.fileuploaddownload;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class SmallFileDownloadController {

    /**
     * Servlet 下载文件 1
     */
    @GetMapping("/download-0/{filename}")
    public void download0(@PathVariable("filename") String filename, HttpServletResponse resp) throws IOException {
        Path path = Paths.get("upload", filename);
        File file = path.toFile();

        // 检查文件是否存在并可读
        if (!file.exists() || !file.canRead()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename=" + filename);

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
             ServletOutputStream sos = resp.getOutputStream()) {
            sos.write(bis.readAllBytes());
        }
    }

    /**
     * Servlet 下载文件 2
     */
    @GetMapping("/download-1/{filename}")
    public void download1(@PathVariable("filename") String filename, HttpServletResponse resp) throws IOException {
        Path path = Paths.get("upload", filename);
        File file = path.toFile();

        // 检查文件是否存在并可读
        if (!file.exists() || !file.canRead()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setContentType("application/octet-stream");
        ContentDisposition contentDisposition = ContentDisposition
                .attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build();
        resp.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());

        Files.copy(path, resp.getOutputStream());
    }

    /**
     * Spring 下载文件 1
     */
    @GetMapping("/download-2/{filename}")
    public ResponseEntity<Resource> download2(@PathVariable String filename) throws IOException {
        // 构建文件路径
        Path filePath = Paths.get("upload", filename);
        Resource resource = new UrlResource(filePath.toUri());

        // 检查文件是否存在并可读
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 设置下载响应头
        ContentDisposition contentDisposition = ContentDisposition
                .attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    /**
     * Spring 下载文件 2
     */
    @GetMapping("/download-4/{filename}")
    public ResponseEntity<StreamingResponseBody> download4(@PathVariable String filename) throws IOException {
        // 构建文件路径
        Path filePath = Paths.get("upload", filename);
        Resource resource = new UrlResource(filePath.toUri());

        // 检查文件是否存在并可读
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 设置下载响应头
        ContentDisposition contentDisposition = ContentDisposition.attachment().filename(filename, StandardCharsets.UTF_8).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);

        StreamingResponseBody responseBody = response -> {
            try (InputStream inputStream = resource.getInputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    response.write(buffer, 0, bytesRead);
                }
            }
        };

        return ResponseEntity.ok()
                .headers(headers)
                .body(responseBody);
    }

    /**
     * inline
     */
    @GetMapping("/download-3/{fileName}")
    public ResponseEntity<Resource> download3(@PathVariable String fileName) throws IOException {
        // 构建文件路径
        Path filePath = Paths.get("upload").resolve(fileName);
        Resource resource = new UrlResource(filePath.toUri());

        // 检查文件是否存在并可读
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 设置下载响应头
        ContentDisposition contentDisposition = ContentDisposition.inline().build();
        HttpHeaders headers = new HttpHeaders();
        String mimeType = Files.probeContentType(filePath);
        System.out.println("mimeType = " + mimeType);
        headers.setContentType(MediaType.valueOf(mimeType + "; charset=UTF-8"));
        headers.setContentDisposition(contentDisposition);

        System.out.println("contentDisposition = " + contentDisposition);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

}
