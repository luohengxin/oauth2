package com.cn.client.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/order2")
public class OrderController2 {

    @RequestMapping("/{id}")
    @PreAuthorize("hasAuthority('orderById2')")
    public String findById(@PathVariable("id") String id){
        System.out.println(id);
        return id;
    }

    @RequestMapping("/2/{id}")
    @PreAuthorize("hasAuthority('orderById')")
    public String findById2(@PathVariable("id") String id){
        System.out.println(id);
        return id;
    }

    @RequestMapping("/3/{id}")
    public String findById3(@PathVariable("id") String id){
        System.out.println(id);
        return id;
    }

}
