package hello.typeconverter.controller;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class FormatterController {



  @GetMapping("/formatter/form")
  public String formatterForm(Model model) {
    FormattedForm form = new FormattedForm();
    form.setNumber(100000);
    form.setLocalDateTime(LocalDateTime.now());
    model.addAttribute("form", form);

    return "formatter-form";
  }



  @PostMapping("/formatter/form")
  public String formatterEdit(@ModelAttribute FormattedForm form, Model model) {
    model.addAttribute("form", form);
    return "formatter-view";
  }




  @Data
  static class FormattedForm{
    @NumberFormat(pattern = "###,###")
    private Integer number;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // ex. LocalDateTime 중간의 "T" 없이 넣어보자.
    private LocalDateTime localDateTime;


  }
}
