<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="status" value="active" />
<c:if test="${ad.status == 0}">
    <c:set var="status" value="inactive" />
</c:if>

<c:set var="userRole" value="standard" />
<c:if test="${ad.getUser().isPremiumUser()}">
    <c:set var="userRole" value="premium" />
</c:if>
    
<div class="resultAd ${status} ${userRole}" data-price="${ad.pricePerMonth}" data-moveIn="${ad.moveInDate}" data-age="${ad.moveInDate}" data-creation="${ad.creationDate}">
    <div class="resultLeft">
        <a href="<c:url value='/ad?id=${ad.id}' />">
            <img src="${ad.pictures[0].filePath}" />
        </a>
        <h2>
            <a class="link" href="<c:url value='/ad?id=${ad.id}' />">${ad.title }</a>
        </h2>
        <p>${ad.street}, ${ad.zipcode} ${ad.city}</p>
        <p>
            <i style="text-transform: capitalize">${ad.type}</i>
        </p>
    </div>
    <div class="resultRight">
        <h2>CHF
            <c:choose>
                <c:when test="${ad.offerType == 0}">${ad.pricePerMonth}</c:when>
                <c:when test="${ad.offerType == 1}">
                    <c:choose>
                        <c:when test="${ad.lastBidder == null}">
                            ${ad.auctionStartingPrice}
                        </c:when>
                        <c:otherwise>
                            ${ad.lastBid}
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:when test="${ad.offerType == 2}">${ad.directBuyPrice}</c:when>
            </c:choose>
            
        </h2>
        <p>
            <b>
                <c:choose>
                    <c:when test="${ad.offerType == 0}">For Rent</c:when>
                    <c:when test="${ad.offerType == 1}">Auction</c:when>
                    <c:when test="${ad.offerType == 2}">For Sale</c:when>
                </c:choose>
            </b>
        </p>
        <div class="adDates">
            <c:if test="${ad.offerType == 0}">
                <fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate" type="date" pattern="dd.MM.yyyy" />
                <p>Move-in date: ${formattedMoveInDate}</p>
            </c:if>
            <c:if test="${ad.offerType == 1}">
                <fmt:formatDate value="${ad.auctionEndingDate}" var="formattedEndingDate" type="date" pattern="dd.MM.yyyy" />
                <p>Ending date: ${formattedEndingDate}</p>
            </c:if>
            <fmt:formatDate value="${ad.creationDate}" var="formattedCreationDate" type="date" pattern="dd.MM.yyyy" />
            <p>Last update: ${formattedCreationDate}</p>
        </div>
    </div>
        <div class="info expired">
            <span>Expired</span>
        </div>
        <div class="info premium">
            <span>Premium</span>
        </div>
	<div style="clear: both"></div>
</div>