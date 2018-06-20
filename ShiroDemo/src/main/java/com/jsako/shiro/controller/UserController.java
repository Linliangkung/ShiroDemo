package com.jsako.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsako.shiro.domain.User;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@RequestMapping("login")
	public String login(User user){
		String result="success";
		Subject subject = SecurityUtils.getSubject();
		if(!subject.isAuthenticated()){
			//登录逻辑
			UsernamePasswordToken token=new UsernamePasswordToken(user.getUsername(), user.getPassword());
			token.setRememberMe(true);
			try{
				subject.login(token);
			}catch(AuthenticationException e ){
				result=e.getMessage();
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@RequestMapping("delete")
	public String delete(){
		return "delete-success";
	}
	
	
}
