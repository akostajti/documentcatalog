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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<spring:message code="welcome" arguments="${user.username}"/>

<c:forEach items="${documents}" var="document">
	<c:url value="/document/${document.id}" var="documentUrl"/>
	<div>
		<a href="${documentUrl}">${document.description}</a>
	</div>
	<div>
		<c:choose>
			<c:when test="${not empty document.comment}">
				${document.comment}
			</c:when>
			<c:otherwise>
				${document.generatedSummary}
			</c:otherwise>
		</c:choose>
	</div>
</c:forEach>
