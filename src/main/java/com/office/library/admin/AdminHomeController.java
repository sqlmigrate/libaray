package com.office.library.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {
    
    // http://localhost:8080/admin or http://localhost:8080/admin/
    @RequestMapping(value = {"", "/"}, method=RequestMethod.GET)
    public String home() {
        String nextPage = "admin/home";  // => views/admin/home.jsp
        return nextPage;
    }
}
