package com.douzone.jblog.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.jblog.service.BlogService;
import com.douzone.jblog.service.UserService;
import com.douzone.jblog.vo.BlogVo;
import com.douzone.jblog.vo.CategoryVo;
import com.douzone.jblog.vo.PostVo;
import com.douzone.jblog.vo.UserVo;
import com.douzone.security.AuthUser;



@Controller
@RequestMapping( "/{id:(?!assets).*}" )
public class BlogContoroller {
	

	@Autowired
	private BlogService blogService;
	
	@Autowired
	private UserService userService;

	@RequestMapping( {"", "/{pathNo1}", "/{pathNo1}/{pathNo2}" } )
	public String blogMain(
			@PathVariable String id,
			@PathVariable Optional<Long> pathNo1,
			@PathVariable Optional<Long> pathNo2,
			ModelMap modelMap,UserVo userVo
			) {
		
		userVo=userService.getUserById(id);
		
		if(userVo==null) {
			
			return "error/404";
		}
		
		Long categoryNo = 0L;
		Long postNo = 0L;
		BlogVo blogVo= new BlogVo();
		blogVo=blogService.getBlog(id);
		
		if( pathNo2.isPresent() ) {
			postNo = pathNo2.get();
			categoryNo = pathNo1.get();
		} else if( pathNo1.isPresent() ){
			categoryNo = pathNo1.get();
		}
		
		modelMap.putAll( blogService.getAll( id, categoryNo, postNo ) );
		modelMap.addAttribute("blogVo",blogVo);
		return "blog/blog-main";
	}
	
	@RequestMapping("/admin-basic")
	public String blogAdminBasic(@PathVariable String id, Model model,@AuthUser UserVo authUser) {
		if(authUser==null) {
			return "redirect:/"+id;
		}
		
		if(!(id.equals(authUser.getId()))) {
			return "redirect:/"+id;
		}
		
		
		BlogVo blogVo = blogService.getBlog(id);
		model.addAttribute("blogVo",blogVo);
		return "blog/blog-admin-basic";
	}
	
	@RequestMapping(value = "/admin-basic",method=RequestMethod.POST)
	public String blogAdminBasic(
			@RequestParam (value="title", required= true)String title,
			@RequestParam (value="file",required= true)MultipartFile file,
			@PathVariable String id,@AuthUser UserVo authUser
			) {
		if(authUser==null) {
			return "redirect:/"+id;
		}
		if(!(id.equals(authUser.getId()))) {
			return "redirect:/"+id;
		}
			
			String url = blogService.restore(file);
			BlogVo blogVo=new BlogVo();
			blogVo.setTitle(title);
			blogVo.setBlogId(id);
			if(file.isEmpty()) {
				BlogVo newBlogVo= new BlogVo();
				newBlogVo=blogService.getBlog(id);
				blogVo.setLogo(newBlogVo.getLogo());
			}else {
				
				blogVo.setLogo(url);
			}
			
			blogService.updateBlog(blogVo);
		
		return "redirect:/"+id;
	}
	
	@RequestMapping("/admin-category")
	public String blogAdminCategory(@PathVariable String id,
					Model model, @AuthUser UserVo authUser) {
		
		
		if(authUser==null) {
			return "redirect:/"+id;
		}
		if(!(id.equals(authUser.getId()))) {
			return "redirect:/"+id;
		}
		
		Map<String,Object> map =blogService.getList(id);
		model.addAllAttributes(map);
		return "blog/blog-admin-category";
	}
	
	@RequestMapping(value="/admin-category",method = RequestMethod.POST)
	public String blogAdminCategory(@PathVariable String id, CategoryVo categoryVo,@AuthUser UserVo authUser
					) {
		
		if(authUser==null) {
			return "redirect:/"+id;
		}
		if(!(id.equals(authUser.getId()))) {
			return "redirect:/"+id;
		}
			
		 categoryVo.setBlogId(id);
		 blogService.addCategory(categoryVo);
		 
		 
		return "redirect:/"+id+"/admin-category";
	}
	
	@RequestMapping(value="/admin-category/delete/{no}")
	public String blogAdminCategoryDelete(@PathVariable String id, CategoryVo categoryVo,
				@PathVariable Long no, @AuthUser UserVo authUser,Model model
					) {
		if(authUser==null) {
			return "redirect:/"+id;
		}
		if(!(id.equals(authUser.getId()))) {
			return "redirect:/"+id;
		}
		
			System.out.println(no);
		blogService.deleteCategory(no);
		model.addAttribute("id",id);
		return "redirect:/"+id+"/admin-category";
	}
	
	@RequestMapping("/admin-write")
	public String blogAdminWrite(@PathVariable String id,Model model,@AuthUser UserVo authUser) {
		
		if(authUser==null) {
			return "redirect:/"+id;
		}
		if(!id.equals(authUser.getId())) {
			return "redirect:/"+id;
		}
		
		
		Map<String,Object> map =blogService.getList(id);
		model.addAllAttributes(map);
		
		
		return "blog/blog-admin-write";
	}
	
	@RequestMapping(value="/admin-write",method = RequestMethod.POST)
	public String blogAdminWrite(@PathVariable String id,PostVo postVo,@AuthUser UserVo authUser) {
		if(!id.equals(authUser.getId())|| authUser==null) {
			return "redirect:/"+id;
		}
		
		
		blogService.addPost(postVo);
		
		return "redirect:/"+id;
	}
	
}
