package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

  @RequestMapping("/response-view-v1")
  public ModelAndView responseViewV1() {
    ModelAndView mv = new ModelAndView("response/hello");
    mv.addObject("data", "HELLO!!!!!");

    return mv;
  }


  /**
   * * String 반환 시 => View 논리 이름 (response/hello)과 Controller Request URL 동일한 경우. 다음 경로의 View Template 랜더링 됨 (templates/response/hello)
   */
  @RequestMapping("/response-view-v2")
  public String responseViewV2(
    Model model
  ) {
    model.addAttribute("data", "responseViewV2 => view 논리이름을 찾는다.");

    return "response/hello"; // * String 반환 시 => View 논리 이름 (response/hello)과 Controller Request URL 동일한 경우. 다음 경로의 View Template 랜더링 됨 (templates/response/hello)
  }


  /**
   * * Void 반환 시 => @Controller 사용하고, `HttpServletResponse`, `OutputStream(Writer` 같은 HTTP Message Body 처리하는 Parameter 없는 경우, 요청 URL을 참고하여, `VIEW 논리이름` 이름으로 사용하여, 일치히는 VIEW 반환
   */
  @RequestMapping("/response/hello")
  public void responseViewV3(
    Model model
  ) {
    model.addAttribute("data", "* Void 반환 시 => @Controller 사용하고, `HttpServletResponse`, `OutputStream(Writer` 같은 HTTP Message Body 처리하는 Parameter 없는 경우, 요청 URL을 참고하여, `VIEW 논리이름` 이름으로 사용하여, 일치히는 VIEW 반환");
  }
}
