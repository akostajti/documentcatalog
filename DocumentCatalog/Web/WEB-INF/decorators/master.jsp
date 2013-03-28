<%--
Copyright by Akos Tajti (akos.tajti@gmail.com)

All rights reserved.

This software is the confidential and proprietary information
of Akos Tajti. ("Confidential Information"). You
shall not disclose such Confidential Information and shall use
it only in accordance with the terms of the license agreement
you entered into with Akos Tajti.
 --%>
 <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
	<head>
		<title>
			docca
		</title>
	</head>
	<body>
		<div id="header">
			<a href="<c:url value="j_spring_security_logout" />" >Logout</a>
			<div style="float: right;">
				<c:url value="/search" var="actionUrl"></c:url>
				<form:form method="POST" action="${actionUrl}">
					<input type="text" name="keyword" title="search"/>
					<input type="button" name="submit" value="Search">
				</form:form>
			</div>
		</div>
		<div id="main">
			<decorator:body />
		</div>
	</body>
</html>
