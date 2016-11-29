<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!-- check if user is logged in -->
<security:authorize var="loggedIn" url="/profile" />
<c:import url="template/header.jsp" />

<script type="text/javascript">
    function onSignIn(googleUser) {
        var id_token = googleUser.getAuthResponse().id_token;
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'http://localhost:8080/tokensignin');
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onload = function() {
            var response = xhr.responseText.replace(/"/g, '').replace(/'/g, '"');
            var json = JSON.parse(response);
            if (json.status !== 'success') {
                console.log(json.message);
            }
            else {
                $('#field-email').val(json.email);
                $('#field-password').val(json.password);
                $('#login-form').submit();
            }
            
        };
        xhr.send('token=' + id_token);
    }
</script>

<pre>
	<a href="/">Home</a>   &gt;   Login</pre>

<h1>Login</h1>
<c:choose>
	<c:when test="${loggedIn}">
		<p>You are already logged in!</p>
	</c:when>
	<c:otherwise>
		<c:if test="${!empty param.error}">
			<p>Incorrect email or password. Please retry using correct email
				and password.</p>
			<br />
		</c:if>
                        
                <div style="margin-top: 20px; margin-bottom: 10px">Sign in with Google:</div><div class="g-signin2" data-onsuccess="onSignIn"></div>
                <div style="margin-top: 25px; margin-bottom: 10px">Or sign in with our super secure authentication system:</div>
                        
		<form id="login-form" method="post" action="/j_spring_security_check">
			<label for="field-email">Email:</label> <input name="j_username"
				id="field-email" /> <label for="field-password">Password:</label> <input
				name="j_password" id="field-password" type="password" />
			<button type="submit">Login</button>
		</form>
		<br />
		<h2 style="position: relative">Test users
                    <div style="position: absolute; top: 38px; left: 350px; font-size: 16px; font-style: italic">&lt;-- Click for quick login! :-)</div>
                </h2>

		<ul class="test-users">
			<li>Email: <i>ese@unibe.ch</i>, password: <i>ese</i></li>
			<li>Email: <i>jane@doe.com</i>, password: <i>password</i></li>
			<li>Email: <i>user@bern.com</i>, password: <i>password</i></li>
			<li>Email: <i>oprah@winfrey.com</i>, password: <i>password</i></li>
		</ul>
		<br />

		<h2>Roommates for AdBern</h2>
		<ul class="test-users">
			<li>Email: <i>hans@unibe.ch</i>, password: <i>password</i></li>
			<li>Email: <i>mathilda@unibe.ch</i>, password: <i>password</i></li>
		</ul>
		<br />
		
			Or <a class="link" href="<c:url value="/signup" />">sign up</a> as a new user.
		
	</c:otherwise>
</c:choose>
<script type="text/javascript">
$(function() {
   $('#field-email').focus(); 
   
    $('.test-users li')
        .css('cursor', 'pointer')
        .click(function() {
            var data = $(this).children('i');
            $('#field-email').val($(data).eq(0).text());
            $('#field-password').val($(data).eq(1).text());
            $('#login-form').submit();
        });
   
});
</script>
<c:import url="template/footer.jsp" />