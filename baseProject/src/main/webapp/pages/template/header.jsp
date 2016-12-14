<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<head>
<meta name="google-signin-client_id" content="68304295039-fvgg84j5itsnfupqsfsbeb8ogu2d0vlg.apps.googleusercontent.com">
<link rel="stylesheet" type="text/css" media="screen"
	href="/css/main.css">
<link rel="stylesheet" type="text/css"
	media="only screen and (max-device-width: 480px)"
	href="/css/smartphone.css" />
<link href="../../css/jquery-ui-timepicker-addon.css" rel="stylesheet" type="text/css" />

<Title>HomePortal</Title>
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script
	src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
<link rel="stylesheet"
	href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/themes/smoothness/jquery-ui.css" />

<script src="/js/unreadMessages.js"></script>

<script>
    function signOut() {
      var auth2 = gapi.auth2.getAuthInstance();
      auth2.signOut().then(function () {
        window.location.href = '/logout';
      });
    }
  
    function onLoad() {
      gapi.load('auth2', function() {
        gapi.auth2.init();
      });
    }
</script>
<script src="https://apis.google.com/js/platform.js?onload=onLoad" async defer></script>

<style>
/* ensure that autocomplete lists are not too long and have a scrollbar */
.ui-autocomplete {
	max-height: 200px;
	overflow-y: auto;
	overflow-x: hidden;
}
</style>

<link rel='shortcut icon' type='image/x-icon' href='/img/favicon2.ico' />

</head>

<!-- check if user is logged in -->
<security:authorize var="loggedIn" url="/profile" />

<!-- check if user has a profile picture -->
<header>
	<div class="left">
		<a href="/"><img src="/img/logo.png"></a>
	</div>
	<div class="right">
		<nav>
			<ul>
				<c:choose>
					<c:when test="${loggedIn}">
					<script>
						$(document).ready(unreadMessages("header"));
					</script>
					
					<!-- include user details -->
					<%@include file='/pages/getUserPicture.jsp' %>
						<li id="profile_picture"><a href="#">
						<% 
							out.print("<img src='" + filePath + "' />");

							out.print("<p class='text'>" + realUser.getFirstName() + "<br />"
								+ realUser.getLastName() + "</p>"); 
						%>
						</a>
							<ul>
                                                                <c:choose>
                                                                <c:when test="${loggedIn}">
                                                                    <li class="admin_menu dropdown right"><a href="/profile/alerts">Alerts</a></li>
                                                                    <li class="admin_menu dropdown"><a href="/profile/schedule">Schedule</a></li>
                                                                    <li class="admin_menu dropdown"><a href="/profile/enquiries">Enquiries</a></li>
                                                                    <li class="admin_menu dropdown"><a class="messageLink" href="/profile/messages">Messages</a></li>
                                                                    <li class="admin_menu dropdown"><a href="/profile/myRooms">My rooms</a></li>
                                                                    <li class="admin_menu dropdown"><a href="/profile/placeAd">Place an ad</a></li>
                                                                </c:when>
                                                                </c:choose>
								<li>
								<% out.print("<a href=\"/user?id=" + realUser.getId() + "\">Public Profile</a>"); %>
								</li>
								<li><a href="#" onclick="signOut()">Logout</a></li>
							</ul></li>
					</c:when>
					<c:otherwise>
						<li><a href="/login">Login</a></li>
					</c:otherwise>
				</c:choose>
				<li><a href="<c:url value='/searchAd' />">Search</a></li>
                                <c:choose>
                                <c:when test="${loggedIn}">
                                    <li class="admin_menu horizontal right"><a href="/profile/alerts">Alerts</a></li>
                                    <li class="admin_menu horizontal"><a href="/profile/schedule">Schedule</a></li>
                                    <li class="admin_menu horizontal"><a href="/profile/enquiries">Enquiries</a></li>
                                    <li class="admin_menu horizontal"><a class="messageLink" href="/profile/messages">Messages</a></li>
                                    <li class="admin_menu horizontal"><a href="/profile/myRooms">My rooms</a></li>
                                    <li class="admin_menu horizontal"><a href="/profile/placeAd">Place an ad</a></li>
                                </c:when>
                                </c:choose>
			</ul>
		</nav>
	</div>
</header>

<body>
	<!-- will be closed in footer-->
	<div id="content">

		<c:if test="${not empty confirmationMessage }">
			<div class="confirmation-message">
				<img src="/img/check-mark.png" />
				<p>${confirmationMessage }</p>
			</div>
		</c:if>