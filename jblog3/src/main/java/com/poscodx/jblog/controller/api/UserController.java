package com.poscodx.jblog.controller.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poscodx.jblog.service.UserService;
import com.poscodx.jblog.vo.UserVo;

@RestController("userApiController")
@RequestMapping("/user/api")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("/checkId")
	public Object checkId(@RequestParam(value="id", required=true, defaultValue="") String id) {
		UserVo vo = userService.getUser(id);
		return Map.of("exist", vo != null);
	}
}
