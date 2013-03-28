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

<h2>Document ${document.id}</h2>
<div class="description">
	${document.description}
</div>
<div>
	Uploaded from ${document.source}
</div>
<div>
	<c:url var="downloadUrl" value="/document/${document.id}/download"/>
	<a href="${downloadUrl}" title="Download">Download</a>
</div>