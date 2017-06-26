<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>导入调查</title>

<link rel="stylesheet" href="${pageContext.request.contextPath }/css/import.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/styles.css" />
 <script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.7.1.js" />
</head>
<script type="text/javascript">
function upload(){
	var filePath = $('#file').val();
	$.post("surveyAction_uploadXls",null,function(data){
		
	});
}
</script>
<body>
	<div>
	<s:include value="common/header.jsp" />
		<table>
			<tr class="trClass">
				<td class="tdClass"><label>模  板 </label></td>
				<td border="0px"><s:a action="surveyAction_download" cssClass="saClass">模板下载</s:a></td>
			</tr>
			<tr class="trClass2">
				<td class="tdClass"><label>导入模板</label></td>
				<td border="0px"><s:file id="file" name="file" cssClass="saClass"></s:file></td>
				<td border="0px"><input type="button" onclick="upload();" value="导入" ></td>
			</tr>
		</table>
	</div>
</body>
</html>