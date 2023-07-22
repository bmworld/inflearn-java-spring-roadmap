package hello.upload.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;


@Slf4j
@Controller
@RequestMapping("servlet/v3")
@RequiredArgsConstructor
public class ServletUploadControllerV3 {


  @Value("${file.absolute-path}")
  private String fileAbsoluteDir;

  @GetMapping("/upload")
  public String newFile() {
    return "upload-form";
  }


  @PostMapping("/upload")
  public String saveFileV1(
    @RequestParam String itemName,
    @RequestParam MultipartFile file,
    HttpServletRequest req
  ) throws IOException {
    log.info("req = {}", req);
    log.info("itemName = {}", itemName);
    log.info("multipartFile = {}", file);

    if (!file.isEmpty()) {
      String fullPath = fileAbsoluteDir + file.getOriginalFilename();
      log.info("--- 파일저장 fullPath = {}", fullPath);
      file.transferTo(new File(fullPath));
    }


    return "upload-form";
  }

  public void initPath(String uploadPath) {
    try {
      Files.createDirectories(Paths.get(uploadPath));
    } catch (IOException e) {
      throw new RuntimeException("Could not create upload folder!");
    }
  }
}
