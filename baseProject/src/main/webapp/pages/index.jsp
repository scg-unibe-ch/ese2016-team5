<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to HomePortal</title>
</head>
<body>
<h1>Welcome to HomePortal</h1>

<c:choose>
	<c:when test="${empty newest}">
		<h2>No ads placed yet</h2>
	</c:when>
	<c:otherwise>
		<div id="resultsDiv" class="home resultsDiv">	
			<h2>Our newest ads:</h2>		
			<c:forEach var="ad" items="${newest}">
                            <%@include file="ad.jsp"%>
			</c:forEach>
		</div>
                <div id="mapHome"></div>
	</c:otherwise>
</c:choose>


<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAsq47dCVpI-c0Ct-PDLFdg0HWcIB-P69g&callback=initMap"></script>
<script type="text/javascript">
    
    function initMap() {

        $(function() {

            var map = new google.maps.Map(document.getElementById('mapHome'));
            
            var addresses = [];
            <c:forEach var="ad" items="${newest}">
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
                        if (data.status === 'OK') {
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
                        }
                        
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

                
                
<c:import url="template/footer.jsp" /><br />