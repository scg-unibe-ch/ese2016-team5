<c:set var="status" value="active" />
<c:if test="${ad.status == 0}">
    <c:set var="status" value="inactive" />
</c:if>
<div class="resultAd ${status}" data-price="${ad.prizePerMonth}" data-moveIn="${ad.moveInDate}" data-age="${ad.moveInDate}" data-creation="${ad.creationDate}">
    <div class="resultLeft">
        <a href="<c:url value='/ad?id=${ad.id}' />">
            <img src="${ad.pictures[0].filePath}" />
        </a>
        <h2>
            <a class="link" href="<c:url value='/ad?id=${ad.id}' />">${ad.title }</a>
        </h2>
        <p>${ad.street}, ${ad.zipcode} ${ad.city}</p>
        <p>
            <i>
                <c:choose>
                    <c:when test="${ad.studio}">Studio</c:when>
                    <c:when test="${ad.room}">Room</c:when>
                </c:choose>
            </i>
        </p>
    </div>
    <div class="resultRight">
        <h2>CHF
            <c:choose>
                <c:when test="${ad.offerType == 0}">${ad.prizePerMonth}</c:when>
                <c:when test="${ad.offerType == 1}">
                    <c:choose>
                        <c:when test="${ad.lastBidder == null}">
                            ${ad.auctionStartingPrize}
                        </c:when>
                        <c:otherwise>
                            ${ad.lastBid}
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:when test="${ad.offerType == 2}">${ad.directBuyPrize}</c:when>
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
        <fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate" type="date" pattern="dd.MM.yyyy" />
        <div class="adDates">
            <p>Move-in date: ${ad.moveInDate }</p>
            <p>Last update: ${ad.creationDate}</p>
        </div>
    </div>
        <div class="expired">
            <span>Expired</span>
        </div>
</div>