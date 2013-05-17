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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib prefix="document" tagdir="/WEB-INF/tags/document" %>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span3">
			<!-- left sidebar -->
			<h2>${document.description}</h2>
			<div>
				<c:url var="userUrl" value="/profile/${document.uploader.username}"/>
				<spring:message code="uploader.label" text="Uploader"/>: <a href="${userUrl}" title="${document.uploader.username}">${document.uploader.username}</a>
			</div>

			<c:if test="${document.generatedSummary != null}">
				${document.generatedSummary}
			</c:if>
			<div>
				<spring:message code="tags.label" text="Tags"/>:
				<c:forEach items="${document.tags}" var="tag">
					<c:url var="tagUrl" value="/tags/${tag.name}"/>
					<span class="label"><a href="${tagUrl}" title="${tag.name}">${tag.name}</a></span>
				</c:forEach>
			</div>
			<c:if test="${!empty document.namedEntities}">
				<div>
					<h3><spring:message code="namedEntities.label" text="Named entities"/>:</h3> 
					<c:forEach items="${entityGroups}" var="group">
						<div>
							<h4><spring:message code="ner.${group.key}.label" text="${group.key}"/></h4>
							<c:forEach items="${group.value}" var="entity">
								${entity.name}, 
							</c:forEach>
						</div>
					</c:forEach>
				</div>
			</c:if>
			<div>
				<c:url var="downloadUrl" value="/document/${document.id}/download"/>
				<a href="${downloadUrl}" title="Download"><spring:message code="download.label" text="Download"/></a>
			</div>
			<div>
				<c:url var="imageUrl" value="/document/${document.id}/downloadSource"/>
				<a href="${imageUrl}" title="Download the source image"><spring:message code="download.source.label" text="Download source image"/></a>
			</div>
		</div>
		<div class="span9">
			<document:preview documentId="${document.id}" />
		</div>
	</div>
</div>

