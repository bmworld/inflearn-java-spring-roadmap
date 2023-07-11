package com.example.servlet.web.frontController.v2;

import com.example.servlet.web.frontController.MyView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ControllerV2 {

    MyView process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException;
}
