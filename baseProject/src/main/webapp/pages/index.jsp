<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to FlatFindr</title>
</head>
<body>

<pre>Home</pre>

<h1>Welcome to FlatFindr!</h1>

<c:choose>
	<c:when test="${empty newest}">
		<h2>No ads placed yet</h2>
	</c:when>
	<c:otherwise>
		<div id="resultsDiv" class="resultsDiv">	
			<h2>Our newest ads:</h2>		
			<c:forEach var="ad" items="${newest}">
                            <%@include file="ad.jsp"%>
			</c:forEach>
		</div>
	</c:otherwise>
</c:choose>

<c:import url="template/footer.jsp" /><br />