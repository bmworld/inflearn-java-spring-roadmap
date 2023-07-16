package com.example.servlet.web.frontController.v3;

import com.example.servlet.web.frontController.ModelView;
import com.example.servlet.web.frontController.MyView;
import com.example.servlet.web.frontController.v2.ControllerV2;
import com.example.servlet.web.frontController.v2.controller.MemberFormControllerV2;
import com.example.servlet.web.frontController.v2.controller.MemberListControllerV2;
import com.example.servlet.web.frontController.v2.controller.MemberSaveControllerV2;
import com.example.servlet.web.frontController.v3.controller.MemberFormControllerV3;
import com.example.servlet.web.frontController.v3.controller.MemberListControllerV3;
import com.example.servlet.web.frontController.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("FrontControllerServletV3.service");

        // FrontController -> URL 매핑정보에서 Controller 조회 -> Controller 호출 -> 해당 Controller 에서 JSP 호출
        String requestURI = req.getRequestURI();
        ControllerV3 controller = controllerMap.get(requestURI);
        if (controller == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND); // URL에 매핑되는 Controller 없을 경우
            return;
        }

        // paramMap
        Map<String, String> paramMap = createParamMap(req);
        ModelView mv = controller.process(paramMap);
        // view 의 논리이름을 물리이름으로 바꾼다.  (ex. name => /WEB-INF/view/name.jsp)
        String viewName = mv.getViewName();

        MyView view = viewResolver(viewName);
        view.render(mv.getModel(), req, res);

    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest req) {
        Map<String, String> paramMap = new HashMap<String, String>();
        req.getParameterNames().asIterator().forEachRemaining(paramName -> paramMap.put(paramName, req.getParameter(paramName)));
        return paramMap;
    }
}
