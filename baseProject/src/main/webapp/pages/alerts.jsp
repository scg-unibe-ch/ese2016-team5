<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<pre><a href="/">Home</a>   &gt;   Alerts</pre>

<script>
function deleteAlert(button) {
	var id = $(button).attr("data-id");
	$.get("/profile/alerts/deleteAlert?id=" + id, function(){
		$("#alertsDiv").load(document.URL + " #alertsDiv");
	});
}
</script>

<script>
function typeOfAlert(alert) {
	if(alert.getOfferType() == 0)
		return "Rent"
	else if(alert.getOfferType() == 1)
		return "Auction"
	else 
		return "Sale"
}	
</script>
	
<script>
	$(document).ready(function() {
		$("#city").autocomplete({
			minLength : 2
		});
		$("#city").autocomplete({
			source : <c:import url="getzipcodes.jsp" />
		});
		$("#city").autocomplete("option", {
			enabled : true,
			autoFocus : true
		});
		
		var price = document.getElementById('priceInput');
		var radius = document.getElementById('radiusInput');
		
		if(price.value == null || price.value == "" || price.value == "0")
			price.value = "500";
		if(radius.value == null || radius.value == "" || radius.value == "0")
			radius.value = "5";
	});
</script>

<h1>Create and manage alerts</h1>
<hr />

<h2>Create new alert</h2><br />
<form:form method="post" modelAttribute="alertForm" action="/profile/alerts" id="alertForm" autocomplete="off">

<div id="searchDiv">
    		<label for="type-offer">Offer Type:</label>
		<form:radiobutton name="type-rent" id="forRent" path="offerType" value="0"/><label>For Rent</label>
		<form:radiobutton name="type-auction" id="forAuction" path="offerType" value = "1" /><label>For Auction</label>
		<form:radiobutton name="type-sale" id="forSale" path="offerType" value="2"/><label>For Sale</label>
		<br />
                
                
 		<label for="type-offer">Type:</label>
		<form:radiobutton name="type-room" id="room" path="type" value="room" /><label>Room</label>
		<form:radiobutton name="type-studio" id="studio" path="type" value="studio" /><label>Studio</label>
		<form:radiobutton name="type-studio" id="studio" path="type" value="property" /><label>Property</label>
		<br />
	
		<label for="city">City / zip code:</label>
		<form:input type="text" name="city" id="city" path="city"
			placeholder="e.g. Bern" tabindex="3" />
		<form:errors path="city" cssClass="validationErrorText" /><br />
			
		<label for="radius">Within radius of (max.):</label>
		<form:input id="radiusInput" type="number" path="radius"
			placeholder="e.g. 5" step="5" />
		km
		<form:errors path="radius" cssClass="validationErrorText" />
		<br /> 


	
                
                <label for="priceInput">Price (max.):</label>
		<form:input id="priceInput" type="number" path="price" placeholder="e.g. 5" step="50" /> CHF
		<form:errors path="price" cssClass="validationErrorText" /><br />
                
                		<button type="submit" tabindex="7">Subscribe</button>
		<button type="reset" tabindex="8">Cancel</button>
    
    
    </div>

</form:form> <br />
<h2>Your active alerts</h2>

<div id="alertsDiv" class="alertsDiv">			
<c:choose>
	<c:when test="${empty alerts}">
		<p>You currently aren't subscribed to any alerts.
	</c:when>
	<c:otherwise>
		<table class="styledTable" id="alerts">
			<thead>
			<tr>
				<th>Offer Type</th>
				<th>Type</th>
				<th>City</th>
				<th>Radius</th>
				<th>max. Price</th>
				<th>Action</th>
			</tr>
			</thead>
		<c:forEach var="alert" items="${alerts}">
			<tr>
				<td>
				<c:choose>
                    <c:when test="${alert.offerType == 0}">For Rent</c:when>
                    <c:when test="${alert.offerType == 1}">Auction</c:when>
                    <c:when test="${alert.offerType == 2}">For Sale</c:when>
                </c:choose>
				</td>
				<td style="text-transform: capitalize">
				${alert.type}
				</td>
				<td>${alert.city}</td>
				<td>${alert.radius} km</td>
				<td>${alert.price} Chf</td>
				<td><button class="deleteButton" data-id="${alert.id}" onClick="deleteAlert(this)">Delete</button></td>
			</tr>
		</c:forEach>
		</table>
	</c:otherwise>
</c:choose>
</div>

<c:import url="template/footer.jsp" />