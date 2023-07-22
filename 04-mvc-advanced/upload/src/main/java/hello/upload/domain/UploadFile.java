package hello.upload.domain;

import lombok.Data;

@Data
public class UploadFile {

  private String uploadFileName; // 고객이 업로드한 파일명

  private String storedFileName;// 서버 내부에서 관리하는 파일명 => 고객이 업로드한 파일명이 겹치지 않도록 별도의 관리하는 파일명을 사용한다.

  public UploadFile(String uploadFileName, String storedFileName) {
    this.uploadFileName = uploadFileName;
    this.storedFileName = storedFileName;
  }
}
