package com.douzone.jblog.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.BlogVo;

@Repository
public class BlogRepository {
	@Autowired
	private SqlSession sqlSession;

	public BlogVo getBlog(String id) {
		
		return sqlSession.selectOne("blog.getBlog",id);
	}

	public void updateBlog(BlogVo blogVo) {
		
		sqlSession.update("blog.updateBlog",blogVo);
		
	}
}
