package com.mach.valtech.rnrauth.controller;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import com.mach.valtech.rnrauth.service.MachCustomerService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class MachTokenController {

    @Autowired
    private MachCustomerService customerService;

    @PostMapping("/auth/token")
    public String getToken(@RequestParam("username") final String username, @RequestParam("password") final String password) {
        String token = customerService.login(username, password);
        if (StringUtils.isEmpty(token)) {
            return "no token found";
        }
        return token;
    }

    @GetMapping("/auth/token")
    public boolean validateToken(HttpServletRequest request) throws Exception {
        String token = StringUtils.isNotEmpty(request.getHeader("token"))?request.getHeader("token"):"";
        boolean isValidationSuccess = customerService.validateToken(token);
        return isValidationSuccess;
    }

    @GetMapping("/logout")
    public String fetchSignoutSite(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }
}
