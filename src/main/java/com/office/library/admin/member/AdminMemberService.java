package com.office.library.admin.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminMemberService {

    final static public int ADMIN_ACCOUNT_ALREADY_EXIST = 0;
    final static public int ADMIN_ACCOUNT_CREATE_SUCCESS = 1;
    final static public int ADMIN_ACCOUNT_CREATE_FAIL = -1;

    @Autowired
    AdminMemberDao adminMemberDao;

    // 회원가입 확인
    public int createAccountConfirm(AdminMemberVo adminMemberVo){
        

        //관리자 아이디가 있는지 확인
        boolean isMember = adminMemberDao.isAdminMember(adminMemberVo.getA_m_id());

        //관리자 아이디가 없는 경우
        if (!isMember){
            int result = adminMemberDao.insertAdminMember(adminMemberVo);

            //관리자 등록 성공
            if (result > 0){
                return ADMIN_ACCOUNT_CREATE_SUCCESS;
            }
            //관리자 등록 실패
            else{
                return ADMIN_ACCOUNT_CREATE_FAIL;
            }
        } 
        //관리자 아이디가 있는 경우
        else {
            return ADMIN_ACCOUNT_ALREADY_EXIST;
        }
    }

    // 로그인 확인
    public AdminMemberVo loginConfirm(AdminMemberVo adminMemberVo) {
        AdminMemberVo loginedAdminMemberVo = adminMemberDao.selectAdmin(adminMemberVo);
        return loginedAdminMemberVo;
    }

    // 관리자 목록
    public List<AdminMemberVo> listupAdmin() {
        return adminMemberDao.listupAdmin();
    }
    
    // 관리자 승인
    public void setAdminApproval(int a_m_no) {
        int result = adminMemberDao.updateAdminAccount(a_m_no);
    }
    
    // 회원정보 수정
    public int modifyAccountConfirm(AdminMemberVo adminMemberVo) {
        return adminMemberDao.updateAdminAccount(adminMemberVo);
    }

    public AdminMemberVo getLoginedAdminMemberVo(int a_m_no) {
        return adminMemberDao.selectAdmin(a_m_no);
    }
}
