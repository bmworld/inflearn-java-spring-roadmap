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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * <h1>HttpServlet Request 종류</h1> <br>
 *
 * <pre>
 *
 * 1. spring.servlet.multipart.enable: true
 * req => org.springframework.web.multipart.support.StandardMultipartHttpServletRequest@...
 * // parts =[org.apache.catalina.core.ApplicationPart@1a523020, org.apache.catalina.core.ApplicationPart@103a75ae]
 *
 * => @DispatcherServlet 에서 MultiPartResolver 실행 => HttpServletRequest를 MultipartHttpServletRequest 로 변환해줌
 *
 *
 * 2. spring.servlet.multipart.enable: false (일반 HttpServlet Reqeust)
 *   * req => org.apache.catalina.connector.RequestFacade@...
 *   * Collection<Part> parts = req.getParts() // parts = []
 *
 * </pre>
 */


@Slf4j
@Controller
@RequestMapping("servlet/v2")
@RequiredArgsConstructor
public class ServletUploadControllerV2 {


  @Value("${file.absolute-path}")
  private String fileDir;

  @GetMapping("/upload")
  public String newFile() {
    return "upload-form";
  }


  @PostMapping("/upload")
  public String saveFileV1(HttpServletRequest req) throws ServletException, IOException {
    log.info("req = {}", req);
    String itemName = req.getParameter("itemName");
    log.info("itemName = {}", itemName);

    Collection<Part> parts = req.getParts();
    log.info("parts ={}", parts);


    for (Part part : parts) {
      log.info("=== parts ===");
      log.info("name ={}", part.getName());
      Collection<String> headerNames = part.getHeaderNames();
      for (String headerName : headerNames) {
        log.info("headerName ={}: {}", headerName, part.getHeader(headerName));

      }

      // Util Method
      // Content-Disposition
      String fileName = part.getSubmittedFileName();
      log.info("--- submittedFileName={}", fileName);
      log.info("--- size={}", part.getSize());


      // Read Date
      InputStream inputStream = part.getInputStream();
      String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);// [주의] Binary <-> String 변환 시, CHARSET 반드시 지정
      log.info("body={}", body);


      if (!StringUtils.hasText(fileName)) continue;

      // Save File
      String uploadFileName = fileDir + fileName;

      Path root = Paths.get(fileDir);
      if (!Files.exists(root)) {
        initPath(fileDir);
      }

      log.info("--- uploadFilePath={}", uploadFileName);

      part.write(uploadFileName);

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
