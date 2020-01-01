package com.jeeno.springbootsecurityrolehierarchy.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.annotation.Resource;

/**
 * security 核心配置类
 * @author 杜家浩
 * @version 2.1.1
 * @date 2019/12/31 11:16
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AccessDeniedHandler accessDeniedHandler;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 初始化两个内存用户
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder)
                .withUser("admin")
                .password(passwordEncoder.encode("123456"))
                .roles("ADMIN")
                .and()
                .withUser("manager")
                .password(passwordEncoder.encode("123456"))
                .roles("MANAGER")
                .and()
                .withUser("user")
                .password(passwordEncoder.encode("123456"))
                .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 表单登录
        http.formLogin();

        http.authorizeRequests().anyRequest().authenticated();

        // 无权限时的逻辑处理
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    /**
     * 配置用户角色继承方式
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        // TODO 需要注意这里的格式 '>'前后的空格必须要有
//        // ADMIN 继承 USER
//        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER ");
        // ADMIN 继承 MANAGER, MANAGER 继承 USER
            // 第一种写法
            roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_USER");
//            // 第二种写法
//            roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MANAGER\n ROLE_MANAGER > ROLE_USER");
//        // ADMIN 继承 MANAGER; ADMIN 继承 USER
//        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER\n ROLE_ADMIN > ROLE_MANAGER");
        return roleHierarchy;
    }

}
