<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<Link rel="stylesheet"
	href="${pageContext.request.contextPath }/assets/css/jblog.css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="wrapper">
			<div id="content" class="full-screen">
				<c:import url="/WEB-INF/views/includes/admin-menu.jsp" />

				<!-- 카테고리 리스트 테이블 -->
				<table class="admin-cat">
					<tr>
						<th>번호</th>
						<th>카테고리명</th>
						<th>포스트 수</th>
						<th>설명</th>
						<th>삭제</th>
					</tr>
					<c:set var="count" value="${fn:length(list) }" />
					<c:forEach items="${list }" var="vo" varStatus="status">
						<tr>
							<td>${- (status.index - count) }</td> <!-- 임시 -->
							<td>${vo.name }</td> <!-- 임시 -->
							<td>${postCountList[status.index] }</td>
							<td>${vo.description }</td>
							<td>
								<c:if test="${vo.name != '미분류'}">
									<a href="${pageContext.request.contextPath }/${blogVo.blogId}/category/delete/${vo.no }">
										<img src="${pageContext.request.contextPath}/assets/images/delete.jpg">
									</a>
								</c:if>
								<c:if test="${vo.name == '미분류'}">
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>

				<!-- 새로운 카테고리 추가 -->
				<form method="post"
					action="${pageContext.request.contextPath }/${blogVo.blogId }/category/add">
					<input type="hidden" name="blogId" value="${blogVo.blogId }" />
					<h4 class="n-c">새로운 카테고리 추가</h4>
					<table id="admin-cat-add">
						<tr>
							<td class="t">카테고리명</td>
							<td><input type="text" name="name"></td>
						</tr>
						<tr>
							<td class="t">설명</td>
							<td><input type="text" name="description"></td>
						</tr>
						<tr>
							<td class="s">&nbsp;</td>
							<td><input type="submit" value="카테고리 추가"></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>