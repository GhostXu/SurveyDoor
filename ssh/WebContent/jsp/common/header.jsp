<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<div class="divOuterFrame">
	<div class="divInnerFrame">欢迎使用SurveyDoor调查系统!</div>
</div>
<div class="divWhiteLine"></div>
<div class="divNavigatorOuterFrame">
	<div class="divNavigatorInnerFrame">
		<s:a action="loginAction_toLoginPage">[首页]</s:a>&nbsp;
		<s:a action="surveyAction_newSurvey">[新建调查]</s:a>&nbsp;
		<s:a action="surveyAction_mySurveys">[我的调查]</s:a>&nbsp;
		<s:a action="engageSurveyAction_review?sid=33">[每日一面]</s:a>&nbsp;
		<s:a action="engageSurveyAction_findAllAvailableSurveys">[参与调查]</s:a>&nbsp;
		<s:a action="userAction_goRegist">[用户注册]</s:a>&nbsp;
		<s:a action="userAuthorizeAction_findAllUsers">[用户授权管理]</s:a>&nbsp;
		<s:a action="roleAction_findAllRoles">[角色管理]</s:a>&nbsp;
		<s:a action="rightAction_findAllRights">[权限管理]</s:a>&nbsp;
		<s:a action="logAction_findAllLogs">[日志管理]</s:a>&nbsp;
	</div>
</div>
<div class="divWhiteLine"></div>