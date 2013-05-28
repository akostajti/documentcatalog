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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type='text/javascript' src='<c:url value="/js/jquery-fileupload/jquery.ui.widget.js"/>'></script>
<script type='text/javascript' src='<c:url value="/js/jquery-fileupload/jquery.iframe-transport.js"/>'></script>
<script type='text/javascript' src='<c:url value="/js/jquery-fileupload/jquery.fileupload.js"/>'></script>
<script type='text/javascript' src='<c:url value="/js/jquery-ui/jquery-ui.custom.min.js"/>'></script>

<c:url value="/upload" var="submitUrl"/>
<c:url value="/file" var="fileUploadUrl"/>

<script type="text/javascript">
	$(document).ready(function () {
		$('#uploadForm').submit(function(event) {
			// don't submit the form on submit event
			event.preventDefault();
			$.post('${submitUrl}', {
					"tags": $('#tags').val(),
					"description": $('#description').val(),
					"comment": $('#comment').val(),
					"merge": $('#merge').val(),
					"files": getFilelist()
				},
				function(result) {
					if (result.success == true) {
						console.log('Success', 'Files have been uploaded!');
					} else {
						console.log('Failure', 'Unable to upload files!');
					}
			}, "json");
		});

		$("#files").fileupload({
			"dataType": "json",
			"done": function (e, data) {
				$.each(data.result, function (index, file) {
					var $file = $("<div>").addClass("thumbnail").append($("<img>").attr("src", file.downloadUrl));
					$file.data("info", file);
					//$("body").data('filelist').push(file);
					$("#fileList").append($file);
					/*$('#attach').empty().append('Add another file');*/
				});
				$("#fileList").sortable({
					"containment": "#fileList"
				});

			}
		});

		$("body").data("filelist", new Array());
	});

	function getFilelist() {
		var files = [];
		var filenames = "";
		$("#fileList .thumbnail").each(function () {
			files.push($(this).data("info"));
		});
		for (var i = 0; i < files.length; i < i++) {
			var suffix = (i == files.length - 1) ? "" : ",";
			filenames += files[i].name + suffix;
		}
		return filenames;
	}
</script>

<div class="row-fluid">
	<div class="span4 offset4">
		<form:form method="POST" id="uploadForm" modelAttribute="uploadForm" enctype="multipart/form-data">
			<div>
				<label><spring:message code="description.label" text="Description"/>:</label><form:input path="description" id="description"/>
			</div>
			<div>
				<label><spring:message code="comment.label" text="Comment"/>:</label><form:textarea path="comment" id="comment"/>
			</div>
			<div>
				<label for="merge"><spring:message code="upload.merge.files.label" text="Merge result into a single file"/></label><form:checkbox path="merge" id="merge"/>
			</div>
			<div>
				<label for="tags"><spring:message code="tags.label" text="Tags"/>:</label><form:input path="tags" tags="tags"/>
			</div>
			<div>
				<span class="btn btn-success fileinput-button">
					<span><spring:message code="select.files.label" text="Select files"/></span>
					<input type="file" name="files" multiple="multiple"
					style="opacity: 0; filter:alpha(opacity: 0);" data-url="${fileUploadUrl}" id="files"/>
				</span>
			</div>
			<div id="fileList">
				
			</div>
			<div>
				<form:button name="submit" value="submit" class="btn btn-primary"><spring:message code="upload.label" text="Upload"/></form:button>
			</div>
		</form:form>
	</div>
</div>

