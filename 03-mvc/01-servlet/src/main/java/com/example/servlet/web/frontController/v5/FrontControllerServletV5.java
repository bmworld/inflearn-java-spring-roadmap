package com.example.servlet.web.frontController.v5;

import com.example.servlet.web.frontController.ModelView;
import com.example.servlet.web.frontController.MyView;
import com.example.servlet.web.frontController.v3.controller.MemberFormControllerV3;
import com.example.servlet.web.frontController.v3.controller.MemberListControllerV3;
import com.example.servlet.web.frontController.v3.controller.MemberSaveControllerV3;
import com.example.servlet.web.frontController.v4.controller.MemberFormControllerV4;
import com.example.servlet.web.frontController.v4.controller.MemberListControllerV4;
import com.example.servlet.web.frontController.v4.controller.MemberSaveControllerV4;
import com.example.servlet.web.frontController.v5.adapter.ControllerV3HandlerAdapter;
import com.example.servlet.web.frontController.v5.adapter.ControllerV4HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

  private final Map<String, Object> handlerMappingMap = new HashMap<>();
  private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

  public FrontControllerServletV5() {
    // 초기화 시, 의미가 있는 Method 이름 부여.
    initHandlerMappingMap();
    initHandlerAdapters();
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    Object handler = getHandler(req);

    if (handler == null) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND); // URL에 매핑되는 Controller 없을 경우
      return;
    }


    MyHandlerAdapter adapter = getHandlerAdapter(handler);

    ModelView mv = adapter.handle(req, res, handler);

    String viewName = mv.getViewName();

    MyView view = viewResolver(viewName);
    view.render(mv.getModel(), req, res);
  }

  private MyHandlerAdapter getHandlerAdapter(Object handler) {
    for (MyHandlerAdapter adt : handlerAdapters) {
      if (adt.support(handler)) {
        return adt;
      }
    }

    throw new IllegalStateException("Handler adapter not found! handlers=" + handler);
  }

  private Object getHandler(HttpServletRequest req) {
    String requestURI = req.getRequestURI();
    Object handler = handlerMappingMap.get(requestURI);
    return handler;
  }

  private void initHandlerAdapters() {
    handlerAdapters.add(new ControllerV3HandlerAdapter());
    handlerAdapters.add(new ControllerV4HandlerAdapter());
  }

  private void initHandlerMappingMap() {

    // V3
    handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3()); // 추후 v4와 구분하기 위해 URL이 이와같이 설정되었다.
    handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3()); // 추후 v4와 구분하기 위해 URL이 이와같이 설정되었다.
    handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3()); // 추후 v4와 구분하기 위해 URL이 이와같이 설정되었다.


    // V4
    handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4()); // 추후 v4와 구분하기 위해 URL이 이와같이 설정되었다.
    handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4()); // 추후 v4와 구분하기 위해 URL이 이와같이 설정되었다.
    handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4()); // 추후 v4와 구분하기 위해 URL이 이와같이 설정되었다.
  }

  private MyView viewResolver(String viewName) {
    return new MyView("/WEB-INF/views/" + viewName + ".jsp");
  }

}
