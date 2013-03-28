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

<h2>Search results</h2>

<div class="results">
	<c:forEach items="${items}" var="item">
		<c:url value="/document/${item.properties['id']}" var="documentUrl"/>
		<div>
			<a href="${documentUrl}">Document ${item.properties['id']}</a>
		</div>
		<div class="snippet">
			${item.properties["content"]}
		</div>
	</c:forEach>
</div>
