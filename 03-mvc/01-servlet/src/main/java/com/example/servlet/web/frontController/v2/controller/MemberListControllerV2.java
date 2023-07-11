package com.example.servlet.web.frontController.v2.controller;

import com.example.servlet.basic.repository.MemberRepository;
import com.example.servlet.domain.member.Member;
import com.example.servlet.web.frontController.MyView;
import com.example.servlet.web.frontController.v1.ControllerV1;
import com.example.servlet.web.frontController.v2.ControllerV2;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MemberListControllerV2 implements ControllerV2 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public MyView process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        List<Member> members = memberRepository.findAll();
        req.setAttribute("members", members);

        String viewPath = "/WEB-INF/views/members.jsp";
        return new MyView(viewPath);


    }
}
