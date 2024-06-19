package com.poscodx.jblog.interceptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import com.poscodx.jblog.vo.UserVo;

public class AdminInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String requestURI = request.getRequestURI();
		HttpSession session = request.getSession();
		UserVo userVo = (UserVo) session.getAttribute("authUser");

        Pattern pattern = Pattern.compile("/jblog3/([^/]+)/");
        Matcher matcher = pattern.matcher(requestURI);

        String blogId = "";
        if (matcher.find()) {
            blogId = matcher.group(1);
        }

		if(userVo == null || !(userVo.getId().equals(blogId))) {
			response.sendRedirect(request.getContextPath() + "/" + blogId);

			return false;
		}

		return true;
	}
}
