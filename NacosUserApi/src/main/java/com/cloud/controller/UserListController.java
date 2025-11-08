package com.cloud.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserListController {

    @RequestMapping("searchList")
    public String searchList() {
        return "用户列表";
    }
}
