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

<form:form method="POST" modelAttribute="uploadForm" enctype="multipart/form-data">
	<div>
		<label>Description:</label><form:input path="description"/>
	</div>
	<div>
		<label>Comment:</label><form:textarea path="comment"/>
	</div>
	<div>
		<label for="files">Select files:</label><input type="file" name="files[0]"/>
	</div>
	<div>
		<label for="merge">Merge result into a single file</label><form:checkbox path="merge"/>
	</div>
	<div>
		<label for="tags">Tags:</label><form:input path="tags"/>
	</div>
	<div>
		<form:button name="submit" value="submit">Submit</form:button>
	</div>
</form:form>
