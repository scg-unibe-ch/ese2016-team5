<%@page import="ch.unibe.ese.team1.model.Ad"%>
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<!-- check if user is logged in -->
<security:authorize var="loggedIn" url="/profile" />

<c:import url="template/header.jsp" />

<pre><a href="/">Home</a>   &gt;   <a href="/profile/myRooms">My Rooms</a>   &gt;   Ad Description</pre>

<script src="/js/image_slider.js"></script>
<script src="/js/adDescription.js"></script>

<script>
	var shownAdvertisementID = "${shownAd.id}";
	var shownAdvertisement = "${shownAd}";
	
	function attachBookmarkClickHandler(){
		$("#bookmarkButton").click(function() {
			
			$.post("/bookmark", {id: shownAdvertisementID, screening: false, bookmarked: false}, function(data) {
				$('#bookmarkButton').replaceWith($('<a class="right" id="bookmarkedButton">' + "Bookmarked" + '</a>'));
				switch(data) {
				case 0:
					alert("You must be logged in to bookmark ads.");
					break;
				case 1:
					// Something went wrong with the principal object
					alert("Return value 1. Please contact the WebAdmin.");
					break;
				case 3:
					$('#bookmarkButton').replaceWith($('<a class="right" id="bookmarkedButton">' + "Bookmarked" + '</a>'));
					break;
				default:
					alert("Default error. Please contact the WebAdmin.");	
				}
				
				attachBookmarkedClickHandler();
			});
		});
	}
	
	function attachBookmarkedClickHandler(){
		$("#bookmarkedButton").click(function() {
			$.post("/bookmark", {id: shownAdvertisementID, screening: false, bookmarked: true}, function(data) {
				$('#bookmarkedButton').replaceWith($('<a class="right" id="bookmarkButton">' + "Bookmark Ad" + '</a>'));
				switch(data) {
				case 0:
					alert("You must be logged in to bookmark ads.");
					break;
				case 1:
					// Something went wrong with the principal object
					alert("Return value 1. Please contact the WebAdmin.");
					break;
				case 2:
					$('#bookmarkedButton').replaceWith($('<a class="right" id="bookmarkButton">' + "Bookmark Ad" + '</a>'));
					break;
				default:
					alert("Default error. Please contact the WebAdmin.");
					
				}			
				attachBookmarkClickHandler();
			});
		});
	}

	$(document).ready(function() {
		attachBookmarkClickHandler();
		attachBookmarkedClickHandler();
		
		$.post("/bookmark", {id: shownAdvertisementID, screening: true, bookmarked: true}, function(data) {
			if(data == 3) {
				$('#bookmarkButton').replaceWith($('<a class="right" id="bookmarkedButton">' + "Bookmarked" + '</a>'));
				attachBookmarkedClickHandler();
			}
			if(data == 4) {
				$('#shownAdTitle').replaceWith($('<h1>' + "${shownAd.title}" + '</h1>'));
			}
		});
		
		$("#newMsg").click(function(){
			$("#content").children().animate({opacity: 0.4}, 300, function(){
				$("#msgDiv").css("display", "block");
				$("#msgDiv").css("opacity", "1");
			});
		});
		
		$("#messageCancel").click(function(){
			$("#msgDiv").css("display", "none");
			$("#msgDiv").css("opacity", "0");
			$("#content").children().animate({opacity: 1}, 300);
		});
		
		$("#messageSend").click(function (){
			if($("#msgSubject").val() != "" && $("#msgTextarea").val() != ""){
				var subject = $("#msgSubject").val();
				var text = $("#msgTextarea").val();
				var recipientEmail = "${shownAd.user.username}";
				$.post("profile/messages/sendMessage", {subject : subject, text: text, recipientEmail : recipientEmail}, function(){
					$("#msgDiv").css("display", "none");
					$("#msgDiv").css("opacity", "0");
					$("#msgSubject").val("");
					$("#msgTextarea").val("");
					$("#content").children().animate({opacity: 1}, 300);
				})
			}
		});
	});
		
</script>


<!-- format the dates -->
<fmt:formatDate value="${shownAd.moveInDate}" var="formattedMoveInDate"
	type="date" pattern="dd.MM.yyyy" />
<fmt:formatDate value="${shownAd.creationDate}" var="formattedCreationDate"
	type="date" pattern="dd.MM.yyyy" />
<c:choose>
	<c:when test="${empty shownAd.moveOutDate }">
		<c:set var="formattedMoveOutDate" value="unlimited" />
	</c:when>
	<c:otherwise>
		<fmt:formatDate value="${shownAd.moveOutDate}"
			var="formattedMoveOutDate" type="date" pattern="dd.MM.yyyy" />
	</c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${empty shownAd.auctionEndingDate}">
        <c:set var="formattedAuctionEndingDate" value="" />
    </c:when>
    <c:otherwise>
        <fmt:formatDate value="${shownAd.auctionEndingDate}" var="formattedAuctionEndingDate" type="date" pattern="dd.MM.yyyy HH:mm" />
    </c:otherwise>
</c:choose>


<h1 id="shownAdTitle">${shownAd.title}
	<c:choose>
		<c:when test="${loggedIn}">
			<a class="right" id="bookmarkButton">Bookmark Ad</a>
		</c:when>
	</c:choose>
</h1>


<hr />

<section>
	<c:choose>
		<c:when test="${loggedIn}">
			<c:if test="${loggedInUserEmail == shownAd.user.username }">
				<a href="<c:url value='/profile/editAd?id=${shownAd.id}' />">
					<button type="button">Edit Ad</button>
				</a>
			</c:if>
		</c:when>
	</c:choose>
	<br>
	<br>

	<table id="adDescTable" class="adDescDiv">




            <c:choose>

                <c:when test="${shownAd.offerType == 1}">
                    
                    <jsp:useBean id="now" class="java.util.Date" />
                    
                    
                    <c:choose>
                        <c:when test="${ shownAd.lastBid != 0 }"><c:set var="currentPrice" value="${shownAd.lastBid}" /></c:when>
                        <c:otherwise><c:set var="currentPrice" value="${shownAd.auctionStartingPrice}" /></c:otherwise>
                    </c:choose>

                    <tr>
                        <td><h2>Place your bid</h2></td>
                        <td>
                            <c:choose>
                                <c:when test="${shownAd.auctionEndingDate ge now}">
                                    <c:choose>
                                        <c:when test="${loggedInUserEmail != shownAd.user.username }">
                                            <form:form method="post" modelAttribute="shownAd" action="/profile/placeBid" id="placeBidForm" autocomplete="off">
                                                <input type="hidden" name="adId" value="${shownAd.id}" />
                                                <form:input type="text" id="field-lastBid" path="lastBid" value="${currentPrice + 100}" />
                                                <button type="submit">Submit</button>
                                                <p style="color: #AE0000" id="placeBidFormError"></p>
                                            </form:form>
                                            <script type="text/javascript">
                                                $('#placeBidForm button').click(function() {
                                                    if ($('#field-lastBid').val() <= ${currentPrice}) {
                                                        $('#placeBidFormError').text('Your bid needs to be greater than the current price of ' + ${currentPrice} + '!');
                                                        return false;
                                                    } else {
                                                        return true; 
                                                    }
                                                });

                                                $('#field-lastBid').keydown(function() {
                                                    $('#placeBidFormError').text('');
                                                });
                                            </script>
                                        </c:when>
                                        <c:otherwise>
                                            <p>This ad belongs to you, so you cannot place bids!</p>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <p>This auction has expired!</p>
                                </c:otherwise>
                            </c:choose>

                        </td>
                    </tr>
                    <tr>
                        <td><h2>Current price</h2></td>
                        <td>${currentPrice}</td>
                    </tr>
                    <tr>
                        <td><h2>Ending date</h2></td>
                        <td>${formattedAuctionEndingDate}</td>
                    </tr>
                    <tr><td colspan="2"><hr style="margin: 10px 0" /></td></tr>

                </c:when>

                <c:when test="${shownAd.offerType == 2}">
                    <tr>
                        <td><h2>Direct buy</h2></td>
                        <td>
                            <c:choose>
                                <c:when test="${shownAd.status == 0}">This property is sold!</c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${loggedIn}">
                                            <c:choose>
                                                <c:when test="${loggedInUserEmail == shownAd.user.username }">
                                                    <p>This property belongs to you; you cannot buy it.</p>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="<c:url value='/profile/DirectBuy?id=${shownAd.id}' />">
                                                        <button type="button">CHF ${shownAd.directBuyPrice}<br /><strong style="border-top:1px solid white;padding-top:4px;margin-top:4px;display:block">Buy directly</strong></button>
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:when>

            </c:choose>
            
		<tr> 
			<td><h2>Offer Type</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.offerType == 0}">For Rent</c:when>
					<c:when test="${shownAd.offerType == 1}">Auction</c:when>
					<c:otherwise>Direct Buy</c:otherwise>
				</c:choose>
			</td>
		</tr>
		
		<tr>
			<td><h2>Type</h2></td>
			<td style="text-transform: capitalize">${shownAd.type}
			</td>
		</tr>

		<tr>
			<td><h2>Address</h2></td>
			<td>
				<a class="link" href="http://maps.google.com/?q=${shownAd.street}, ${shownAd.zipcode}, ${shownAd.city}">${shownAd.street},
						${shownAd.zipcode} ${shownAd.city}</a>
			</td>
		</tr>

		<tr>
			<td><h2>Available from</h2></td>
			<td>${formattedMoveInDate}</td>
		</tr>

		<c:choose>
			<c:when test="${shownAd.offerType == 0}">
				<tr>
					<td><h2>Move-out Date</h2></td>
					<td>${formattedMoveOutDate}</td>
				</tr>

				<tr>
					<td><h2>Monthly Rent</h2></td>
					<td>${shownAd.pricePerMonth}&#32;CHF</td>
				</tr>
			</c:when>
		</c:choose>

		<tr>
			<td><h2>Square Meters</h2></td>
			<td>${shownAd.squareFootage}&#32;m²</td>
		</tr>
		<tr>
			<td><h2>Ad created on</h2></td>
			<td>${formattedCreationDate}</td>
		</tr>
                <tr><td><h2>Views</h2></td><td>${shownAd.views}</td></tr>
	</table>
</section>

<div id="image-slider">
	<div id="left-arrow">
		<img src="/img/left-arrow.png" />
	</div>
	<div id="images">
		<c:forEach items="${shownAd.pictures}" var="picture">
			<img src="${picture.filePath}" />
		</c:forEach>
	</div>
	<div id="right-arrow">
		<img src="/img/right-arrow.png" />
	</div>
</div>
<div class="clearBoth"></div>
<hr class="clearBoth" />

<div id="adDetailGoogle"><div id="map"></div><div id="pano"></div><hr class="clearBoth" /></div>



<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAsq47dCVpI-c0Ct-PDLFdg0HWcIB-P69g&callback=initMap"></script>
<script>
	var map, sv;
    function initMap() {
        var address = "${shownAd.street}, ${shownAd.zipcode} ${shownAd.city}";
        $.getJSON('http://maps.googleapis.com/maps/api/geocode/json?address='+address+'&sensor=false', null, function (data) {
            if (data.status === 'OK') {
                var p = data.results[0].geometry.location;
                var latlng = {lat: p.lat, lng: p.lng};
                sv = new google.maps.StreetViewService();
                panorama = new google.maps.StreetViewPanorama(document.getElementById('pano'));
				
				// Set up the map.
				map = new google.maps.Map(document.getElementById('map'), {
					center: latlng,
					zoom: 16
				});

				var marker = new google.maps.Marker({
					position: latlng,
					map: map,
					title: '${shownAd.title}'
				});
				
				// Street View
				sv.getPanorama({location: latlng, radius: 50}, processSVData);
				
            }
			else {
				$('#adDetailGoogle, #adDetailGoogle hr').remove();
			}
        });
    }
	
	function processSVData(data, status) {
		if (status === google.maps.StreetViewStatus.OK) {
			panorama.setPano(data.location.pano);
			panorama.setPov({
				heading: 270,
				pitch: 0
			});
			panorama.setVisible(true);
			
			map.addListener('click', function(event) {
				sv.getPanorama({location: event.latLng, radius: 50}, processSVData);
			});
			
			map.setStreetView(panorama);
		} else {
			$('#adDetailGoogle #pano').remove();
			$('#adDetailGoogle #map').css('width', '100%');
			google.maps.event.trigger(map, "resize");
		}
	}
</script>

<section>
	<div id="descriptionTexts">
		<div class="adDescDiv">
			<h2>Room Description</h2>
			<p>${shownAd.roomDescription}</p>
		</div>
		<br />

		<div class="adDescDiv">
			<h2>Preferences</h2>
			<p>${shownAd.preferences}</p>
		</div>
		<br />

		<div id="visitList" class="adDescDiv">
			<h2>Visiting times</h2>
			<table>
				<c:forEach items="${visits }" var="visit">
					<tr>
						<td>
							<fmt:formatDate value="${visit.startTimestamp}" pattern="dd-MM-yyyy " />
							&nbsp; from
							<fmt:formatDate value="${visit.startTimestamp}" pattern=" HH:mm " />
							until
							<fmt:formatDate value="${visit.endTimestamp}" pattern=" HH:mm" />
						</td>
						<td><c:choose>
								<c:when test="${loggedIn}">
									<c:if test="${loggedInUserEmail != shownAd.user.username}">
										<button class="thinButton" type="button" data-id="${visit.id}">Send
											enquiry to advertiser</button>
									</c:if>
								</c:when>
								<c:otherwise>
									<a href="/login"><button class="thinInactiveButton" type="button"
										data-id="${visit.id}">Login to send enquiries</button></a>
								</c:otherwise>
							</c:choose></td>
					</tr>
				</c:forEach>
			</table>
		</div>

	</div>

	<table id="checkBoxTable" class="adDescDiv">
		<tr>
			<td><h2>Smoking inside allowed</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.smokers}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

		<tr>
			<td><h2>Animals allowed</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.animals}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

		<tr>
			<td><h2>Furnished Room</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.furnished}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>
		
		<tr>
			<td><h2>WiFi available</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.internet}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>
		
		<tr>
			<td><h2>Dishwasher available</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.dishwasher}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>
		
		<tr>
			<td><h2>Cable TV</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.cable}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

		<tr>
			<td><h2>Garage</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.garage}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

		<tr>
			<td><h2>Cellar</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.cellar}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

		<tr>
			<td><h2>Balcony</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.balcony}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

		<tr>
			<td><h2>Garden</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.garden}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

	</table>
</section>

<div class="clearBoth"></div>
<br>

<table id="advertiserTable" class="adDescDiv">
	<tr>
	<td><h2>Advertiser</h2><br /></td>
	</tr>

	<tr>
		<td><c:choose>
				<c:when test="${shownAd.user.picture.filePath != null}">
					<img src="${shownAd.user.picture.filePath}">
				</c:when>
				<c:otherwise>
					<img src="/img/avatar.png">
				</c:otherwise>
			</c:choose></td>
		
		<td>${shownAd.user.username}</td>
		
		<td id="advertiserEmail">
		<c:choose>
			<c:when test="${loggedIn}">
				<a href="/user?id=${shownAd.user.id}"><button type="button">Visit profile</button></a>
			</c:when>
			<c:otherwise>
				<a href="/login"><button class="thinInactiveButton" type="button">Login to visit profile</button></a>
			</c:otherwise>
		</c:choose>

		<td>
			<form>
				<c:choose>
					<c:when test="${loggedIn}">
						<c:if test="${loggedInUserEmail != shownAd.user.username }">
							<button id="newMsg" type="button">Contact Advertiser</button>
						</c:if>
					</c:when>
					<c:otherwise>
						<a href="/login"><button class="thinInactiveButton" type="button">Login to contact advertiser</button></a>
					</c:otherwise>
				</c:choose>
			</form>
		</td>
	</tr>
</table>

<div id="msgDiv">
<form class="msgForm">
	<h2>Contact the advertiser</h2>
	<br>
	<br>
	<label>Subject: <span>*</span></label>
	<input  class="msgInput" type="text" id="msgSubject" placeholder="Subject" />
	<br><br>
	<label>Message: </label>
	<textarea id="msgTextarea" placeholder="Message" ></textarea>
	<br/>
	<button type="button" id="messageSend">Send</button>
	<button type="button" id="messageCancel">Cancel</button>
	</form>
</div>

<div id="confirmationDialog">
	<form>
	<p>Send enquiry to advertiser?</p>
	<button type="button" id="confirmationDialogSend">Send</button>
	<button type="button" id="confirmationDialogCancel">Cancel</button>
	</form>
</div>


<c:import url="template/footer.jsp" />