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
				<td class="tdHeader">增加Logo:</td>
			</tr>
			<tr>
				<td style="vertical-align: top;">
					<table>
						<tr>
							<td>
								<s:form action="surveyAction_doAddLogo" method="post" enctype="multipart/form-data">
								<s:hidden name="sid" />
								<table>
									<tr>
										<td class="tdFormLabel">选择Logo:</td>
										<td class="tdFormControl">
											<s:file name="logoPhoto" cssClass="text" />
											<s:fielderror fieldName="logoPhoto"></s:fielderror>
										</td>
									</tr>
									<tr>
										<td class="tdFormLabel"></td>
										<td class="tdFormControl"><s:submit value="%{'确定'}" cssClass="btn" /></td>
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