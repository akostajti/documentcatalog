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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span4 offset4">
			<form:form method="POST" modelAttribute="registerUserForm">
				<form:errors/>
				<div>
					<spring:message code="userName.label" text="Username"/>: <form:input path="username"/>
				</div>
				<div>
					<spring:message code="password.label" text="Password"/>: <form:password path="password"/>
				</div>
				<form:button name="SUBMIT" value="Register" class="btn"><spring:message code="register.label" text="Register"/></form:button>
			</form:form>
		</div>
	</div>
</div>

