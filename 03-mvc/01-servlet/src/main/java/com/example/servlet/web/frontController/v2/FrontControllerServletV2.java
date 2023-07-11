package com.example.servlet.web.frontController.v2;

import com.example.servlet.web.frontController.MyView;
import com.example.servlet.web.frontController.v1.ControllerV1;
import com.example.servlet.web.frontController.v2.controller.MemberFormControllerV2;
import com.example.servlet.web.frontController.v2.controller.MemberListControllerV2;
import com.example.servlet.web.frontController.v2.controller.MemberSaveControllerV2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "frontControllerServletV2", urlPatterns = "/front-controller/v2/*")
public class FrontControllerServletV2 extends HttpServlet {

    private Map<String, ControllerV2> controllerMap = new HashMap<>();

    public FrontControllerServletV2() {
        controllerMap.put("/front-controller/v2/members/new-form", new MemberFormControllerV2());
        controllerMap.put("/front-controller/v2/members/save", new MemberSaveControllerV2());
        controllerMap.put("/front-controller/v2/members", new MemberListControllerV2());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("FrontControllerServletV2.service");

        // FrontController -> URL 매핑정보에서 Controller 조회 -> Controller 호출 -> 해당 Controller 에서 JSP 호출
        String requestURI = req.getRequestURI();
        ControllerV2 controller = controllerMap.get(requestURI);
        if (controller == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND); // URL에 매핑되는 Controller 없을 경우
            return;
        }

        MyView view = controller.process(req, res);
        view.render(req,res);
    }
}
