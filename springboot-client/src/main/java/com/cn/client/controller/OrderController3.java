package com.cn.client.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/order3")
public class OrderController3 {

    @RequestMapping("/{id}")
    public String findById(@PathVariable("id") String id){
        System.out.println(id);
        return id;
    }

    @RequestMapping("/2/{id}")
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
