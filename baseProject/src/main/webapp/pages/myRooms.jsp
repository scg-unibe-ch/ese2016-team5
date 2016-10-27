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
				<div class="resultAd" data-price="${ad.prizePerMonth}" 
								data-moveIn="${ad.moveInDate}" data-age="${ad.moveInDate}" data-creation="${ad.creationDate}">
					<div class="resultLeft">
						<a href="<c:url value='/ad?id=${ad.id}' />"><img
							src="${ad.pictures[0].filePath}" /></a>
						<h2>
							<a href="<c:url value='/ad?id=${ad.id}' />">${ad.title }</a>
						</h2>
						<p>${ad.street}, ${ad.zipcode} ${ad.city}</p>
						<br />
						<p>
							<i><c:choose>
									<c:when test="${ad.studio}">Studio</c:when>
									<c:otherwise>Room</c:otherwise>
								</c:choose></i>
						</p>
					</div>
					<div class="resultRight">
						<h2>CHF ${ad.prizePerMonth }</h2>
						<p>
							<b><c:choose>
									<c:when test="${ad.forSale}">For Sale</c:when>
									<c:otherwise>For Rent</c:otherwise>
								</c:choose></b>
						</p>
                                                <div>
                                                    <p style="margin: 20px 0 0 0">Move-in date: ${ad.moveInDate }</p>
                                                    <p>Creation date: ${ad.creationDate }</p>
                                                </div>
					</div>
				</div>
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
				<div class="resultAd" data-price="${ad.prizePerMonth}" 
								data-moveIn="${ad.moveInDate}" data-age="${ad.moveInDate}" data-creation="${ad.creationDate}">
					<div class="resultLeft">
						<a href="<c:url value='/ad?id=${ad.id}' />"><img
							src="${ad.pictures[0].filePath}" /></a>
						<h2>
							<a href="<c:url value='/ad?id=${ad.id}' />">${ad.title }</a>
						</h2>
						<p>${ad.street}, ${ad.zipcode} ${ad.city}</p>
						<br />
						<p>
							<i><c:choose>
									<c:when test="${ad.studio}">Studio</c:when>
									<c:otherwise>Room</c:otherwise>
								</c:choose></i>
						</p>
					</div>
					<div class="resultRight">
						<h2>CHF ${ad.prizePerMonth }</h2>
						<p>
							<b><c:choose>
									<c:when test="${ad.forSale}">For Sale</c:when>
									<c:otherwise>For Rent</c:otherwise>
								</c:choose></b>
						</p>
                                                <div>
                                                    <p style="margin: 20px 0 0 0">Move-in date: ${ad.moveInDate }</p>
                                                    <p>Creation date: ${ad.creationDate }</p>
                                                </div>
					</div>
				</div>
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