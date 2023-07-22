package hello.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
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
@RequestMapping("servlet/v1")
public class ServletUploadControllerV1 {

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
    return "upload-form";
  }
}
