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
    <style>
        .scene {
            width: 100%;
            background: url(<%=path %>/statics/imgs/background.jpg) no-repeat 0px 0px;
            background-size: contain;
        }
    </style>
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
    <canvas id='canvas'></canvas>
</div>
<div id="config">

    <div>
        <input type="button" id="show_log" value="显示日志" />
    </div>
    <div>
        <input type="button" id="show_node" value="隐藏节点" />
    </div>
    <div>
        <input type="button" id="to_history" value="历史轨迹" />
    </div>
    <div>
        <input type="button" id="to_file" value="导出文件" />
    </div>
    <div>	 <input type="button" id="show_history" value="标签历史信息" onclick="window.location.href='manage/ibeacon_manage'"></div>
    <div>	 <input type="button" id="show_flow" value="标签流水信息" onclick="window.location.href='index.jsp'"></div>
    <div>	 <input type="button" id="show_new" value="标签最新消息" onclick="window.location.href='nowibeacon.jsp'"></div>
    <div>	 <input type="button" id="show_config" value="节点配置页面" onclick="window.location.href='nodeconfig.jsp'"></div>

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
