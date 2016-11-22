<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<c:import url="template/header.jsp" />
<pre><a href="/">Home</a>   &gt;   <a href="/searchAd/">Search</a>   &gt;   Results</pre>

<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAsq47dCVpI-c0Ct-PDLFdg0HWcIB-P69g&callback=initMap"></script>
<script type="text/javascript">
    
    function initMap() {

        $(function() {

            var map = new google.maps.Map(document.getElementById('map'));
            
            var addresses = [];
            <c:forEach var="ad" items="${results}">
                var offerType;
                if (0 === ${ad.offerType}) offerType = 'Rent';
                if (1 === ${ad.offerType}) offerType = 'Auction';
                if (2 === ${ad.offerType}) offerType = 'Buy';
                addresses.push({
                    address: "${ad.street}, ${ad.zipcode} ${ad.city}",
                    title: "${ad.title}",
                    id: ${ad.id},
                    img: "${ad.pictures[0].filePath}",
                    type: "${ad.type}".charAt(0).toUpperCase() + "${ad.type}".slice(1),
                    offerType: offerType
                });
            </c:forEach>
                
            var latLngList = [];
            var counter = 0;
            for (var x = 0; x < addresses.length; x++) {
                counter++;
                (function(address) {
                $.getJSON('http://maps.googleapis.com/maps/api/geocode/json?address='+address.address+'&sensor=false', null, function (data) {
                    var p = data.results[0].geometry.location
                    var latlng = new google.maps.LatLng(p.lat, p.lng);
                    latLngList.push(latlng);
                    var marker = new google.maps.Marker({
                        position: latlng,
                        map: map
                    });
                    marker.addListener('click', function() {
                        if (typeof window.infoWindow !== 'undefined') {
                            window.infoWindow.close();
                        }
                        window.infoWindow = new google.maps.InfoWindow({
                            content:    '<div class="mapInfo"><a href="/ad?id=' + address.id + '"><img src="' + address.img + '" /></a>' + 
                                        '<h2><a href="/ad?id=' + address.id + '">' + address.title + '</a></h3>' +
                                        '<p>Type: ' + address.type + '</p>' + 
                                        '<p>Offer Type: ' + address.offerType + '</p></div>'
                        });
                        window.infoWindow.open(map, marker);
                    });
                    
                    counter--;
                    if (counter === 0) {
                        latlngbounds = new google.maps.LatLngBounds();

                        latLngList.forEach(function(latLng){
                           latlngbounds.extend(latLng);
                        });

                        map.setCenter(latlngbounds.getCenter());
                        map.fitBounds(latlngbounds); 
                    }
                });
                })(addresses[x]);
            }
            
            
        });
        

    }
</script>


<script>
function validateType(form)
{
	var filtered = document.getElementById('filtered');
	filtered.checked = true;
}
</script>


<script>
/*
 * This script takes all the resultAd divs and sorts them by a parameter specified by the user.
 * No arguments need to be passed, since the function simply looks up the dropdown selection.
 */
function sort_div_attribute() {
    //determine sort modus (by which attribute, asc/desc)
    var sortmode = $('#modus').find(":selected").val();   
    
    //only start the process if a modus has been selected
    if(sortmode.length > 0) {
    	var attname;
		
    	//determine which variable we pass to the sort function
		if(sortmode == "price_asc" || sortmode == "price_desc")
			attname = 'data-price';
	    else if(sortmode == "moveIn_asc" || sortmode == "moveIn_desc")	
			attname = 'data-moveIn';
	    else
			attname = 'data-age';
    	
		//copying divs into an array which we're going to sort
	    var divsbucket = new Array();
	    var divslist = $('div.resultAd');
	    var divlength = divslist.length;
	    for (a = 0; a < divlength; a++) {
			divsbucket[a] = new Array();
			divsbucket[a][0] = divslist[a].getAttribute(attname);
			divsbucket[a][1] = divslist[a];
			divslist[a].remove();
	    }
		
	    //sort the array
		divsbucket.sort(function(a, b) {
	    if (a[0] == b[0])
			return 0;
	    else if (a[0] > b[0])
			return 1;
        else
			return -1;
		});

	    //invert sorted array for certain sort options
		if(sortmode == "price_desc" || sortmode == "moveIn_asc" || sortmode == "dateAge_asc")
			divsbucket.reverse();
        
	    //insert sorted divs into document again
		for(a = 0; a < divlength; a++)
        	$("#resultsDiv").append($(divsbucket[a][1]));
	}
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
		
		$("#field-earliestMoveInDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#field-latestMoveInDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#field-earliestMoveOutDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#field-latestMoveOutDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
                
	});
</script>

<h1>Search results:</h1>

<hr />

<div>
<select id="modus">
    <option value="">Sort by:</option>
    <option value="price_asc">Price (ascending)</option>
    <option value="price_desc">Price (descending)</option>
    <option value="moveIn_desc">Move-in date (earliest to latest)</option>
    <option value="moveIn_asc">Move-in date (latest to earliest)</option>
    <option value="dateAge_asc">Date created (youngest to oldest)</option>
    <option value="dateAge_desc">Date created (oldest to youngest)</option>
</select>

<button onClick="sort_div_attribute()">Sort</button>	
</div>
<c:choose>
	<c:when test="${empty results}">
		<p>No results found!
	</c:when>
	<c:otherwise>
		<div id="resultsDiv" class="resultsDiv">	
                    
                    <div id="map" style="height: 400px; width: 100%; border: 1px solid black">
                    </div>

			<c:forEach var="ad" items="${results}">
                            <%@include file="ad.jsp"%>
			</c:forEach>
                    
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
                    
		</div>
	</c:otherwise>
</c:choose>

<form:form method="post" modelAttribute="searchForm" action="/results"
	id="filterForm" autocomplete="off">

	<div id="filterDiv">
		<h2>Filter results:</h2>
		<label for="type-offer">Offer Type:</label>
		<form:checkbox name="type-rent" id="forRent" path="offerType" value="0"/><label>For Rent</label>
		<form:checkbox name="type-auction" id="forAuction" path="offerType" value = "1" /><label>For Auction</label>
		<form:checkbox name="type-sale" id="forSale" path="offerType" value="2"/><label>For Sale</label>
		<br />
		<label for="type-offer">Type:</label>
		<form:checkbox name="type-room" id="room" path="room" /><label>Room</label>
		<form:checkbox name="type-studio" id="studio" path="studio" /><label>Studio</label>
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
			
		
		<button type="submit" onClick="validateType(this.form)" >Filter</button>	
		<button type="reset">Cancel</button>
	</div>
</form:form>

<c:import url="template/footer.jsp" />