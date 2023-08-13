package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ConverterController {

  /**
   * <h1>view(JSP)파일에서, {{}} 괄호가 2개 있으면, Converter가 적용된다.</h1>
   * Spring 기본제공 타입 Converter가 무수히 많다.
   */

  @GetMapping("/converter/view")
  public String convertView(Model model) {

    String ip = "127.0.0.1";
    int port = 8000;
    model.addAttribute("ipPort", new IpPort(ip, port));
    model.addAttribute("number", 10000);


    return "converter-view";
  }





  @GetMapping("/converter/edit")
  public String convertForm(Model model) {
    String ip = "127.0.0.1";
    int port = 8000;
    IpPort ipPort = new IpPort(ip, port);
    Form form = new Form(ipPort);
    model.addAttribute("form", form);

    return "converter-form";
  }



  @PostMapping("/converter/edit")
  public String convertFormEdit(@ModelAttribute Form form, Model model) {
    IpPort ipPort = form.getIpPort();
    model.addAttribute("ipPort", ipPort);

    return "converter-view";
  }


  @Data
  static class Form {
    private IpPort ipPort;

    public Form(IpPort ipPort) {
      this.ipPort = ipPort;
    }
  }

}
