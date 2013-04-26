<%--
Copyright by Akos Tajti (akos.tajti@gmail.com)

All rights reserved.

This software is the confidential and proprietary information
of Akos Tajti. ("Confidential Information"). You
shall not disclose such Confidential Information and shall use
it only in accordance with the terms of the license agreement
you entered into with Akos Tajti.
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<c:set var="items" value="${result.items }"></c:set>

<h2><spring:message code="search.result.label" text="Search result"/></h2>

<div class="results">
	<c:forEach items="${items}" var="item">
		<c:url value="/document/${item.properties['id']}" var="documentUrl"/>
		<div>
			<a href="${documentUrl}">${item.properties['description']}</a>
		</div>
		<div class="snippet">
			${item.properties["content"]}
		</div>
	</c:forEach>
</div>
