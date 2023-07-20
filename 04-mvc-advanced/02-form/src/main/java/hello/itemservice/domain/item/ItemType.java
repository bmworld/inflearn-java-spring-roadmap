package hello.itemservice.domain.item;

public enum ItemType {
  Book("도서", "doseo"), FOOD("음식", "emmsik"), ETC("기타", "gita");

  private final String description;
  private final String konglish;

  ItemType(String description, String konglish) {
    this.description = description;
    this.konglish = konglish;
  }

  public String getDescription() {
    return description;
  }
}
