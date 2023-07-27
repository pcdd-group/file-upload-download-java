package org.pcdd.fileuploaddownload;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

@RestController
public class SmallFileUploadController {

    /**
     * MultipartFile + transferTo(File dest)
     */
    @PostMapping("/upload-1")
    public String upload1(@RequestPart("file") MultipartFile mf) throws IOException {
        String filename = mf.getOriginalFilename();
        mf.transferTo(new File("d:/", filename));

        return "upload success";
    }

    /**
     * MultipartFile + transferTo(Path dest)
     */
    @PostMapping("/upload-2")
    public String upload2(@RequestParam("file") MultipartFile mf) throws IOException {
        String filename = mf.getOriginalFilename();
        Path path = Paths.get("upload", filename);
        mf.transferTo(path);

        return "upload success";
    }

    /**
     * MultipartFile + Files.write(Path path, byte[] bytes, OpenOption... options)
     */
    @PostMapping("/upload-3")
    public String upload3(@RequestParam("file") MultipartFile mf) throws IOException {
        String filename = mf.getOriginalFilename();
        Path path = Paths.get("upload", filename);
        Files.write(path, mf.getBytes());

        return "upload success";
    }

    /**
     * MultipartFile + Files.copy(InputStream in, Path target, CopyOption... options)
     */
    @PostMapping("/upload-4")
    public String upload4(@RequestParam("file") MultipartFile mf) throws IOException {
        String filename = mf.getOriginalFilename();
        Path path = Paths.get("upload", filename);
        // 若文件已存在，则会抛出 FileAlreadyExistsException
        Files.copy(mf.getInputStream(), path);

        return "upload success";
    }

    /**
     * MultipartHttpServletRequest + 前四种上传方法
     * 同时支持单/多文件上传
     */
    @PostMapping("/upload-5")
    public String upload5(MultipartHttpServletRequest req) {
        req.getFileNames().forEachRemaining(name -> {
            for (MultipartFile mf : req.getFiles(name)) {
                // 同理又有四种
                // mf.transferTo(File dest)
                // mf.transferTo(Path dest)
                // Files.write(Path path, byte[] bytes, OpenOption... options)
                // Files.copy(mf.getInputStream(), path);
            }
        });

        return "upload success";
    }

    /**
     * List<MultipartFile> + 四种上传方法
     * 同时支持单/多文件上传
     */
    @PostMapping("/upload-6")
    public String upload6(@RequestParam("files") List<MultipartFile> files) {
        for (MultipartFile mf : files) {
            System.out.println(mf.getOriginalFilename());
            // 同理又有四种
            // mf.transferTo(File dest)
            // mf.transferTo(Path dest)
            // Files.write(Path path, byte[] bytes, OpenOption... options)
            // Files.copy(mf.getInputStream(), path);
        }

        return "upload success";
    }

    @PostMapping("/upload-7")
    public String upload7(@RequestParam("files") MultipartFile[] files) {
        for (MultipartFile mf : files) {
            System.out.println(mf.getOriginalFilename());
            // 同理又有四种
            // mf.transferTo(File dest)
            // mf.transferTo(Path dest)
            // Files.write(Path path, byte[] bytes, OpenOption... options)
            // Files.copy(mf.getInputStream(), path);
        }

        return "upload success";
    }

    @PostMapping("/upload-8")
    public String upload8(HttpServletRequest req) throws ServletException, IOException {
        // 获取上传的文件流，单文件
        Part filePart = req.getPart("file");
        String filename = filePart.getSubmittedFileName();

        // 构建目标文件对象
        Path path = Paths.get("upload", filename);
        byte[] bytes = filePart.getInputStream().readAllBytes();
        InputStream in = filePart.getInputStream();

        Files.write(path, bytes);
        Files.copy(in, path);

        return "upload success";
    }

    @PostMapping("/upload-9")
    public String upload9(HttpServletRequest req) throws ServletException, IOException {
        Collection<Part> parts = req.getParts();

        for (Part part : parts) {
            String filename = part.getSubmittedFileName();

            Path path = Paths.get("upload", filename);
            byte[] bytes = part.getInputStream().readAllBytes();
            InputStream in = part.getInputStream();

            Files.write(path, bytes);
            //Files.copy(in,path);
        }

        return "upload success";
    }

    /**
     * Content-Type 必须为 multipart/form-data
     */
    @PostMapping("/upload")
    public String handleFileUpload(@RequestPart("file") byte[] fileBytes,
                                   @RequestPart("file") InputStream fileInputStream,
                                   @RequestPart("file") Part filePart) {
        // 处理文件上传逻辑
        // ...

        System.out.println("fileBytes = " + fileBytes);
        System.out.println("fileInputStream = " + fileInputStream);
        System.out.println("filePart = " + filePart);

        return "upload success";
    }


}
