package hello.itemservice.domain.item;

public enum Region {

  SEOUL("서울"),
  BUSAN("부산"),
  GYEONGBUK("경북"),
  JEJU("제주");

  private final String kor;

  Region(String name) {
    this.kor = name;
  }

  public String getKore () {
    return kor;
  }
}
