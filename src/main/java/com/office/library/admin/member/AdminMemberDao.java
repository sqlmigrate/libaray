package com.office.library.admin.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminMemberDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    // 관리자 아이디가 있는지 확인
    public boolean isAdminMember(String a_m_id) {
        String sql = "SELECT COUNT(*) FROM tbl_admin_member WHERE a_m_id = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, a_m_id);
        if (result > 0){
            return true;
        }else{
           return false;
        }
    }

    // 회원가입 확인
    public int insertAdminMember(AdminMemberVo adminMemberVo) {
        List<String> args = new ArrayList<String>();

        String sql = "INSERT INTO tbl_admin_member(";
            if (adminMemberVo.getA_m_id().equals("super admin")){
                sql += "a_m_approval, ";
                args.add("1");
            }
            sql += "a_m_id, ";
            args.add(adminMemberVo.getA_m_id());

            sql += "a_m_pw, ";
            //args.add(adminMemberVo.getA_m_pw());
            args.add(passwordEncoder.encode(adminMemberVo.getA_m_pw()));

            sql += "a_m_name, ";
            args.add(adminMemberVo.getA_m_name());

            sql += "a_m_gender, ";
            args.add(adminMemberVo.getA_m_gender());

            sql += "a_m_part, ";
            args.add(adminMemberVo.getA_m_part());

            sql += "a_m_position, ";
            args.add(adminMemberVo.getA_m_position());

            sql += "a_m_mail, ";
            args.add(adminMemberVo.getA_m_mail());

            sql += "a_m_phone, ";
            args.add(adminMemberVo.getA_m_phone());

            sql += "a_m_reg_date, a_m_mod_date)";

            if (adminMemberVo.getA_m_id().equals("super admin")) {
                sql += " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
            } else{
                sql += " VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
            }
        int result = -1;

        try {
            result = jdbcTemplate.update(sql, args.toArray());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public AdminMemberVo selectAdmin(AdminMemberVo adminMemberVo) {
        String sql = "SELECT * FROM tbl_admin_member WHERE a_m_id = ? AND a_m_approval > 0 LIMIT 1";
        // 결과를 Map으로 받아오기
        Map<String, Object> resultMap = jdbcTemplate.queryForMap(sql, adminMemberVo.getA_m_id());

        //Map에서 AdminMemberVo 객체 생성
        AdminMemberVo existAdminMemberVo = new AdminMemberVo();
        existAdminMemberVo.setA_m_no((Integer) resultMap.get("a_m_no"));
        existAdminMemberVo.setA_m_approval((Integer) resultMap.get("a_m_approval"));
        existAdminMemberVo.setA_m_id((String) resultMap.get("a_m_id"));
        existAdminMemberVo.setA_m_pw((String) resultMap.get("a_m_pw"));
        existAdminMemberVo.setA_m_name((String) resultMap.get("a_m_name"));
        existAdminMemberVo.setA_m_gender((String) resultMap.get("a_m_gender"));
        existAdminMemberVo.setA_m_part((String) resultMap.get("a_m_part"));
        existAdminMemberVo.setA_m_position((String) resultMap.get("a_m_position"));
        existAdminMemberVo.setA_m_mail((String) resultMap.get("a_m_mail"));
        existAdminMemberVo.setA_m_phone((String) resultMap.get("a_m_phone"));
        existAdminMemberVo.setA_m_reg_date(resultMap.get("a_m_reg_date").toString());
        existAdminMemberVo.setA_m_mod_date(resultMap.get("a_m_mod_date").toString());

        // 비밀번호 일치 여부 확인
        if (existAdminMemberVo.getA_m_pw() != null && 
        passwordEncoder.matches(adminMemberVo.getA_m_pw(), existAdminMemberVo.getA_m_pw())) {
                return existAdminMemberVo;
        }
       return null;

       /*
       String sql = "SELECT * FROM tbl_admin_member WHERE a_m_id = ? AND a_m_approval > 0";
       List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>();
        try {
            adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
                @Override
                public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
                    AdminMemberVo adminMemberVo = new AdminMemberVo();
                    adminMemberVo.setA_m_id(rs.getString("a_m_id"));

                    return adminMemberVo;
                }
            }, adminMemberVo.getA_m_id());

            if (!passwordEncoder.matches(adminMemberVo.getA_m_pw(),
                adminMemberVos.get(0).getA_m_pw()))
                adminMemberVos.clear();
        } catch (Exception e){
            e.printStackTrace();
        }
        return adminMemberVos.size() > 0 ? adminMemberVos.get(0) : null;
        */
    }

    public List<AdminMemberVo> listupAdmin() {
        String sql = "SELECT * FROM tbl_admin_member";
        List<AdminMemberVo> adminMemberVos = new ArrayList<>();

        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);

        resultList.forEach(resultMap -> {
                AdminMemberVo adminMemberVo = new AdminMemberVo();
                if (resultMap.containsKey("a_m_no")) {
                    adminMemberVo.setA_m_no((Integer) resultMap.get("a_m_no"));
                }
                if (resultMap.containsKey("a_m_approval")) {
                    adminMemberVo.setA_m_approval((Integer) resultMap.get("a_m_approval"));
                }
                if (resultMap.containsKey("a_m_id")){
                   adminMemberVo.setA_m_id(resultMap.get("a_m_id").toString());
                }
                if (resultMap.containsKey("a_m_pw")) {
                   adminMemberVo.setA_m_pw(resultMap.get("a_m_pw").toString());
                }
                if (resultMap.containsKey("a_m_name")) {
                   adminMemberVo.setA_m_name(resultMap.get("a_m_name").toString());
                }
                if (resultMap.containsKey("a_m_gender")) {
                   adminMemberVo.setA_m_gender(resultMap.get("a_m_gender").toString());
                }
                if (resultMap.containsKey("a_m_part")) {
                   adminMemberVo.setA_m_part(resultMap.get("a_m_part").toString());
                }
                if (resultMap.containsKey("a_m_position")) {
                   adminMemberVo.setA_m_position(resultMap.get("a_m_position").toString());
                }
                if (resultMap.containsKey("a_m_mail")) {
                   adminMemberVo.setA_m_mail(resultMap.get("a_m_mail").toString());
                }
                if (resultMap.containsKey("a_m_phone")) {
                   adminMemberVo.setA_m_phone(resultMap.get("a_m_phone").toString());
                }
                if (resultMap.containsKey("a_m_reg_date")) {
                   adminMemberVo.setA_m_reg_date(resultMap.get("a_m_reg_date").toString());
                }
                if (resultMap.containsKey("a_m_mod_date")) {
                   adminMemberVo.setA_m_mod_date(resultMap.get("a_m_mod_date").toString());
                }
                   adminMemberVos.add(adminMemberVo);
            });
            return adminMemberVos;
        }

        // 관리자 승인
        public int updateAdminAccount(int a_m_no) {
            String sql = "UPDATE tbl_admin_member SET a_m_approval = 1 WHERE a_m_no = ?";

            int result = -1;

            try {
                result = jdbcTemplate.update(sql, a_m_no);
            } catch (Exception e) {
                   e.printStackTrace();
            }
            return result;
        }

        public int updateAdminAccount(AdminMemberVo adminMemberVo) {
            String sql = "UPDATE tbl_admin_member SET a_m_name = ?, a_m_gender = ?, a_m_part = ?, a_m_position = ?, a_m_mail = ?, a_m_phone = ?, a_m_mod_date = NOW() WHERE a_m_no = ?";
            int result = -1;

            try {
                result = jdbcTemplate.update(sql, adminMemberVo.getA_m_name(), adminMemberVo.getA_m_gender(), adminMemberVo.getA_m_part(), adminMemberVo.getA_m_position(), adminMemberVo.getA_m_mail(), adminMemberVo.getA_m_phone(), adminMemberVo.getA_m_no());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        public AdminMemberVo selectAdmin(int a_m_no) {
            String sql = "SELECT * FROM tbl_admin_member WHERE a_m_no = ?";
            AdminMemberVo adminMemberVo = new AdminMemberVo();
        
            try {
                Map<String, Object> resultMap = jdbcTemplate.queryForMap(sql, a_m_no);
        
                adminMemberVo.setA_m_no((Integer) resultMap.get("a_m_no"));
                adminMemberVo.setA_m_approval((Integer) resultMap.get("a_m_approval"));
                adminMemberVo.setA_m_id((String) resultMap.get("a_m_id"));
                adminMemberVo.setA_m_pw((String) resultMap.get("a_m_pw"));
                adminMemberVo.setA_m_name((String) resultMap.get("a_m_name"));
                adminMemberVo.setA_m_gender((String) resultMap.get("a_m_gender"));
                adminMemberVo.setA_m_part((String) resultMap.get("a_m_part"));
                adminMemberVo.setA_m_position((String) resultMap.get("a_m_position"));
                adminMemberVo.setA_m_mail((String) resultMap.get("a_m_mail"));
                adminMemberVo.setA_m_phone((String) resultMap.get("a_m_phone"));
                adminMemberVo.setA_m_reg_date(resultMap.get("a_m_reg_date").toString());
                adminMemberVo.setA_m_mod_date(resultMap.get("a_m_mod_date").toString());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        
            return adminMemberVo;
        }
    }
