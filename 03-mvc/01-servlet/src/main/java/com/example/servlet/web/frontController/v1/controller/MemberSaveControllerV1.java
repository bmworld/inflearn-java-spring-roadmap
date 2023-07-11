package com.example.servlet.web.frontController.v1.controller;

import com.example.servlet.basic.repository.MemberRepository;
import com.example.servlet.domain.member.Member;
import com.example.servlet.web.frontController.v1.ControllerV1;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MemberSaveControllerV1 implements ControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Member member = null;
        try {
            String username = req.getParameter("username");
            int age = Integer.parseInt(req.getParameter("age"));
            member = new Member(username, age);
            memberRepository.save(member);

        } catch (Exception e) {
            e.getStackTrace();
        }



        // Model 내에 데이터 보관
        req.setAttribute("member", member);

        // View 호출
        String viewPath = "/WEB-INF/views/save-result.jsp";
        RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
        dispatcher.forward(req, res);

    }
}
