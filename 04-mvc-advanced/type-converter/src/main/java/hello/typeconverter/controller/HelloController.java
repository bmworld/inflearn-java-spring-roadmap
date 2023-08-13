package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

  @GetMapping("/hello/v1")
  public String helloV1(HttpServletRequest request) {
    String data = request.getParameter("data");
    Integer intVal = Integer.valueOf(data);
    System.out.println("intVal = " + intVal);

    return "[v1] value = " + intVal;
  }


  // Spring 기본제공 타입 Converter가 무수히 많다.
  @GetMapping("/hello/v2")
  public String helloV2(@RequestParam Integer data) {
    Integer intVal = Integer.valueOf(data);
    System.out.println("intVal = " + intVal);

    return "[v2] value = " + intVal;
  }



  @GetMapping("/hello/ip-port")
  public String helloV2(@RequestParam IpPort ipPort) {
    System.out.println("--- ip = " + ipPort.getIp());
    System.out.println("--- port = " + ipPort.getPort());
    return "[ipPort] value = " + ipPort;
  }
}
