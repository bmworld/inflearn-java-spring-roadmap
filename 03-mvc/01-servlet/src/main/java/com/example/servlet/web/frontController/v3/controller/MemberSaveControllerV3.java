package com.example.servlet.web.frontController.v3.controller;

import com.example.servlet.basic.repository.MemberRepository;
import com.example.servlet.domain.member.Member;
import com.example.servlet.web.frontController.ModelView;
import com.example.servlet.web.frontController.v3.ControllerV3;

import java.util.Map;

public class MemberSaveControllerV3 implements ControllerV3 {
    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        String name = paramMap.get("name");
        int age = Integer.parseInt(paramMap.get("age"));
        Member member = new Member(name, age);
        memberRepository.save(member);


        ModelView mv = new ModelView("save-result");
        mv.getModel().put("member", member);
        return mv;

    }
}
