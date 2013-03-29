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
<div>
	<c:url var="imageUrl" value="/document/${document.id}/downloadSource"/>
	<a href="${imageUrl}" title="Download the source image">Download the source image</a>
</div>
<div class="pdf" style="height: 80%;">
	<c:url var="embedUrl" value="/document/${document.id}/embed"/>
	<object data="${embedUrl}#toolbar=1&amp;navpanes=0&amp;scrollbar=1&amp;page=1&amp;view=FitH" type="application/pdf" width="100%" height="100%">
		<p>It appears you don't have a PDF plugin for this browser. No biggie... you can download the file using the link above.</p>
	</object>
</div>