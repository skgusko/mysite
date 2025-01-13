<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath }/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="board">
				<form id="search_form" action="${pageContext.request.contextPath}/board" method="post">
					<input type = "hidden" name = "a" value="search">
					<input type="text" id="kwd" name="kwd" value="${keyword}">
					<input type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>	
					<c:forEach var="vo" items="${map.list}" varStatus="status">
						<tr>
							<td>[${fn:length(map.list) - status.index}]</td>
							<td style="text-align:left; padding-left:${vo.depth * 20 }px"> 	
								<c:if test="${vo.depth > 0 }">
									<img src="${pageContext.request.contextPath }/assets/images/reply.png">
								</c:if>
								<a href="${pageContext.request.contextPath }/board/view/${vo.id }?page=${map.currentPage }">${vo.title }</a>
							</td>
							<td>${vo.userName }</td>
							<td>${vo.hit }</td>
							<td>${vo.regDate }</td>
							<c:choose>
								<c:when test="${sessionScope.authUser.id == vo.userId }">
									<td><a href="${pageContext.request.contextPath }/board/delete/${vo.id }?page=${map.currentPage }" class="del">삭제</a></td>
								</c:when>
								<c:otherwise>
									<td></td>
								</c:otherwise>
							</c:choose>
						</tr>
					</c:forEach>
				</table>
				
				<!-- pager 추가 -->
				<div class="pager">	
				    <ul>
				        <!-- 이전 페이지 그룹 -->
				        <li>
				            <c:if test="${map.prevPage > 0}">
				                <a href="${pageContext.request.contextPath}/board?page=${map.prevPage}">◀</a>
				            </c:if>
				            <c:if test="${map.prevPage == 0}">
				                <span>◀</span>
				            </c:if>
				        </li>
				        <!-- 페이지 숫자 -->
				        <c:forEach var="i" begin="${map.beginPage}" end="${map.endPage}">
			                <c:choose>
			                    <c:when test="${i <= map.pageCount}">
			                        <li class="${i == map.currentPage ? 'selected' : ''}">
			                        	<c:choose>
			                        		<c:when test="${empty map.keyword }">
			                        			<a href="${pageContext.request.contextPath}/board?page=${i}">${i}</a>
			                        		</c:when>
			                        		<c:otherwise>
												<a href="${pageContext.request.contextPath}/board?a=search&page=${i}&kwd=${map.keyword}">${i}</a>			                        		
			                        		</c:otherwise>
			                        	</c:choose>
		                        	</li>
			                    </c:when>
			                    <c:otherwise>
			                        <li>
			                        	<span>${i}</span>
			                        </li>
			                    </c:otherwise>
			                </c:choose>
				        </c:forEach>
				
				        <!-- 다음 페이지 그룹 -->
				        <li>
				            <c:if test="${map.nextPage > 0}">
				                <a href="${pageContext.request.contextPath}/board?page=${map.nextPage}">▶</a>
				            </c:if>
				            <c:if test="${map.nextPage == 0}">
				                <span>▶</span>
				            </c:if>
				        </li>
				    </ul>
				</div>	
				<!-- pager 추가 -->
				
				<div class="bottom">
					<c:if test="${not empty sessionScope.authUser}">
	        			<a href="${pageContext.request.contextPath}/board/write" id="new-book">글쓰기</a>
	    			</c:if>
				</div>				
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"/>
		<c:import url="/WEB-INF/views/includes/footer.jsp"/>
	</div>
</body>
</html>