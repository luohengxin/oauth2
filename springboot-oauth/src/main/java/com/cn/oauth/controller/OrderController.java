package com.cn.oauth.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/order")
public class OrderController {

    @PreAuthorize("hasAuthority('orderById')")
    @RequestMapping("/{id}")
    public String findById(@PathVariable("id") String id){
        System.out.println(id);
        return id;
    }

}
