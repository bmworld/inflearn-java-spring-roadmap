package com.example.servlet.web.frontController.v2.controller;

import com.example.servlet.basic.repository.MemberRepository;
import com.example.servlet.domain.member.Member;
import com.example.servlet.web.frontController.MyView;
import com.example.servlet.web.frontController.v2.ControllerV2;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MemberSaveControllerV2 implements ControllerV2 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public MyView process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

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
        return new MyView(viewPath);
    }
}
