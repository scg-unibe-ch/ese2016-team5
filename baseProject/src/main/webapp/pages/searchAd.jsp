<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

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
		
		var price = document.getElementById('prizeInput');
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
	id="searchForm" autocomplete="off">

	<fieldset>
		<label for="type-offer">Offer Type:</label>
		<form:checkbox name="forRent" id="forRent" path="forRent" /><label>For Rent</label>
		<form:checkbox name="forSale" id="forSale" path="forSale" /><label>For Sale</label>
		<form:checkbox name="forAuction" id="forAuction" path="forAuction" /><label>For Auction</label><br>
		
		<label for="type-offer">Type:</label>
		<form:checkbox name="room" id="room" path="room" /><label>Room</label>
		<form:checkbox name="studio" id="studio" path="studio" /><label>Studio</label>
		
		<label for="city">City / zip code:</label>
		<form:input type="text" name="city" id="city" path="city"
			placeholder="e.g. Bern" tabindex="3" />
		<form:errors path="city" cssClass="validationErrorText" />
		

		<label for="radius">Within radius of (max.):</label>
		<form:input id="radiusInput" type="number" path="radius"
			placeholder="e.g. 5" step="5" />
		km
		<form:errors path="radius" cssClass="validationErrorText" />
		<br /> <label for="prize">Price (max.):</label>
		<form:input id="prizeInput" type="number" path="prize"
			placeholder="e.g. 5" step="50" />
		CHF
		<form:errors path="prize" cssClass="validationErrorText" />
		<br />

		<button type="submit" tabindex="7" onClick="validateType(this.form)">Search</button>
		<a href="searchAdvancedAd"><button type="button" tabindex="8" value="/searchAdvancedAd">Advanced Search</button></a>
		<button type="reset" tabindex="9">Cancel</button>
		
	</fieldset>

</form:form>

<c:import url="template/footer.jsp" />