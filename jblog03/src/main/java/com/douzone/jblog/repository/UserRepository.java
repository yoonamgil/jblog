package com.douzone.jblog.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.UserVo;

@Repository
public class UserRepository {
	
	@Autowired
	private SqlSession sqlSession;
	
	public void insert(UserVo vo) {
		sqlSession.insert("user.insertuser",vo);
		sqlSession.insert("user.insertblog",vo);
		sqlSession.insert("user.insertcategory",vo);
		
	}

	public UserVo findByIdAndPassword(UserVo vo) {
		
		return sqlSession.selectOne("user.findByIdAndPassword", vo);
	}

}
