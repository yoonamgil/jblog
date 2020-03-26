package com.douzone.jblog.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.jblog.repository.BlogRepository;
import com.douzone.jblog.repository.CategoryRepository;
import com.douzone.jblog.repository.PostRepository;
import com.douzone.jblog.vo.BlogVo;
import com.douzone.jblog.vo.CategoryVo;
import com.douzone.jblog.vo.PostVo;

@Service
public class BlogService {

	private static final String SAVE_PATH = "/jblog-uploads";
	private static final String URL = "/image";
	@Autowired
	private BlogRepository blogRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private PostRepository postRepository;

	public BlogVo getBlog(String id) {

		return blogRepository.getBlog(id);
	}

	public String restore(MultipartFile multipartFile) {
		String url = "";

		try {
			if (multipartFile.isEmpty()) {
				return url;
			}

			String originFilename = multipartFile.getOriginalFilename();

			String extName = originFilename.substring(originFilename.lastIndexOf('.') + 1);

			String saveFilename = generatesaveFilename(extName);
			long fileSize = multipartFile.getSize();

			System.out.println("##################" + originFilename);
			System.out.println("##################" + saveFilename);
			System.out.println("##################" + fileSize);

			byte[] fileData = multipartFile.getBytes();
			OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFilename);
			os.write(fileData);
			os.close();
			url = URL + "/" + saveFilename;

		} catch (IOException e) {
			throw new RuntimeException("file upload error:" + e);
		}
		return url;
	}

	private String generatesaveFilename(String extName) {
		String fileName = "";
		Calendar calendar = Calendar.getInstance();
		fileName += calendar.get(Calendar.YEAR);
		fileName += calendar.get(Calendar.MONTH);
		fileName += calendar.get(Calendar.DATE);
		fileName += calendar.get(Calendar.HOUR);
		fileName += calendar.get(Calendar.MINUTE);
		fileName += calendar.get(Calendar.SECOND);
		fileName += calendar.get(Calendar.MILLISECOND);
		fileName += ("." + extName);

		return fileName;
	}

	public void updateBlog(BlogVo blogVo) {
		blogRepository.updateBlog(blogVo);
		
	}

	public Map<String, Object> getList(String id) {
		Map<String, Object> map = new HashMap<>();
		List<CategoryVo> list = categoryRepository.findList(id);
		Long min=categoryRepository.findMin(id);
		BlogVo vo=blogRepository.getBlog(id);
		
		map.put("list", list);
		map.put("listSize", list.size());
		map.put("blogVo", vo);
		map.put("min", min);
	
		
		return map;
	}

	public void addCategory(CategoryVo vo) {
		categoryRepository.addCategory(vo);
	}

	public void deleteCategory(Long no) {
		categoryRepository.deleteCategory(no);
	}

	public void addPost(PostVo postVo) {
		postRepository.addPost(postVo);
	}

	public Map<String, Object> getAll(String id, Long categoryNo, Long postNo) {
		Map<String, Object> map = new HashMap<>();
		List<CategoryVo> categoryList = null;
		List<PostVo> postList = null;
		PostVo postVo= new PostVo();
		Long min;
		
		categoryList=categoryRepository.findList(id);
		if((postNo != 0L) && (categoryNo != 0L)) {
			postList=postRepository.findList(categoryNo);
			postVo = postRepository.findPost(postNo);	
			
		}else if((categoryNo!= 0L) && (postNo== 0L)) {
			
			postList=postRepository.findList(categoryNo);
			min=postRepository.findMin(categoryNo);
			postVo = postRepository.findPost(min);	
		}else {
			
			min=categoryRepository.findMin(id);
			postList=postRepository.findList(min);
			
			min=postRepository.findMin(min);
			postVo = postRepository.findPost(min);	
		}
		
		
		
		map.put("categoryList", categoryList);
		map.put("postList", postList);
		map.put("postVo", postVo);
		
		
		return map;
	}
}
