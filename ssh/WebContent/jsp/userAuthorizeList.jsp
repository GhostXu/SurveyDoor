<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>用户权限管理</title>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.7.1.js"></script>
		<script type="text/javascript">
			$(function(){
				$('tbody > tr:even').css('background-color','rgb(240,240,240)');
			});
		</script>
	</head>
	<body>
		<table>
			<tr>
				<td colspan="10" style="height: 5px"></td>
			</tr>
			<tr>
				<td colspan="10" style="height: 5px"></td>
			</tr>
		</table>
		<s:if test="allUsers.isEmpty() == true">目前没有任何用户!</s:if >
		<s:else>
			<table>
				<thead>
					<tr>
						<td colspan="10" style="height: 5px"></td>
					</tr>
					<tr>
						<td colspan="10" class="tdHeader">授权管理:</td>
					</tr>
					<tr>
						<td colspan="10" style="height: 5px"></td>
					</tr>
					<tr>
						<td class="tdListHeader">序号</td>
						<td class="tdListHeader">ID</td>
						<td class="tdListHeader">email</td>
						<td class="tdListHeader">昵称</td>
						<td class="tdListHeader">修改授权</td>
						<td class="tdListHeader">清除授权</td>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="allUsers" status="st">
						<s:set var="userId" value="uid" />
						<tr>
							<td><s:property value="#st.count" /></td>
							<td><s:property value="uid" /></td>
							<td style="text-align: left;"><s:property value="email" /></td>
							<td style="color: gray;text-align: left;"><s:property value="nickName" /></td>
							<td><s:a action="userAuthorizeAction_editAuthorize?userId=%{#userId}" cssClass="aList">修改授权</s:a></td>
							<td><s:a action="userAuthorizeAction_clearAuthorize?userId=%{#userId}" cssClass="aList">清除授权</s:a></td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</s:else>
	</body>
</html>