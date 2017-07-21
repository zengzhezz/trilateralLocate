<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<!DOCTYPE html>
<html >
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<title>tracker展示平台</title>
<link rel="stylesheet" href="${ctx }/statics/css/restcss.css"
	type="text/css">
<link rel="stylesheet" href="${ctx }/statics/css/mystyle.css">
<link rel="stylesheet"
	href="${ctx }/statics/css/font-awesome-4.7.0/css/font-awesome.css"
	type="text/css" />
<link rel="stylesheet" type="text/css"
	href="${ctx }/statics/css/media-queries.css" />
<script src="${ctx }/statics/js/common/jquery-1.7.2.min.js"></script>
<script src="${ctx }/statics/js/myjs.js"></script>

<script type="text/javascript"
	src="${ctx}/statics/js/common/time.js"></script>
<!--<script src="date/time.js"></script>-->


<script>
	$(document).ready(
			function() {
			/* 头导航颜色变化 */
				 $(".nbavbar_ul li").click(
						function() {
							$(".nbavbar_ul li:not(this)").css(
									"background-color", "#208979");
							$(this).toggleClass('current').css(
									"background-color", "#4ea99b");
						});
 
				var myDate = new Date();
				//获取当前小时数(0-23)
				var hourString = myDate.getHours();
				//获取当前分钟数(0-59)  
				var minutesString = myDate.getMinutes();
				if (minutesString < 9) {
					minutesString = "0" + minutesString;
				}
				//获取当前秒数(0-59)  
				var secondString = myDate.getSeconds();
				if (secondString < 9) {
					secondString = "0" + secondString;
				}
				$("#mytime").html(
						hourString + ":" + minutesString + ":" + secondString);

				//获取当前日期
				var dateString = myDate.toLocaleDateString();

				//获取当前星期X(0-6,0代表星期天)
				var day = myDate.getDay();
				var dayString;
				if (day == "0") {
					dayString = "星期天"
				} else if (day == "1") {
					dayString = "星期1"
				} else if (day == "2") {
					dayString = "星期2"
				} else if (day == "3") {
					dayString = "星期3"
				} else if (day == "4") {
					dayString = "星期4"
				} else if (day == "5") {
					dayString = "星期5"
				} else if (day == "6") {
					dayString = "星期6"
				}
				$("#dateSpan").html(dateString + dayString);

			});
	function usermanage(){
	$(".nbavbar_ul li").css("background-color", "#208979");
	}

	$(function() {
		$('.menu>li>a').click(
				function(e) {
					$(this).parent().addClass('current').siblings()
							.removeClass('current')
				});

		$('.user').hover(function(e) {
			$('.dropmenu2').stop().slideToggle(200)
		});

	})
</script>
<script type="text/javascript">
	function refresh(id, type, url, dataType) {
		$.ajax({
			type : type,
			dataType : dataType,
			url : url,
			timeout : 5000,
			async : false, //参数async设为false就为同步调用 当ajax返回结果后程序才继续执行
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert("连接超时");
			},
			success : function(data) {
				var element = "#" + id;
				$(element).html(data);
			}
		});
	}
</script>

<!--用户管理创建-->
<script>
	$(function() {
		$('.fa-pencil,.btn1,.close_btn').click(function() {
			$('#outside').fadeToggle(0)
		})

	})
</script>

</head>
<body>
	<div id="header">
		<div class="logo">
			<h1>
				<i class="fa fa-pagelines"></i>
			</h1>
			<p>tracker展示平台</p>
		</div>
		<div class="sideNav">
			<i class="fa fa-bars"></i>
		</div>
		<div class="navbar">
			<ul class="nbavbar_ul">
				<li class="specill"><i class="fa fa-github-square"></i> <a
					href="${ctx }/monitoring/alllocation" target="mainFrame">定位监控</a></li>
				<li><i class="fa fa-gears"></i> <a
					href="${ctx }/device/manage" target="mainFrame">设备管理</a></li>
			</ul>
		</div>
		<div class="logo_r">
			<div class="time">
				<!--  <strong id="mytime"></strong><br> <span id="dateSpan"></span>-->
				<strong id="sj_time"></strong><span id="sj_xq"></span>
			</div>
			<div class="user">
				<div class="user_photo">
					<img src="${ctx }/statics/images/user.png" alt="" />
				</div>
				<h4 class="user_name">${username }</h4>
				<div class="iconfont">
					<i class="fa fa-caret-down"></i>
				</div>
				<ul class="dropmenu2">
					<li><a href="${ctx }/user/list" target="mainFrame" onclick="usermanage()"><i class="fa fa-lock"></i><span>用户管理</span></a></li>
					<li><a href="${ctx }/singleout"><i class="fa fa-sign-out"></i><span>退出登录</span></a></li>
				</ul>
			</div>

		</div>
	</div>
<div id = "downheadermain">
	<iframe src="${ctx}/monitoring/alllocation" id="mainFrame" name="mainFrame"
			marginwidth="0" marginheight="0" border="0" scrolling="auto"
			frameborder="0" style="height:100%;width:100%"> </iframe>
</div>
</body>
</html>



