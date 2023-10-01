package hello.advanced.trace.threadLocal.code;

import lombok.extern.slf4j.Slf4j;

import static hello.util.customUtils.sleep;

@Slf4j
public class FieldService {
  private String storedName;

  public String logic(String name) {
    log.info("저장 name ={} -> storedName ={}", name, storedName);
    storedName = name;
    sleep(1000); // 저장에 1초 가량 소요된다고 가정
    log.info("조회 storedName ={}", storedName);
    return storedName;
  }

}
