package com.example.servlet.web.frontController.v5;

import com.example.servlet.web.frontController.ModelView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface MyHandlerAdapter {
    boolean support(Object handler);

    ModelView handle(HttpServletRequest req, HttpServletResponse res, Object handler) throws ServletException, IOException;
}
