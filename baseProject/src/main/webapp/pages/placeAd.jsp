<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<script src="/js/jquery.ui.widget.js"></script>
<script src="/js/jquery.iframe-transport.js"></script>
<script src="/js/jquery.fileupload.js"></script>
<script src="/js/jquery-ui-timepicker-addon.js"></script>

<script src="/js/pictureUpload.js"></script>


<script>
	$(document).ready(function() {
		
		// Go to controller take what you need from user
		// save it to a hidden field
		// iterate through it
		// if there is id == x then make "Bookmark Me" to "bookmarked"
		
		$("#field-city").autocomplete({
			minLength : 2
		});
		$("#field-city").autocomplete({
			source : <c:import url="getzipcodes.jsp" />
		});
		$("#field-city").autocomplete("option", {
			enabled : true,
			autoFocus : true
		});
		$("#field-moveInDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#field-moveOutDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		
		$("#field-visitDay").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		
		$("#addVisitButton").click(function() {
			var date = $("#field-visitDay").val();
			if(date == ""){
				return;
			}
			
			var startHour = $("#startHour").val();
			var startMinutes = $("#startMinutes").val();
			var endHour = $("#endHour").val();
			var endMinutes = $("#endMinutes").val();
			
			if (startHour > endHour) {
				alert("Invalid times. The visit can't end before being started.");
				return;
			} else if (startHour == endHour && startMinutes >= endMinutes) {
				alert("Invalid times. The visit can't end before being started.");
				return;
			}
			
			var newVisit = date + ";" + startHour + ":" + startMinutes + 
				";" + endHour + ":" + endMinutes; 
			var newVisitLabel = date + " " + startHour + ":" + startMinutes + 
			" to " + endHour + ":" + endMinutes; 
			
			var index = $("#addedVisits input").length;
			
			var label = "<p>" + newVisitLabel + "</p>";
			var input = "<input type='hidden' value='" + newVisit + "' name='visits[" + index + "]' />";
			
			$("#addedVisits").append(label + input);
                        
		});
                
                // Offer Type
                function offerTypeShowProperFields(type) {
                    console.log(type);
                    $('.ot-rent, .ot-auction, .ot-direct').hide();
                    $('.' + type).show();
                }
                
                // Set on click
                $('#type-rent, #type-auction, #type-direct').click(function() {
                    var showType = 'ot-' + $(this).attr('id').replace('type-', '');
                    offerTypeShowProperFields(showType);
                });
                
                // Set on load
                var showType = 'ot-' + $('*[name="offerType"][checked="checked"]').attr('id').replace('type-', '');
                offerTypeShowProperFields(showType);

                // Auction Ending Date
                $('#field-auctionEndingDate').datetimepicker({
                    dateFormat : 'dd-mm-yy',
                    hour: (new Date()).getHours()
                });
	});
</script>

<pre>
	<a href="/">Home</a>   &gt;   Place ad</pre>

<h1>Place an ad</h1>
<hr />

<form:form method="post" modelAttribute="placeAdForm"
	action="/profile/placeAd" id="placeAdForm" autocomplete="off"
	enctype="multipart/form-data">

        <fieldset>
            <legend>General info</legend>
            <table class="placeAdTable">
                <tr>
                    <td>
                        <label for="field-title">Ad Title *</label>
                        <form:input id="field-title" path="title" placeholder="Ad Title" />
                        <form:errors path="title" cssClass="validationErrorText" />
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td>
                        <label for="type-rent">Offer Type *</label>
                            <form:radiobutton id="type-rent" path="offerType" value="0" />Rent
                            <form:radiobutton id="type-auction" path="offerType" value="1" />Auction
                            <form:radiobutton id="type-direct" path="offerType" value="2" />Direct
                    </td>
                    <td>
                        <label for="type-room">Property Type *</label>
                        <form:radiobutton id="type-room" path="type" value="room" checked="checked" />Room
                        <form:radiobutton id="type-studio" path="type" value="studio" />Studio
                        <form:radiobutton id="type-property" path="type" value="property" />Property
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="field-street">Street *</label>
                        <form:input id="field-street" path="street" placeholder="Street" />
                        <form:errors path="street" cssClass="validationErrorText" />
                    </td>
                    <td>
                        <label for="field-city">City / Zip code *</label>
                        <form:input id="field-city" path="city" placeholder="City" />
                        <form:errors path="city" cssClass="validationErrorText" />
                    </td>
                </tr>
                <tr>
                <tr class="ot-rent">
                    <td>
                        <label for="moveInDate">Move-in date *</label>
                        <form:input type="text" id="field-moveInDate" path="moveInDate" />
                        <form:errors path="moveInDate" cssClass="validationErrorText" />
                    </td>
                    <td>
                        <label for="moveOutDate">Move-out date (optional)</label>
                        <form:input type="text" id="field-moveOutDate" path="moveOutDate" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <span class="ot-rent">
                            <label for="field-price">Price per month *</label>
                            <form:input id="field-price" type="number" path="price" placeholder="Price per month" step="50" />
                            <form:errors path="price" cssClass="validationErrorText" />
                        </span>
                        <span class="ot-direct">
                            <label for="field-DirectBuyPrice">Price *</label>
                            <form:input id="field-DirectBuyPrice" type="number" path="directBuyPrice" placeholder="Price" step="50" />
                            <form:errors path="directBuyPrice" cssClass="validationErrorText" />
                        </span>
                        <span class="ot-auction">
                            <label for="field-StartingPrice">Starting price *</label>
                            <form:input id="field-StartingPrice" type="number" path="auctionStartingPrice" placeholder="Starting Price" step="50" />
                        </span>
                    </td>
                    <td>
                        <span class="ot-auction">
                            <label for="field-auctionEndingDate">Ending Date *</label>
                            <form:input id="field-auctionEndingDate" path="auctionEndingDate" placeholder="Ending Date" />
                            <form:errors path="auctionEndingDate" cssClass="validationErrorText" />
                        </span>                      
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="field-SquareFootage">Square Meters *</label>
                        <form:input id="field-SquareFootage" type="number" path="squareFootage" placeholder="Square Footage" step="5" />
                        <form:errors path="squareFootage" cssClass="validationErrorText" />
                    </td>
                    <td></td>
                </tr>
            </table>
        </fieldset>

	<br />
	<fieldset>
		<legend>Room Description</legend>

		<table class="placeAdTable">
			<tr>
				<td><form:checkbox id="field-smoker" path="smokers" value="1" /><label>Animals
						allowed</label></td>
				<td><form:checkbox id="field-animals" path="animals" value="1" /><label>Smoking
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
				<td><form:checkbox id="field-cable" path="cable" value="1" /><label>Cable
						TV</label></td>
				<td><form:checkbox id="field-garage" path="garage" value="1" /><label>Garage</label>
				</td>
			</tr>
			<tr>
				<td><form:checkbox id="field-internet" path="internet" value="1" /><label>WiFi available</label></td>
			
				<td><form:checkbox id="field-dishwasher" path="dishwasher" value="1" /><label>Dishwasher available</label></td>
			</tr>
		</table>
		<br />
		<form:textarea path="roomDescription" rows="10" style="width: 100%" placeholder="Room Description" />
		<form:errors path="roomDescription" cssClass="validationErrorText" />
	</fieldset>

	<br />
	<fieldset>
		<legend>Preferences (optional)</legend>
		<form:textarea path="preferences" rows="5" style="width: 100%" placeholder="Preferences"></form:textarea>
	</fieldset>

	<fieldset>
		<legend>Pictures (optional)</legend>
		<br /> <label for="field-pictures">Pictures</label> <input
			type="file" id="field-pictures" accept="image/*" multiple="multiple" />
		<table id="uploaded-pictures" class="styledTable">
			<tr>
				<th id="name-column">Uploaded picture</th>
				<th>Size</th>
				<th>Delete</th>
			</tr>
		</table>
		<br>
	</fieldset>

	<fieldset>
		<legend>Visiting times (optional)</legend>

		<table>
			<tr>
				<td><input type="text" id="field-visitDay" /> <select
					id="startHour">
						<%
							for (int i = 0; i < 24; i++) {
									String hour = String.format("%02d", i);
									out.print("<option value=\"" + hour + "\">" + hour
											+ "</option>");
								}
						%>
				</select> <select id="startMinutes">
						<%
							for (int i = 0; i < 60; i++) {
									String minute = String.format("%02d", i);
									out.print("<option value=\"" + minute + "\">" + minute
											+ "</option>");
								}
						%>
				</select> <span>to&thinsp; </span> <select id="endHour">
						<%
							for (int i = 0; i < 24; i++) {
									String hour = String.format("%02d", i);
									out.print("<option value=\"" + hour + "\">" + hour
											+ "</option>");
								}
						%>
				</select> <select id="endMinutes">
						<%
							for (int i = 0; i < 60; i++) {
									String minute = String.format("%02d", i);
									out.print("<option value=\"" + minute + "\">" + minute
											+ "</option>");
								}
						%>
				</select>



					<div id="addVisitButton" class="smallPlusButton">+</div>

					<div id="addedVisits"></div></td>

			</tr>

		</table>
		<br>
	</fieldset>



	<br />
	<div>
		<button type="submit">Submit</button>
		<a href="/"><button type="button">Cancel</button></a>
	</div>

</form:form>

<c:import url="template/footer.jsp" />
