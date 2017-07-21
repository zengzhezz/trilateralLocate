<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    <title>演示页面</title>
    <link rel="stylesheet" type="text/css" href="<%=path %>/statics/css/restcss.css">
    <link rel="stylesheet" type="text/css" href="<%=path %>/statics/css/main.css">
  </head>
  <body width="150%">
  	<div id="header">
  		<div class='title'>
			慧联人员定位平台
		</div>
		<!-- <div>
			<input type="button" id="to_config" value="配置界面" />
		</div> -->
		
  	</div>
  	<div class="scene">
      <img src="<%=path %>/statics/imgs/background.jpg" alt="">
    </div>
    <div id="config">
    	<div class="checkbox">
    		选择要显示的类型:
    		<label><input id="show_man" type="checkbox" value="" checked/>安保人员 </label> 
			<label><input id="show_car" type="checkbox" value="" checked/>巡逻车辆 </label> 
			<label><input id="show_dog" type="checkbox" value="" checked/>犬类 </label> 
			<label><input id="show_thief" type="checkbox" value="" checked/>重点人员 </label> 
			<label><input id="show_bike" type="checkbox" value="" checked/>电动车 </label> 
    	</div>
    	<div>
			<input type="button" id="show_log" value="显示日志" />
		</div>
    	<div>
			<input type="button" id="show_node" value="显示节点" />
		</div>
		<div>
			<input type="button" id="to_history" value="历史轨迹" />
		</div>
    </div>
    <div class="flowmsg">
    	<table>
	        <tr class='first'>
	          <td>时间</td>
	          <td>uuid</td>
	          <td>mac数</td>
	          <td>mac</td>
	          <td>距离</td>
	          <td>mac</td>
	          <td>距离</td>
	        </tr>
      </table>
    </div>
    <!-- <div id="alarm_wrap">
    	<div class="hint">
    		<div class="trai"></div>
    		<div class="message_wrap">
    			<p>非法人员</p>
    			<p>0001, 1号宝宝</p>
    			<p>0003, 3号宝宝</p>
    		</div>
    	</div>
    </div> -->
    <audio id="alarmMusic" src="<%=basePath %>/statics/sounds/alarm_sound.wav"></audio>
    <script src="<%=path %>/statics/js/jquery-3.1.1.min.js" charset="utf-8"></script>
    <script src="<%=path %>/statics/js/mainsecond.js" charset="utf-8"></script>
  </body>  
</html>
