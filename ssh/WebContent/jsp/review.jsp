<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>参与调查</title>
	</head>
	<body>
		<s:form action="engageSurveyAction_doEngageSurvey" method="post" >
			<s:hidden name="currPid" value="%{currPage.id}" />
			<table>
				<tr>
					<td colspan="2" class="tdWhiteLine"></td>
				</tr>
				<tr>
					<!-- 输出调查标题 -->
					<td colspan="2" class="tdHeader"><s:property value="#session.current_survey.title" /></td>
				</tr>
				<tr>
					<td colspan="2" class="tdWhiteLine"></td>
				</tr>
				<tr>
					<!-- 页面标题 -->
					<td colspan="2" class="tdPHeaderL"><s:property value="currPage.title" /></td>
				</tr>
				<tr>
					<td width="30px"></td>
					<td>
						<table>
							<!-- 遍历问题集合 -->
							<s:iterator var="q" value="currPage.questions">
								<tr>
									<td class="tdQHeaderL"><s:property value="#q.title"/></td>
								</tr>
								<tr>
									<td class="tdOptionArea">
										<input type="text" 
												name='q<s:property value="#qId" />'
												<s:property value="setText('q' + #qId)" />
												>
									</td>
								</tr>
							</s:iterator>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<!-- 构造上一步按钮 -->
						<s:if test="currPage.orderno != #session.current_survey.minOrderno">
							<input type="submit" name='submit_pre' value='<s:property value="#session.current_survey.preText"/>' class="btn">
						</s:if>
						<!-- 构造下一步按钮 -->
						<s:if test="currPage.orderno != #session.current_survey.maxOrderno">
							<input type="submit" name='submit_next' value='<s:property value="#session.current_survey.nextText"/>' class="btn">
						</s:if>
						<!-- 构造完成按钮 -->
						<s:if test="currPage.orderno == #session.current_survey.maxOrderno">
							<input type="submit" name="submit_done" value='<s:property value="#session.current_survey.doneText"/>' class="btn">
						</s:if>
						<input type="submit" name="submit_exit" value='<s:property value="#session.current_survey.exitText"/>' class="btn">
					</td>
				</tr>
			</table>
			</s:form>
	</body>
</html>