<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>日志管理</title>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.7.1.js"></script>
		<script type="text/javascript">
		$(function(){
			$("input[name^=r_]").parent().click(function(){
				$(this).children("input[name^=r_]").removeAttr("disabled");
				if($(this).children("input").attr("value") == "未命名"){
					$(this).children("input").attr("value","");
				}
				$(this).children("input[name^=r_]").focus();
			});	
			$("input[name^=r_]").attr("disabled","disabled");
		});
		
		//全选
		$(function(){
			$('#cbSelectAll').click(function(){
				var v = $(this).attr("checked");
				if(v == "checked"){
					$("input[type=checkbox]").attr("checked","checked");
				}
				else{
					$("input[type=checkbox]").removeAttr("checked");
				}
			});
			
			$('#inverseSelectAll').click(function(index){
				$("input[type=checkbox]").each(function(){
					var v = $(this).attr("checked");
					if("checked" == v){
						$(this).removeAttr("checked");
					}
					else{
						$(this).attr("checked","checked");
					}
				});
			});
		});
		</script>
	</head>
	<body>
		<table>
			<tr>
				<td colspan="10" style="height: 5px"></td>
			</tr>
			<tr>
				<td colspan="10" class="tdPHeaderR"><s:a action="rightAction_toAddRightPage" namespace="/">添加权限</s:a></td>
			</tr>
			<tr>
				<td colspan="10" style="height: 5px"></td>
			</tr>
		</table>
		<s:if test="allLogs.isEmpty() == true">没有日志!</s:if >
		<s:else>
			<table>
				<thead>
					<tr>
						<td colspan="10" style="height: 5px"></td>
					</tr>
					<tr>
						<td colspan="10" class="tdHeader">权限管理:</td>
					</tr>
					<tr>
						<td class="tdListHeader">操作人</td>
						<td class="tdListHeader">操作名称</td>
						<td class="tdListHeader">参数</td>
						<td class="tdListHeader">操作结果</td>
						<td class="tdListHeader">消息</td>
						<td class="tdListHeader">时间</td>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="allLogs" status="st">
						<tr>
							<td>
								<s:property value="operator" />
							</td>
							<td>
								<s:property value="operName" />
							</td>
							<td>
								<span title='<s:property value="operParams"/>'><s:property value="@cn.util.StringUtil@getDescString(operParams)"/></span>
							</td>
							<td>
								<s:property value="operResult"/>
							</td>
							<td>
								<span title='<s:property value="resultMsg"/>'><s:property value="@cn.util.StringUtil@getDescString(resultMsg)"/></span>
							</td>
							<td>
								<s:date name="operTime" format="yy/MM/dd hh:mm:ss"/>
							</td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</s:else>
	</body>
</html>