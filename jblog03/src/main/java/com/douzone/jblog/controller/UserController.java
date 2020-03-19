package com.douzone.jblog.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.douzone.jblog.service.UserService;
import com.douzone.jblog.vo.UserVo;

@Controller
@RequestMapping("/user")
public class UserController {
    
	@Autowired
	private UserService userService;
	
	@RequestMapping("/join")
	public String join() {
		
		return "user/join";
	}
	@RequestMapping(value="/auth",method= RequestMethod.POST)
	public void auth() {
		
	}
	@RequestMapping(value ="/join",method= RequestMethod.POST)
	public String join(UserVo vo) {
		userService.join(vo);
		return "user/joinsuccess";
	}
	
	@RequestMapping("/login")
	public String login() {
		
		return "user/login";
	}
	
	

}
