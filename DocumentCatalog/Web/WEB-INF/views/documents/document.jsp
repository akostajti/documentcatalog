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

<script src="<c:url value="/js/document-viewer/public/assets/viewer.js"/>" type="text/javascript" charset="utf-8"></script>
<script src="<c:url value="/js/document-viewer/public/assets/templates.js"/>" type="text/javascript" charset="utf-8"></script>

<h2>${document.description}</h2>
<div style="float:left; width: 15%;">
	<div>
		Tags:
		<c:forEach items="${document.tags}" var="tag">
			<c:url var="tagUrl" value="/tags/${tag.name}"/>
			<a href="${tagUrl}" title="${tag.name}">${tag.name}</a>
		</c:forEach>
	</div>
	<c:if test="${!empty document.namedEntities}">
		<div>
			Named entities: 
			<c:forEach items="${document.namedEntities}" var="entity">
				${entity.name},
			</c:forEach>
		</div>
	</c:if>
	<div>
		<c:url var="downloadUrl" value="/document/${document.id}/download"/>
		<a href="${downloadUrl}" title="Download">Download</a>
	</div>
	<div>
		<c:url var="imageUrl" value="/document/${document.id}/downloadSource"/>
		<a href="${imageUrl}" title="Download the source image">Download the source image</a>
	</div>
</div>
<div class="pdf" style="width: 80%; height: 60%; float: right;">
	<c:url var="embedUrl" value="/document/${document.id}/embed"/>
	<object data="${embedUrl}#toolbar=1&amp;navpanes=0&amp;scrollbar=1&amp;page=1&amp;view=FitH" type="application/pdf" width="100%" height="100%">
		<p>It appears you don't have a PDF plugin for this browser. No biggie... you can download the file using the link above.</p>
	</object>
</div>

