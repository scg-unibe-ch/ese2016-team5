<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<c:import url="template/header.jsp" />

<script>
	$(document).ready(function() {
	});

</script>


<pre><a href="/">Home</a>   &gt;   My Rooms</pre>

<c:choose>
	<c:when test="${empty ownAdvertisements}">
		<h1>My Advertisements</h1>
		<hr />
		<p>You have not advertised anything yet.</p>
		<br /><br />
	</c:when>
	<c:otherwise>
	
		<div id="resultsDiv" class="resultsDiv">
		<h1>My Advertisements</h1>
		<hr />			
			<c:forEach var="ad" items="${ownAdvertisements}">
                            <%@include file="ad.jsp"%>
			</c:forEach>
			<br /> <br />
		</div>	
            
	</c:otherwise>
</c:choose>


<c:choose>
	<c:when test="${empty bookmarkedAdvertisements}">
		<h1>My Bookmarks</h1>
		<hr />
		<p>You have not bookmarked anything yet.</p><br /><br />
	</c:when>
	<c:otherwise>
		
		<div id="resultsDiv" class="resultsDiv">
		<h1>My Bookmarks</h1>
		<hr />			
			<c:forEach var="ad" items="${bookmarkedAdvertisements}">
				<%@include file="ad.jsp"%>
			</c:forEach>
		</div>		
	</c:otherwise>
</c:choose>

<script type="text/javascript">
    var days = 300;
    $('.resultAd').each(function(i, elm) {
        var date = $(elm).data('creation').split('-');
        var ad = (new Date(date[0], date[1], date[2])).getTime();
        var now = (new Date()).getTime();
        if (now - ad > 86400*1000*days) {
            $(elm).addClass('old');
        }
    });
</script>
                
<c:import url="template/footer.jsp" />