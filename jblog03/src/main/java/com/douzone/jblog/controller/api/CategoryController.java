package com.douzone.jblog.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.douzone.jblog.dto.JsonResult;
import com.douzone.jblog.service.BlogService;
import com.douzone.jblog.vo.CategoryVo;
import com.douzone.jblog.vo.UserVo;
import com.douzone.security.AuthUser;


@RestController("CategoryApiController")
@RequestMapping("/api/category")
public class CategoryController {
	
	@Autowired
	private BlogService blogService ;
	
	@GetMapping("/list/{id}")
	public JsonResult list(@PathVariable("id") String id) {
		 List<CategoryVo> list = blogService.getCategoryList(id);
		 System.out.println(list);
		
		return JsonResult.success(list);
	}
	
	
	@PostMapping("/add")
	public JsonResult add(@RequestBody CategoryVo vo,@AuthUser UserVo authUser) {
		
		vo.setBlogId(authUser.getId());
		blogService.addCategory(vo);
		
		return JsonResult.success(vo);
	}
	
	@DeleteMapping("/delete/{no}")
	public JsonResult delete(@PathVariable("no") Long no,
			@AuthUser UserVo authUser
		) {
		
		blogService.deleteCategory(no);
		List<CategoryVo> list = blogService.getCategoryList(authUser.getId());
		System.out.println(no);
		
		return JsonResult.success(list);
	}
	
	
	
}
