<%@ page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ include file="taglibs.jsp"%>
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
  <link rel="stylesheet" type="text/css" href="<%=basePath%>statics/css/turnpage/paging.css"/>
  <link rel="stylesheet" type="text/css" href="<%=basePath%>statics/css/media-queries.css"/>
</head>
<body>




</center>

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
      <li><a><span>>流水信息</span></a></li>
      <li><a><span></span></a></li>
    </ol>
    <div class="time"><strong id="sj_time">&nbsp;&nbsp;</strong> <span id="sj_xq"></span></div>
  </div>
  <div class="container">
    <div class="attend_title4">

      <!--  >label for=""><input type="button" class="subm" value="添加公司" />&nbsp;&nbsp;|</label-->
      <label for="">&nbsp;节点名称:<input type="text" id="search_id" placeholder="请输入名称" /></label>
      <!-- label for="">&nbsp;公司url:<input type="text" placeholder="请输入url" /></label-->
      <label for=""><input type="submit" value="查询" id="search_ibeacon"/></label>

    </div>
    <div class="course_tab">
      <table class="tab4">

        <tr class="first">

          <th>uuid</th>
          <th>rssi</th>
          <th>type</th>

          <th>time</th>
          <th>mac</th>

        </tr>

      </table>
    </div>
    <div id="pageToolbar">
      <div class="pageToolbar_r4">
        <input type="button" value="提交"/>
      </div>
    </div>
  </div>
</div>
<!--js文件  -->
<script src="<%=basePath%>statics/js/jquery-1.7.2.min.js"></script>
<script src="<%=basePath%>statics/js/myjs.js"></script>
<script src="<%=basePath%>statics/date/time.js"></script>
<!--分页导航-->
<script type="text/javascript" src="<%=basePath%>statics/js/turnpage/jquery-1.11.2.js"></script>
<script type="text/javascript" src="<%=basePath%>statics/js/turnpage/query.js"></script>
<script type="text/javascript" src="<%=basePath%>statics/js/turnpage/paging.js"></script>
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
<!--分页导航-->
<script>
    $(function() {
        $('#pageTool').Paging(
            {
                pagesize : 10,
                count : 100,
                callback : function(page, size, count) {
                    console.log(arguments);
                    alert('当前第 ' + page + '页,每页 ' + size + '条,总页数：'
                        + count + '页')
                }
            });
        $('#pageToolbar').Paging({
            pagesize : 10,
            count : 85,
            toolbar : true
        });
    })
</script>


<script src="<%=path %>/statics/js/jquery-3.1.1.min.js" charset="utf-8"></script>
<script src="<%=path %>/statics/js/main.js" charset="utf-8"></script>

</body>
</html>

