<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>矩阵型问题设计</title>
	</head>
	<body>
		<s:form action="questionAction_saveOrUpdateQuestion.action" method="post">
		<s:hidden name="id" />
		<s:hidden name="questionType" />
		<s:hidden name="pid" />
		<s:hidden name="sid" />
		<table>
			<tr>
				<td colspan="2" class="tdQHeaderL">矩阵型问题设计:</td>
			</tr>
			<tr>
				<td width="35%" style="text-align: right;">问题标题:</td>
				<td width="*" style="text-align: left;"><s:textfield name="title" cssClass="text" /></td>
			</tr>
			<tr>
				<td style="text-align: right;vertical-align: top;">行标题标签组:</td>
				<td width="*" style="text-align: left;"><s:textarea cols="41" rows="8" name="matrixRowTitles" /></td>
			</tr>
			<tr>
				<td style="text-align: right;vertical-align: top;">列标题标签组:</td>
				<td width="*" style="text-align: left;"><s:textarea cols="41" rows="8" name="matrixColTitles" /></td>
			</tr>
			<tr>
				<td style="text-align: right;vertical-align: top;">[下拉列表选项集合]:</td>
				<td width="*" style="text-align: left;"><s:textarea cols="41" rows="8" name="matrixSelectOptions"/></td>
			</tr>
			<tr>
				<td style="text-align: right;"></td>
				<td width="*" style="text-align: left;"><input type="submit" name="ok" value="确定" class="btn"></td>
			</tr>
		</table>
		</s:form>
	</body>
</html>