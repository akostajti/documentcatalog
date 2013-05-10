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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>


<div class="container-fluid">
	<div class="row-fluid">
		<div class="span4 offset4">

			<div id="login-error">${error}</div>

			<form action="<%=request.getContextPath()%>/j_spring_security_check" method="post" >
				<p>
					<label for="j_username"><spring:message code="userName.label" text="Username"/></label>
					<input id="j_username" name="j_username" type="text" />
				</p>
				<p>
					<label for="j_password"><spring:message code="password.label" text="Password"/></label>
					<input id="j_password" name="j_password" type="password" />
				</p>
				<p>
					<label for="j_remember"><spring:message code="rememberMe.label" text="Remember Me"/></label>
					<input id="j_remember" name="_spring_security_remember_me" type="checkbox" />
				</p>
				<spring:message code="signIn.label" text="Sign in" var="signInLabel"/>
				<input  type="submit" value="${signInLabel}" class="btn"/>
			</form>
		</div>
	</div>
</div>

	