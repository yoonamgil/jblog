package com.douzone.jblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.jblog.vo.UserVo;
import com.douzone.jblog.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public void join(UserVo vo) {
		userRepository.insert(vo);
	}
	
	public UserVo getUser(UserVo vo) {
		return userRepository.findByIdAndPassword(vo);
	}

	
	
}
