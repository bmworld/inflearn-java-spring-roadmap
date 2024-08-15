package hello.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TrafficController {

  private List<String> list = new ArrayList<>();

  @GetMapping("cpu")
  public String cpu() {

    log.info("---- cpu start {}", System.currentTimeMillis());

    long value = 0;

    for (long i = 0; i < 1000000000L; i++) {
      value++;
    }

    // get running time
    log.info("--- cpu done {}", System.currentTimeMillis());

    return "ok cpu!" + value;
  }

  @GetMapping("jvm")
  public String jvm() {

    log.info("---- jvm {}", System.currentTimeMillis());

    for (long i = 0; i < 1000000L; i++) {
      list.add("jvm !" + i);
    }

    log.info("---- jcm {}", System.currentTimeMillis());

    return "ok jvm! ";
  }

  @Autowired DataSource dataSource;

  @GetMapping("/jdbc")
  public String jdbc() throws SQLException {

    Connection conn = dataSource.getConnection();
    log.info("conneciton info ={}", conn);
    // conn.close(); // connection 종료 X

    return "ok jdbc!";
  }

  @GetMapping("/error-log")
  public String errorLog() {

    log.error("error log");

    return "error log!";
  }
}
