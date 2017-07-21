<%@ page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ include file="taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="com.ibeacon.hibernate.Page" %>
<%@page import="java.util.*" %>
<%@page import="com.ibeacon.model.beacon.Ibeacon" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <title>定位管理系统</title>
    <link rel="stylesheet" href="<%=basePath%>statics/css/restcss.css" type="text/css">
    <link rel="stylesheet" href="<%=basePath%>statics/css/mystyle.css">
    <link rel="stylesheet" href="<%=basePath%>statics/css/font-awesome-4.7.0/css/font-awesome.css" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>statics/plugins/huatiao/css/style.css" />
    <script src="<%=path %>/statics/js/jquery-3.1.1.min.js" charset="utf-8"></script>
    <script type="text/javascript"
            src="<%=path %>/statics/plugins/date/WdatePicker.js"></script>
    <script src="<%=path %>/statics/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript"
            src="<%=path %>/statics/plugins/huatiao/js/jquery-ui-1.8.4.custom.min.js"></script>
    <script type="text/javascript"
            src="<%=path %>/statics/plugins/huatiao/js/jQuery.peSlider.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>statics/css/media-queries.css"/>

    <script src="<%=basePath%>/statics/js/common/jquery.js"></script>
    <script src="<%=basePath%>/statics/plugins/form/jquery.form.js"
            type="text/javascript"></script>
    <script src="<%=basePath%>/statics/js/myjs.js"></script>
    <!--<script src="date/time.js"></script>-->

</head>

<body>





<div id="header">
    <div class="logo">
        <h1><i class="fa fa-leaf"></i></h1>
        <p>定位管理系统</p>
    </div>
    <div class="sideNav" ><i class="fa fa-bars"></i></div>

    <div class="logo_r">

        <div class="user">
            <div class="user_photo">
                <img src="statics/images/userphoto.jpg" alt="" />
            </div>
            <h4 class="user_name">admin</h4>
            <div class="iconfont"><i class="fa fa-caret-down"></i></div>
            <ul class="dropmenu">
                <li><a ><i class="fa fa-user-o"></i><span>用户信息</span></a></li>
                <li><a ><i class="fa fa-lock"></i><span>权限管理</span></a></li>
                <li><a ><i class="fa fa-sign-out"></i><span>退出登录</span></a></li>
            </ul>
        </div>
    </div>
</div>

<div id="sidebar">
    <ul class="menu">
        <li class="first"><span>主菜单</span><i></i></li>
        <li class="menu-list current" >
            <a><i class="fa fa-address-book-o "></i><span>注册管理</span></a>
            <ul class="sub-menu-list">
                <li style="border-top: 1px solid #b9cad3;"><a href="manage/ibeacon_manage"><i class="fa fa-caret-right"></i><span>标签历史信息</span></a></li>
                <li><a href="index.jsp"><i class="fa fa-caret-right"></i><span>标签流水信息</span></a></li>
                <li><a href="nowibeacon.jsp"><i class="fa fa-caret-right"></i><span>标签最新消息</span></a></li>
                <li><a href="nodeconfig.jsp"><i class="fa fa-caret-right"></i><span>节点配置页面</span></a></li>
                <li><a href="nodeindex.jsp"><i class="fa fa-caret-right"></i><span>节点显示页面</span></a></li>
            </ul>
        </li>

    </ul>

</div>
<div id="main">
    <div class="breadcrumb">
        <ol class="breadcrumb_r">
            <li><a><i class="fa fa-home"></i><span>管理</span></a></li>
            <li><a><span>>历史信息</span></a></li>
            <li><a><span></span></a></li>
        </ol>
        <div class="time"><strong id="sj_time">&nbsp;&nbsp;</strong> <span id="sj_xq"></span></div>
    </div>
    <div class="container">
        <form action="manage/ibeacon_manage" method="post" autocomplete="off"
              id="mainForm">

            <div class="attend_title">
                <label for="">
                    <div id="map_search">

                        <div style="margin-left:10px;">
                            始时间:
                            <input type="text" id="start"
                                   onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',lang:'zh-cn'})" />
                        </div>
                        <div>
                            末时间:
                            <input type="text" id="end"
                                   onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',lang:'zh-cn'})" />
                        </div>
                        <div>
                            <input type="button" id="run" class="play" value="查询" />
                        </div>
                    </div>
                </label>
            </div>
            <div class="course_tab">
                <table class="tab4">
                    <thead>
                    <tr class="first">


                        <th>uuid</th>
                        <th>rssi</th>
                        <th>type</th>
                        <th>mac</th>
                        <th>time</th>

                    </tr>
                    </thead>

                </table>
                <div>


                </div>
            </div>
        </form>
    </div>
</div>
<!--js文件  -->
<script src="<%=basePath%>statics/js/jquery-1.7.2.min.js"></script>
<script src="<%=basePath%>statics/js/myjs.js"></script>
<script src="<%=basePath%>statics/date/time.js"></script>

<script>
    $(function() {
        $('.menu>li>a').click(
            function(e) {
                $(this).parent().addClass('current').siblings()
                    .removeClass('current')
            });

        $('.user').hover(function(e) {
            $('.dropmenu').stop().slideToggle(200)
        });

    })
</script>
<script type="text/javascript">





    // 状态标志
    var state_flag = 0;
    (function () {


        $('#run').click(function(){



            var start = $("#start").val();
            var end = $("#end").val();


            if (start == null || start == '' || end == null || end == '') {
                alert("请选择时间段");
                return;
            }

            // 向后台请求位置信息
            $.ajax({
                type : "post",// 请求方式
                url :  "<%=path %>/history/showmy",// 发送请求地址
                dataType : "json",
                data : {// 发送给数据库的数据

                    start: start,
                    end: end
                },
                // 请求成功后的回调函数有两个参数
                success : function(data, textStatus) {
                    $(".course_tab .tab4 tr:not('.first')").remove();
                    if(data != null && data.length != 0){
                        $.each(data,function(index, item) {
                            uuid = data[index].uuid,
                                rssi = data[index].rssi,
                                type = data[index].type,
                                mac = data[index].mac,
                                timelast = data[index].timelast;

                            $(".course_tab .tab4 .first").after("<tr><td>"+uuid+"</td><td>"+
                                rssi+"</td><td>"+
                                type +"</td><td>"+
                                mac+"</td><td>"+
                                timelast
                                +"<td></td></tr>");

                        });



                    }else{
                        alert("该时间段无数据！");

                    }
                }
            });


        });


        $('#to_index').click(function(){
            window.location.href = "index";
        });

        /* $("#pause").click(function(){
         if(animationId!=null){
         cancelAnimationFrame(animationId);
         }
         }) */

    }());

</script>



</body>
</html>
