package com.douzone.jblog.repository;

import java.util.List;

import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.PostVo;

@Repository
public class PostRepository {
	@Autowired
	private SqlSession sqlSession;

	public void addPost(PostVo postVo) {
		sqlSession.insert("post.addPost",postVo);
		
	}

	public List<PostVo> findList(Long min) {
		
		return sqlSession.selectList("post.findList",min);
	}

	public Long findMin(Long min) {
		
		return sqlSession.selectOne("post.findMin",min);
	}

	public PostVo findPost(Long min) {
		
		return sqlSession.selectOne("post.findPost",min);
	}
	
	
}
