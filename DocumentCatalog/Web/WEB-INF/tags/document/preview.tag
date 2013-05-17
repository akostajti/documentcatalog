<%--
Copyright by Akos Tajti (akos.tajti@gmail.com)

All rights reserved.

This software is the confidential and proprietary information
of Akos Tajti. ("Confidential Information"). You
shall not disclose such Confidential Information and shall use
it only in accordance with the terms of the license agreement
you entered into with Akos Tajti.
 --%>

<%--
This tag creates a pageable preview for a document.
 --%>
<%@ tag language="java" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<%@ attribute name="documentId" required="true" description="The id of the document to preview" %>


<script type="text/javascript" src="http://github.com/malsup/media/raw/master/jquery.media.js?v0.92"></script> 
<script type="text/javascript" src="//cdn.jsdelivr.net/jquery.metadata/2.0/jquery.metadata.js"></script> 

<div class="preview">
	<script type="text/javascript">
		var width = $(".preview").parent().width();
		var height = $(window).height() - 140;;

		$(document).ready(function () {
			$('a.media').media({"width": width, "height": height});
		});
	</script>

	<c:url var="embedUrl" value="/document/${document.id}/embed.pdf"/>

	<a class="media" href="${embedUrl}"></a>
</div>


