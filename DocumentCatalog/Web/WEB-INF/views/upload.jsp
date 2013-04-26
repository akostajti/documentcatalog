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

<form:form method="POST" modelAttribute="uploadForm" enctype="multipart/form-data">
	<div>
		<label><spring:message code="description.label" text="Description"/>:</label><form:input path="description"/>
	</div>
	<div>
		<label><spring:message code="comment.label" text="Comment"/>:</label><form:textarea path="comment"/>
	</div>
	<div>
		<label for="files"><spring:message code="select.files.label" text="Select files"/>:</label><input type="file" name="files[0]"/>
	</div>
	<div>
		<label for="merge"><spring:message code="upload.merge.files.label" text="Merge result into a single file"/></label><form:checkbox path="merge"/>
	</div>
	<div>
		<label for="tags"><spring:message code="tags.label" text="Tags"/>:</label><form:input path="tags"/>
	</div>
	<div>
		<form:button name="submit" value="submit"><spring:message code="upload.label" text="Upload"/></form:button>
	</div>
</form:form>

