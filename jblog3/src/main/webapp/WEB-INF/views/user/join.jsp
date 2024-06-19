<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<Link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jblog.css">
<script src="${pageContext.request.contextPath }/assets/js/jquery/jquery-1.9.0.js"></script>
<script>
$(function() {
	$("#btn-checkId").click(function() {
		var id = $("#blog-id").val();
		if(id == '') {
			return;
		}
		
		$.ajax({
			url: "/jblog3/user/api/checkId?id=" + id,
			type: "get",
			dataType: "json",
			error: function(xhr, status, err){
				console.error(err);			
			},
			success: function(response){
				if(response.exist) {
					alert("존재하는 아이디입니다. 다른 아이디를 사용해 주세요.");
					$("#blog-id").val("");
					$("#blog-id").focus();
					return;
				}
				
				// 사용할 수 있는 아이디
				$("#btn-checkId").hide();
				$("#img-checkId").show();
			}
		});
	})
});
</script>
</head>
<body>
	<div class="center-content">
		<c:import url="/WEB-INF/views/includes/menu.jsp" />
		<form:form 
			modelAttribute="userVo" 
			class="join-form" 
			id="join-form" 
			method="post" 
			action="${pageContext.request.contextPath }/user/join">

			<label class="block-label" for="name">이름</label>
			<form:input path="name" />
			<p style="padding:3px 0 5px 0; text-align: left; color: #f00">
				<form:errors path="name" />
			</p>
			
			<label class="block-label" for="blog-id">아이디</label>
			<input id="blog-id" name="id" type="text"> 
			<input id="btn-checkId" type="button" value="id 중복체크">
			<img id="img-checkId" style="display: none;" src="${pageContext.request.contextPath}/assets/images/check.png">

			<label class="block-label" for="password">패스워드</label>
			<!-- <input id="password" name="password" type="password" /> -->
			<form:password path="password" />
			<p style="padding:3px 0 5px 0; text-align: left; color: #f00">
				<form:errors path="password" />
			</p>

			<fieldset>
				<legend>약관동의</legend>
				<input id="agree-prov" type="checkbox" name="agreeProv" value="y">
				<label class="l-float">서비스 약관에 동의합니다.</label>
			</fieldset>

			<input type="submit" value="가입하기">
			<c:if test="${!joinResult}" >
				<p style="color: red;">중복되는 ID입니다.</p>
			</c:if>
		</form:form>
	</div>