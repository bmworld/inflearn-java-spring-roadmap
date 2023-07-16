package com.example.servlet.web.frontController.v4;


import com.example.servlet.web.frontController.MyView;
import com.example.servlet.web.frontController.v4.controller.MemberFormControllerV4;
import com.example.servlet.web.frontController.v4.controller.MemberListControllerV4;
import com.example.servlet.web.frontController.v4.controller.MemberSaveControllerV4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // FrontController -> URL 매핑정보에서 Controller 조회 -> Controller 호출 -> 해당 Controller 에서 JSP 호출
        String requestURI = req.getRequestURI();
        ControllerV4 controller = controllerMap.get(requestURI);
        if (controller == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND); // URL에 매핑되는 Controller 없을 경우
            return;
        }

        // paramMap
        Map<String, String> paramMap = createParamMap(req);
        Map<String, Object> model = new HashMap<>(); // 이번 버전에서 신규 추가된 것


        String viewName = controller.process(paramMap, model);

        MyView view = viewResolver(viewName);
        view.render(model, req, res);

    }

    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private static Map<String, String> createParamMap(HttpServletRequest req) {
        Map<String, String> paramMap = new HashMap<String, String>();
        req.getParameterNames().asIterator().forEachRemaining(paramName -> paramMap.put(paramName, req.getParameter(paramName)));
        return paramMap;
    }
}
