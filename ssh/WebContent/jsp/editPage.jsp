<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>增加/编辑页内容</title>
	</head>
	<body>
		<table>
			<tr>
				<td class="tdHeader">增加/编辑页内容:</td>
			</tr>
			<tr>
				<td style="vertical-align: top;">
					<table>
						<tr>
							<td>
								<s:form action="pageAction_saveOrUpdatePage" namespace="/" method="post">
								<s:hidden name="id" />
								<s:hidden name="sid" />
								<table>
									<tr>
										<td class="tdFormLabel">页面标题:</td>
										<td class="tdFormControl"><s:textfield name="title" cssClass="text" /></td>
									</tr>
									<tr>
										<td class="tdFormLabel">页面描述:</td>
										<td class="tdFormControl"><s:textarea name="description" cssClass="text" cols="20" rows="8"/></td>
									</tr>
									<tr>
										<td class="tdFormLabel"></td>
										<td class="tdFormControl"><s:submit value="确定" cssClass="btn" /></td>
									</tr>
								</table>
								</s:form>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>