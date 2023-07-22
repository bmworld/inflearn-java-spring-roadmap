package hello.upload.controller;

import hello.upload.domain.Item;
import hello.upload.domain.UploadFile;
import hello.upload.dto.ItemForm;
import hello.upload.file.FileStore;
import hello.upload.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

  private final ItemRepository itemRepository;
  private final FileStore fileStore;

  @GetMapping("/items/new")
  public String newItem(@ModelAttribute ItemForm form) {
    return "item-form";
  }


  @PostMapping("/items/new")
  public String saveItem(
    RedirectAttributes redirectAttributes,
    @ModelAttribute ItemForm form
  ) throws IOException {

    UploadFile storedAttachedFile = fileStore.storeFile(form.getAttachedFile());
    List<UploadFile> storedAttachedFiles = fileStore.storeFiles(form.getImageFiles());

    // DB 에는 파일 경로만 저장
    // 파일은 AWS S3 등에 저장하는 거다.
    Item item = new Item();
    item.setItemName(form.getItemName());
    item.setAttachFile(storedAttachedFile);
    item.setImageFiles(storedAttachedFiles);

    itemRepository.save(item);

    Long itemId = item.getId();
    redirectAttributes.addAttribute("itemId", itemId);
    return "redirect:/items/{itemId}";
  }


  @GetMapping("/items/{id}")
  public String getItems(@PathVariable Long id, Model model) {

    Item item = itemRepository.findById(id);
    model.addAttribute("item", item);

    return "item-view";

  }


  @ResponseBody
  @GetMapping("/images/{filename}")
  public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
    String path = getResourceFullPath(filename);
    return new UrlResource(path);
  }

  @GetMapping("attach/{itemId}")
  public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
    Item item = itemRepository.findById(itemId);
    String storedFileName = item.getAttachFile().getStoredFileName();
    String uploadFileName = item.getAttachFile().getUploadFileName();
    UrlResource urlResource = new UrlResource(getResourceFullPath(storedFileName));
    log.info("uploadFileName={}", uploadFileName);


    // =============================================================================================================================
    // ** 중요: 다운로드가 되려면, response Header => contentDisposition 추가 필수.
    // =============================================================================================================================
    String encodedUploadFilename = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
    String contentDisposition = "attachment; filename=\"" + encodedUploadFilename + "\"";
    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
      .body(urlResource);

  }


  private String getResourceFullPath(String filename) {
    String fileProtocol = "file:";
    String path = fileProtocol + fileStore.getFullPath(filename); // ex. file:/Users/.../upload/4945c40c-7dd4-4205-b0e8-f932f7fe7db7-sample.png
    return path;
  }
}
