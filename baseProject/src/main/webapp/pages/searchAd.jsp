<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<pre><a href="/">Home</a>   &gt;   Search</pre>

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


<script>
function validateType(form)
{
	var filtered = document.getElementById('filtered');
	filtered.checked = true;
}
</script>


<h1>Search for an ad</h1>
<hr />

<form:form method="post" modelAttribute="searchForm" action="/results"
	id="filterForm" autocomplete="off">

	<div id="searchDiv">
            
		<label for="type-offer">Offer Type:</label>
		<form:checkbox name="type-rent" id="forRent" path="offerType" value="0"/><label>For Rent</label>
		<form:checkbox name="type-auction" id="forAuction" path="offerType" value = "1" /><label>For Auction</label>
		<form:checkbox name="type-sale" id="forSale" path="offerType" value="2"/><label>For Sale</label>
		<br />
		
		<label for="type-offer">Type:</label>
		<form:checkbox name="type-room" id="room" path="type" value="room" checked="checked" /><label>Room</label>
		<form:checkbox name="type-studio" id="studio" path="type" value="studio" /><label>Studio</label>
		<form:checkbox name="type-property" id="property" path="type" value="property" /><label>Property</label>
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
		<br /> <label for="price">Price (max.):</label>
		<form:input id="priceInput" type="number" path="price"
			placeholder="e.g. 5" step="50" />
		CHF
		<form:errors path="price" cssClass="validationErrorText" /><br />
		
		<hr class="slim">		
		
		<table style="width: 80%">
			<tr>
				<td><label for="earliestMoveInDate">Earliest move-in date</label></td>
				<td><label for="earliestMoveOutDate">Earliest move-out date (optional)</label></td>
			</tr>
			<tr>
				<td><form:input type="text" id="field-earliestMoveInDate"
						path="earliestMoveInDate" /></td>
				<td><form:input type="text" id="field-earliestMoveOutDate"
						path="earliestMoveOutDate" /></td>
			</tr>
			<tr>
				<td><label for="latestMoveInDate">Latest move-in date</label></td>
				<td><label for="latestMoveOutDate">Latest move-out date (optional)</label></td>
			</tr>
			<tr>
				<td><form:input type="text" id="field-latestMoveInDate"
						path="latestMoveInDate" /></td>
				<td><form:input type="text" id="field-latestMoveOutDate"
						path="latestMoveOutDate" /></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-smoker" path="smokers" value="1" /><label>Smoking inside
						allowed</label></td>
				<td><form:checkbox id="field-animals" path="animals" value="1" /><label>Animals
						inside allowed</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-garden" path="garden" value="1" /><label>Garden
						(co-use)</label></td>
				<td><form:checkbox id="field-balcony" path="balcony" value="1" /><label>Balcony
						or Patio</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-cellar" path="cellar" value="1" /><label>Cellar
						or Attic</label></td>
				<td><form:checkbox id="field-furnished" path="furnished"
						value="1" /><label>Furnished</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-cable" path="cable" value="1" /><label>Cable TV</label></td>
				<td><form:checkbox id="field-garage" path="garage" value="1" /><label>Garage</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-internet" path="internet" value="1" /><label>WiFi</label></td>
				<td><form:checkbox id="field-dishwasher" path="dishwasher" value="1" /><label>Dishwasher</label></td>
			</tr>
		</table>
		
		<button type="submit" onClick="validateType(this.form)">Search</button>	
		<button type="reset">Cancel</button>
		</div>
</form:form>

<c:import url="template/footer.jsp" />