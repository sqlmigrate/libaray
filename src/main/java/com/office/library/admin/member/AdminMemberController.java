package com.office.library.admin.member;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/admin/member")
public class AdminMemberController {

    @Autowired
    AdminMemberService adminMemberService;

    // 회원가입, http://localhost:8080/admin/member/createAccountForm
    @GetMapping("/createAccountForm")
    public String createAccountForm() {
        String nextPage = "admin/member/create_account_form";
        return nextPage;
    }

    // 회원가입 확인
    @PostMapping("/createAccountConfirm")
    public String createAccount(AdminMemberVo adminMemberVo) {
        String nextPage= "admin/member/create_account_ok";
        int result = adminMemberService.createAccountConfirm(adminMemberVo);
        if(result <= 0) {
            nextPage = "admin/member/create_account_ng";
        }
        return nextPage;
    }

    // 로그인
    @GetMapping("/loginForm")
    public String loginForm() {
        String nextPage = "admin/member/login_form";
        return nextPage;
    }

    // 로그인 확인
    @PostMapping("/loginConfirm")
    public String loginConfirm(AdminMemberVo adminMemberVo, HttpSession session) {
        String nextPage = "admin/member/login_ok";

        AdminMemberVo loginedAdminMemberVo = adminMemberService.loginConfirm(adminMemberVo);

        if(loginedAdminMemberVo == null) {
            nextPage = "admin/member/login_ng";
        } else {
            session.setAttribute("loginedAdminMemberVo", loginedAdminMemberVo);
            session.setMaxInactiveInterval(60 * 30);
        }
        return nextPage;
    }

    // 로그아웃
    @RequestMapping(value = "/logoutConfirm", method = RequestMethod.GET)
    public String logoutConfirm(HttpSession session) {
        session.invalidate();
        String nextPage = "redirect:/admin";
        return nextPage;
    }

    // 관리자 목록
    @RequestMapping(value = "/listupAdmin", method = RequestMethod.GET)
    public String listupAdmin(Model model) {
        String nextPage = "admin/member/listup_admins";
        List<AdminMemberVo> adminMemberVos = adminMemberService.listupAdmin();
        model.addAttribute("adminMemberVos", adminMemberVos);
        return nextPage;
    }

    // 관리자 승인
    @RequestMapping(value = "/setAdminApproval", method = RequestMethod.GET)
    public String setAdminApproval(@RequestParam("a_m_no") int a_m_no) {
        adminMemberService.setAdminApproval(a_m_no);
        String nextPage = "redirect:/admin/member/listupAdmin";
        return nextPage;
    }

    // 회원 정보 수정
    @GetMapping("/modifyAccountForm")
    public String modifyAccountForm(HttpSession session) {
        String nextPage = "admin/member/modify_account_form";
        
        AdminMemberVo loginedAdminMemberVo = (AdminMemberVo) session.getAttribute("loginedAdminMemberVo");
        if(loginedAdminMemberVo == null) {
            nextPage = "redirect:/admin/member/loginForm";
            return nextPage;
        }
        return nextPage;
    }

    // 회원 정보 수정 확인
    @PostMapping("/modifyAccountConfirm")
    public String modifyAccountConfirm(AdminMemberVo adminMemberVo, HttpSession session) {
        String nextPage = "admin/member/modify_account_ok";
        int result = adminMemberService.modifyAccountConfirm(adminMemberVo);

        if (result > 0) {
            AdminMemberVo loginedAdminMemberVo = adminMemberService.getLoginedAdminMemberVo(adminMemberVo.getA_m_no());
            session.setAttribute("loginedAdminMemberVo", loginedAdminMemberVo);
            session.setMaxInactiveInterval(60 * 30);
        } else {
            nextPage = "admin/member/modify_account_ng";
        }
        return nextPage;
    }
}
