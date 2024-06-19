package com.poscodx.jblog.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.poscodx.jblog.service.BlogService;
import com.poscodx.jblog.service.FileUploadService;
import com.poscodx.jblog.vo.BlogVo;
import com.poscodx.jblog.vo.CategoryVo;
import com.poscodx.jblog.vo.PostVo;

@Controller
@RequestMapping("/{id:(?!assets).*}")
public class BlogController {
	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private BlogService blogService;

	@RequestMapping({"", "/{categoryNo}", "/{categoryNo}/{postNo}" })
	public String index(
		@PathVariable("id") String blogId,
		@PathVariable("categoryNo") Optional<Long> categoryNo,
		@PathVariable("postNo") Optional<Long> postNo,
		@RequestParam(value="type", required=true, defaultValue="") String type,
		Model model) {

		BlogVo blogVo = blogService.getBlogAdmin(blogId);

		if(blogVo == null) { // 가입하지 않은 회원 에러 처리
			return "error/404";
		}

		List<CategoryVo> categoryList = blogService.getCategoryList(blogId);

		model.addAttribute("blogVo", blogVo);
		model.addAttribute("categoryList", categoryList);
		
		List<PostVo> postList = new ArrayList<>();
		Long postIdx = (long) 0;
		String typeStr = "";
		
		if (categoryNo.isEmpty() && postNo.isEmpty()) { // blogId
			postList = blogService.getPostList(blogId);
			typeStr = "total";
		} else if (categoryNo.isPresent() && postNo.isEmpty()) { // blogId, categoryNo
			postList = blogService.getPostListByCategory(blogId, categoryNo.get());
			typeStr = "category";
		} else { // blogId, categoryNo, postNo
			postList = new ArrayList<>();
			if("total".equals(type)) { 
				postList = blogService.getPostList(blogId);
			} else {
				postList = blogService.getPostListByCategory(blogId, categoryNo.get());
			}
			
			for(PostVo data : postList) {
				if(data.getNo() == postNo.get()) break;
				postIdx++;
			}
			typeStr = type;
		}
		emptyPost(postList, model);
		
		model.addAttribute("postList", postList);
		model.addAttribute("postIdx", postIdx);
		model.addAttribute("type", typeStr);
		
		return "blog/main";
	}

	// [Todo] 메서드명 변경 필요
	public void emptyPost(List<PostVo> postList, Model model) { 
		if(postList.isEmpty()) {
			PostVo postVo = new PostVo();
			postVo.setContents("게시글이 없습니다.");
			postList.add(postVo);
			model.addAttribute("postList", postList);
		}
	}

	@RequestMapping("/basic/update")
	public String update(
		@PathVariable("id") String blogId, BlogVo vo, 
		@RequestParam(value = "logo-file") MultipartFile file) {

		String url = fileUploadService.restore(file);
		if(url == null) {
			url = vo.getImage();
		}
		vo.setImage(url);
		blogService.updateAdminBasic(vo);

		return "redirect:/" + blogId + "/admin/basic";
	}

	@RequestMapping("/admin/basic")
	public String adminBasic(@PathVariable("id") String blogId, Model model) {
		BlogVo blogVo = blogService.getBlogAdmin(blogId);
		model.addAttribute("blogVo", blogVo);
		model.addAttribute("selected", "basic");

		return "blog/admin-basic";
	}

	@RequestMapping("/admin/category")
	public String adminCategory(@PathVariable("id") String blogId, Model model) {
		List<CategoryVo> list = blogService.getCategoryList(blogId);
		BlogVo blogVo = blogService.getBlogAdmin(blogId);
		List<String> postCountList = new ArrayList<>();
		for(int i = 0 ; i < list.size() ; i++) {
			Long categoryNo = list.get(i).getNo();

			int postCount = blogService.getPostCount(blogId, categoryNo);
			postCountList.add(Integer.toString(postCount));
		}

		model.addAttribute("selected", "category");
		model.addAttribute("list", list);
		model.addAttribute("postCountList", postCountList);
		model.addAttribute("blogVo", blogVo);

		return "blog/admin-category";
	}

	@RequestMapping("/category/add")
	public String update(
		@PathVariable("id") String blogId, CategoryVo vo) {

		blogService.addCategory(vo);

		return "redirect:/" + blogId + "/admin/category";
	}

	@RequestMapping("/category/delete/{no}")
	public String delete(
			@PathVariable("id") String blogId,
			@PathVariable("no") Long no) {

		blogService.deleteCategory(blogId, no);

		return "redirect:/" + blogId + "/admin/category";
	}

	@RequestMapping("/admin/write")
	public String adminWrite(@PathVariable("id") String blogId, Model model) {
		List<CategoryVo> list = blogService.getCategoryList(blogId);
		BlogVo blogVo = blogService.getBlogAdmin(blogId);

		model.addAttribute("selected", "write");
		model.addAttribute("list", list);
		model.addAttribute("blogVo", blogVo);

		return "blog/admin-write";
	}

	@RequestMapping("/write/add")
	public String adminWriteAdd(
		@PathVariable("id") String blogId, PostVo vo,
		@RequestParam(value="category", required=true, defaultValue="") String categoryNo) {

		vo.setCategoryNo(categoryNo);
		blogService.addPost(vo);

		return "redirect:/" + blogId;
	}
}
