<%--
Copyright by Akos Tajti (akos.tajti@gmail.com)

All rights reserved.

This software is the confidential and proprietary information
of Akos Tajti. ("Confidential Information"). You
shall not disclose such Confidential Information and shall use
it only in accordance with the terms of the license agreement
you entered into with Akos Tajti.
 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<html>
	<head>
		<title>
			<spring:message code="appName"/>
		</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link href='<c:url value="/css/bootstrap.min.css"/>' rel="stylesheet" media="screen">
		<link href='<c:url value="/css/main.css"/>' rel="stylesheet" media="screen">
	</head>
	<body>
		<div id="header" class="row-fluid">
			<div class="span3">
				<c:url value="/upload" var="uploadUrl"/>
				<a href="<c:url value="j_spring_security_logout" />" ><spring:message code="signout.label" text="Sign out"/></a>
				<a href="${uploadUrl}" title="Upload"><spring:message code="upload.label" text="Upload"/></a>
			</div>
			<div class="span3 offset6">
				<c:url value="/search" var="actionUrl"/>
				<span><form:form method="POST" action="${actionUrl}">
					<input type="text" name="keyword" title="search"/>
					<input type="button" name="submit" value="Search" class="button">
				</form:form></span>
			</div>
		</div>
		<div id="main">
			<decorator:body />
		</div>
		<script src="http://code.jquery.com/jquery.js" type="text/javascript"></script>
		<script src='<c:url value="/js/bootstrap.min.js"/>' type="text/javascript"></script>
	</body>
</html>
