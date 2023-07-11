package com.example.servlet.web.frontController.v1.controller;

import com.example.servlet.basic.repository.MemberRepository;
import com.example.servlet.domain.member.Member;
import com.example.servlet.web.frontController.v1.ControllerV1;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MemberListControllerV1 implements ControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public void process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {


        List<Member> members = memberRepository.findAll();


        // Model 내에 데이터 보관
        req.setAttribute("members", members);

        // View 호출
        String viewPath = "/WEB-INF/views/members.jsp";
        RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
        dispatcher.forward(req, res);

    }
}
