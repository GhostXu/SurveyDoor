<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>设计调查</title>
		<script type="text/javascript" src="<s:url value="/js/jquery-1.7.1.js" />"></script>
		<script type="text/javascript" src="<s:url value="/js/echarts-all.js" />"></script>
	</head>
	<body>
		<s:set var="sId" value="id" />
		<table>
			<tr>
				<td colspan="2" class="tdWhiteLine"></td>
			</tr>
			<tr>
				<td colspan="2" class="tdHeader">分析调查:</td>
			</tr>
			<tr>
				<td colspan="2" class="tdWhiteLine"></td>
			</tr>
			<tr>
				<td colspan="2" class="tdHeader"><s:property value="title" /></td>
			</tr>
			<tr>
				<td colspan="2" class="tdWhiteLine"></td>
				</tr>
		</table>
		<div id="main" style="height:300px; width:300px;"></div>
	</body>
</html>
<script>
	var result = ${option};
$(function(){
	if(result.legend != undefined && result.legend != null)	{
		var chart = echarts.init(document.getElementById('main'));
		chart.setOption(result);
	}	
});
	
</script>