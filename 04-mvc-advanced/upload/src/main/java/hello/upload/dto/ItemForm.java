package hello.upload.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


// 상품 저잦ㅇ 폼
@Data
public class ItemForm {
  private Long itemId;
  private String itemName;
  private List<MultipartFile> imageFiles;
  private MultipartFile attachedFile;
}
