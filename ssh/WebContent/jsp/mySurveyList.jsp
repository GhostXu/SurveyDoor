<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.7.1.js" /> 
		<title>我的调查</title>
		<script type="text/javascript">
			$(function(){
				$("a[href*=delete]").click(function(){
					$(this).attr("disabled","disabled");
				});
			});
		</script>
	</head>
	<body>
		<s:if test="mySurveys.isEmpty() == true">目前您没有任何调查项!</s:if >
		<s:else>
			<table>
				<tr>
					<td colspan="10" style="height: 5px"></td>
				</tr>
				<tr>
					<td colspan="10" class="tdHeader">我的调查:</td>
				</tr>
				<tr>
					<td colspan="10" style="height: 5px"></td>
				</tr>
				<tr>
					<td class="tdListHeader">ID</td>
					<td class="tdListHeader">调查标题</td>
					<td class="tdListHeader">创建时间</td>
					<td class="tdListHeader">状态</td>
					<td class="tdListHeader">设计</td>
					<td class="tdListHeader">收集信息</td>
					<td class="tdListHeader">分析</td>
					<td class="tdListHeader">打开/关闭</td>
					<td class="tdListHeader">清除调查</td>
					<td class="tdListHeader">删除</td>
				</tr>
				<s:iterator value="mySurveys" >
					<s:set var="sId" value="id" />
					<tr>
						<td><s:property value="id" /></td>
						<td><s:property value="title" /></td>
						<td><s:date name="createTime" format="MM/dd/yy HH:mm" /></td>
						<td>
							<s:if test="closed">关闭</s:if>
							<s:else>打开</s:else>
						</td>
						<td><s:a action="surveyAction_designSurvey?sid=%{#sId}" namespace="/" cssClass="aList">设计</s:a></td>
						<td><s:a action="collectSurveyAction?sid=%{#sId}" namespace="/" cssClass="aList">收集信息</s:a></td>
						<td><s:a action="surveyAction_analyzeSurvey?sid=%{#sId}" namespace="/" cssClass="aList">分析</s:a></td>
						<td><s:a action="surveyAction_toggleStatus?sid=%{#sId}" namespace="/" cssClass="aList">打开/关闭</s:a></td>
						<td><s:a action="surveyAction_clearSurvey?sid=%{#sId}" namespace="/" cssClass="aList">清除调查</s:a></td>
						<td><s:a action="surveyAction_deleteSurvey?sid=%{#sId}" namespace="/" cssClass="aList">删除</s:a></td>
					</tr>
				</s:iterator>
			</table>
		</s:else>
	</body>
</html>