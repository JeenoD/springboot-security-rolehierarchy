package com.jeeno.springbootsecurityrolehierarchy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 用户相关的控制层接口
 * @author 杜家浩
 * @version 2.1.1
 * @date 2019/12/31 11:22
 */
@Slf4j
@RestController
@PreAuthorize(value = "isAuthenticated()")
public class UserController {

    @GetMapping({"", "/index"})
    public String index() {
        return "用户通用首页";
    }

    /**
     * 返回当前登录用户的信息
     * @param principal 用户信息
     * @return Principal
     */
    @GetMapping("/me")
    public Principal me(@AuthenticationPrincipal Principal principal) {
        return principal;
    }

    /**
     * 超级管理员才能使用的接口
     * @return String
     */
    @GetMapping("/admin")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public String adminOpt() {
        return "超管专属接口";
    }

    /**
     * 普通用户使用的接口（非角色继承方式下）
     * @return String
     */
    @GetMapping("/user")
    @PreAuthorize(value = "hasRole('USER')")
    public String userOpt() {
        return "普通用户接口";
    }

    /**
     * 管理员用户使用的接口（非角色继承方式下）
     * @return String
     */
    @GetMapping("/manager")
    @PreAuthorize(value = "hasRole('MANAGER')")
    public String managerOpt() {
        return "管理员用户接口";
    }
}
