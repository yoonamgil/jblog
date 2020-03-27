package com.douzone.jblog.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.CategoryVo;

@Repository
public class CategoryRepository {
	
	@Autowired
	private SqlSession sqlSession;

	public List<CategoryVo> findList(String id) {
		return sqlSession.selectList("category.findList",id);
	}

	public void addCategory(CategoryVo vo) {
		
		sqlSession.insert("category.addCategory",vo);
	}

	public void deleteCategory(Long no) {
		sqlSession.delete("category.deleteCategory",no);
		
	}

	public Long findMin(String id) {
		
		return sqlSession.selectOne("category.findMin",id);
	}
}
