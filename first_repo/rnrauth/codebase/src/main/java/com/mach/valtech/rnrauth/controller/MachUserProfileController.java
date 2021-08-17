package com.mach.valtech.rnrauth.controller;


import com.mach.valtech.rnrauth.jpa.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mach.valtech.rnrauth.service.MachCustomerService;

@RestController
//@RequestMapping("/api/users")
public class MachUserProfileController {

    @Autowired
    private MachCustomerService customerService;

    @GetMapping(value = "/api/users/user/{id}",produces = "application/json")
    public Customer getUserDetail(@PathVariable Long id){
        return customerService.findById(id);
    }
}