package com.example.servlet.web.frontController.v1;

import com.example.servlet.web.frontController.v1.controller.MemberFormControllerV1;
import com.example.servlet.web.frontController.v1.controller.MemberListControllerV1;
import com.example.servlet.web.frontController.v1.controller.MemberSaveControllerV1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "frontControllerServletV1", urlPatterns = "/front-controller/v1/*")
public class FrontControllerServletV1 extends HttpServlet {

    private Map<String, ControllerV1> controllerMap = new HashMap<>();

    public FrontControllerServletV1() {
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("FrontControllerServletV1.service");

        // FrontController -> URL 매핑정보에서 Controller 조회 -> Controller 호출 -> 해당 Controller 에서 JSP 호출
        String requestURI = req.getRequestURI();
        ControllerV1 controller = controllerMap.get(requestURI);
        if (controller == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND); // URL에 매핑되는 Controller 없을 경우
            return;
        }

        controller.process(req, res);
    }
}
