<%--
Copyright by Akos Tajti (akos.tajti@gmail.com)

All rights reserved.

This software is the confidential and proprietary information
of Akos Tajti. ("Confidential Information"). You
shall not disclose such Confidential Information and shall use
it only in accordance with the terms of the license agreement
you entered into with Akos Tajti.
 --%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<form:form method="POST" modelAttribute="registerUserForm">
	<form:errors/>
	<div>
		Username: <form:input path="username"/>
	</div>
	<div>
		Password: <form:password path="password"/>
	</div>
	<form:button name="SUBMIT" value="Register"><spring:message code="register.label"/></form:button>
</form:form>
