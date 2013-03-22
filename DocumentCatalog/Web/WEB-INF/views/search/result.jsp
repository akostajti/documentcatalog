<%--
Copyright by Akos Tajti (akos.tajti@gmail.com)

All rights reserved.

This software is the confidential and proprietary information
of Akos Tajti. ("Confidential Information"). You
shall not disclose such Confidential Information and shall use
it only in accordance with the terms of the license agreement
you entered into with Akos Tajti.
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="items" value="${result.items }"></c:set>

Search results:

<c:forEach items="${items}" var="item">
	<div class="searchResultItem">
		<c:forEach items="${item.properties}" var="property">
			<span>${property.key }: ${property.value }</span>
		</c:forEach>
	</div>
</c:forEach>