<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>历史轨迹</title>
    <link rel="stylesheet" type="text/css" href="<%=path %>/statics/css/restcss.css">
    <link rel="stylesheet" type="text/css"
          href="<%=path %>/statics/plugins/huatiao/css/style.css" />
    <style type="text/css">
        body, html {
            width: 100%;
            height: 100%;
            margin: 0;
            font-family: "微软雅黑";
        }
        #map_search {
            height: 50px;
            line-height: 50px;
            background: #edf6fa;
        }
        #map_search div input[type="text"]{
            border: 1px solid #d4d4d4;
            padding: 6px 0 6px 4px;
            box-sizing: border-box;
        }
        #map_search div select{
            width: 120px;
        }
        #map_search div {
            float: left;
            margin-left: 6px;
        }
        #map_search div input[type="button"] {
            padding: 4px 16px;
            border-radius: 4px;
            background: #379b8c;
            color: #fff;
            cursor: pointer;
        }
        #map_search div input[type="button"]:hover {
            background: #268878;
        }
        .scene {
            width: 100%;
            background: url(<%=path %>/statics/imgs/background.jpg) no-repeat 0px 0px;
            background-size: contain;
        }
        #canvas {
            width: 100%;
        }
        #label {
            position: absolute;
            height: auto;
            top: -50px;
            left: -50px;
        }
        .label_msg_wrapper {
            padding: 0px;
            margin: 0px;
            position: relative;
            width: 130px;
            left: -55px;
            top: 1px;
        }
        #label img{
            display: block;
        }

        .label_msg_wrapper .label_p {
            padding: 0px 3px;
            background: white;
            border: 1px solid grey;
            border-radius: 4px;
        }

        .label_msg_wrapper p{
            margin: 0px;
            font-size: 0.8em;
        }

        .label_msg_wrapper .trai {
            postion: absolute;
            width: 0;
            height: 0;
            border-left: 7px solid transparent;
            border-right: 7px solid transparent;
            border-bottom: 5px solid grey;
            margin: 0 auto;
        }

        #header {
            height: 50px;
            line-height: 50px;
            background: #edf6fa;
        }

        #header .title{
            margin: 0 auto;
            text-align: center;
            font-size: 1.8em;
            letter-spacing: .2em;
            font-weight: 400;
        }
    </style>
</head>
<body width="150%">
<div id="header">
    <div class='title'>
        慧联人员定位平台
    </div>
</div>
<div class="scene">
    <canvas id='canvas'></canvas>
</div>
<div id="label"><img src="<%=path %>/statics/imgs/man.png" width="20px;" id='label_img'><div class='label_msg_wrapper'><div class="trai"></div><div class='label_p'></div></div></div>
<div id="map_search">
    <div style="margin-left:10px;">
        uuid:
        <select id="search_uuid"></select>
    </div>
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
        <input type="button" id="run" class="play" value="播放" />
    </div>
    <div>
        <input type="button" id="pause" class="pause" value="暂停" />
    </div>
    <div>
        <input type="button" id="clear" class="clear" value="清屏" />
    </div>
    <div>
        <input type="button" id="to_index" value="显示界面" />
    </div>
    <div>
        <label for="speed">速度:</label>
        <input type="number" name="speed" id="speed" value="3" min="1"
               max="10" size="1"/>
    </div>
</div>
<script src="<%=path %>/statics/js/jquery-3.1.1.min.js" charset="utf-8"></script>
<script type="text/javascript"
        src="<%=path %>/statics/plugins/date/WdatePicker.js"></script>
<script src="<%=path %>/statics/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
        src="<%=path %>/statics/plugins/huatiao/js/jquery-ui-1.8.4.custom.min.js"></script>
<script type="text/javascript"
        src="<%=path %>/statics/plugins/huatiao/js/jQuery.peSlider.js"></script>
<script type="text/javascript">
    $(function() {
        //创建输入滑杆
        $('input#speed').peSlider({
            range : 'min'
        });
    });
</script>
<script type="text/javascript">

    var image_ratio = 0.367475;
    var image_width = document.body.clientWidth;
    var image_height = image_width * image_ratio;
    var movespeed;
    var canvas = document.getElementById("canvas");
    canvas.width = image_width;
    canvas.height = image_height;
    // 显示的图片类型
    var image_type = ["man","dog","car","thief","bike"];
    /* // 标签的类型，默认为0
     var labelType = 0;
     // 该人员的描述信息
     var description; */
    var my_label = {};
    var ctx = canvas.getContext("2d");
    ctx.scale(1, 1);
    ctx.lineCap = "round";
    // variable to hold how many frames have elapsed in the animation
    var t = 1;
    // define the path to plot
    var vertices = [];
    var points = [];
    var animationId = null;
    // 状态标志
    var state_flag = 0;
    (function () {
        var lastTime = 0;
        var vendors = ['ms', 'moz', 'webkit', 'o'];
        for (var x = 0; x < vendors.length && !window.requestAnimationFrame; ++x) {
            window.requestAnimationFrame = window[vendors[x] + 'RequestAnimationFrame'];
            window.cancelAnimationFrame = window[vendors[x] + 'CancelAnimationFrame'] || window[vendors[x] + 'CancelRequestAnimationFrame'];
        }

        if (!window.requestAnimationFrame) window.requestAnimationFrame = function (callback, element) {
            var currTime = new Date().getTime();
            var timeToCall = Math.max(0, 16 - (currTime - lastTime));
            var id = window.setTimeout(function () {
                    callback(currTime + timeToCall);
                },
                timeToCall);
            lastTime = currTime + timeToCall;
            return id;
        };

        if (!window.cancelAnimationFrame) window.cancelAnimationFrame = function (id) {
            clearTimeout(id);
        };
        getAllLabel();

        $('#run').click(function(){
            if(state_flag==0){
                // 清楚之前的数据
                if(animationId!=null){
                    cancelAnimationFrame(animationId);
                }
                ctx.clearRect(0,0,canvas.width,canvas.height);
                vertices.length = 0;
                points.length = 0;
                t = 1;
                var start = $("#start").val();
                var end = $("#end").val();
                var uuid = $("#search_uuid").val();
                if(uuid == null || uuid == ''){
                    alert("请选择要查看的id！");
                    return;
                }
                if (start == null || start == '' || end == null || end == '') {
                    alert("请选择时间段");
                    return;
                }
                // 向后台请求uuid的labelType和描述信息
                $.ajax({
                    type : "post",// 请求方式
                    url :  "<%=path %>/label/get_label",// 发送请求地址
                    dataType : "json",
                    data : {// 发送给数据库的数据
                        uuid : uuid
                    },
                    // 请求成功后的回调函数有两个参数
                    success : function(data, textStatus) {
                        if(data.length!=0 && data!=null){
                            my_label = data;
                        }
                    }
                });
                // 向后台请求位置信息
                $.ajax({
                    type : "post",// 请求方式
                    url :  "<%=path %>/history/show",// 发送请求地址
                    dataType : "json",
                    data : {// 发送给数据库的数据
                        uuid : uuid,
                        start: start,
                        end: end
                    },
                    // 请求成功后的回调函数有两个参数
                    success : function(data, textStatus) {
                        if(data != null && data.length != 0){
                            $.each(data,function(index, item) {
                                var left = data[index].locationLeft * image_width,
                                    top = data[index].locationTop * image_height,
                                    time = data[index].createTime;
                                vertices.push({
                                    x: left,
                                    y: top,
                                    time: time
                                });
                            });
                            // calculate incremental points along the path
                            movespeed = $("#speed").val();
                            points = calcWaypoints(vertices);
                            showPath();
                        }else{
                            alert("该时间段无数据！");

                        }
                    }
                });
            }
            else if(state_flag == 1){
                showPath();
                state_flag = 0;
            }
        });
        // 点击暂停动画
        $("#pause").click(function(){
            if(animationId!=null){
                cancelAnimationFrame(animationId);
            }
            // 暂停按钮按下，进入状态1
            state_flag = 1;
        });
        // 点击清屏按钮，清空数组和不显示标签
        $("#clear").click(function(){
            if(animationId!=null){
                cancelAnimationFrame(animationId);
            }
            ctx.clearRect(0,0,canvas.width,canvas.height);
            vertices.length = 0;
            points.length = 0;
            t = 1;
            $('#label').css("left", "-90px");
            $('#label').css("top", "-90px");
            state_flag = 0;
        });
        // 点击跳转到显示页面
        $('#to_index').click(function(){
            window.location.href = "nodeindex.jsp";
        });

        /* $("#pause").click(function(){
         if(animationId!=null){
         cancelAnimationFrame(animationId);
         }
         }) */

    }());

    function showPath(){
        // 修改显示标签的图片
        $('#label_img').attr('src',"<%=path %>/statics/imgs/"+image_type[parseInt(my_label.labelType)]+".png");
        // set some style
        ctx.lineWidth = 3;
        ctx.strokeStyle = "green";
        // extend the line from start to finish with animation
        animate(points);
    }
    // calc waypoints traveling along vertices
    function calcWaypoints(vertices) {
        var waypoints = [];
        for (var i = 1; i < vertices.length; i++) {
            var pt0 = vertices[i - 1];
            var pt1 = vertices[i];
            var dx = pt1.x - pt0.x;
            var dy = pt1.y - pt0.y;
            var distance = Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));
            var number = Math.floor(distance / movespeed);
            for (var j = 0; j < number; j++) {
                var x = pt0.x + dx * j / number;
                var y = pt0.y + dy * j / number;
                waypoints.push({
                    x: x,
                    y: y,
                    time: vertices[i].time
                });
            }
        }
        return (waypoints);
    }
    function animate() {
        if (t < points.length - 1) {
            animationId = requestAnimationFrame(animate);
        }
        // draw a line segment from the last waypoint
        // to the current waypoint
        ctx.beginPath();
        ctx.moveTo(points[t - 1].x, points[t - 1].y);
        ctx.lineTo(points[t].x, points[t].y);
        ctx.stroke();
        $('.label_p').children().remove();
        $('.label_p').append("<p>姓名:"+my_label.uuidName+"<br/>"+my_label.description+"时间:"+points[t].time+"<br/></p>");
        $('#label').css("left", points[t].x - 10);
        $('#label').css("top", points[t].y + 24);
        // increment "t" to get the next waypoint
        t++;
    }
    /**
     * 从服务器得到所有的标签信息
     */
    function getAllLabel(){
        $.ajax({
            type:"post",//请求方式
            url:"<%=path %>/label/find_all",//发送请求地址
            dataType:"json",
            data:{//发送给数据库的数据
            },
            //请求成功后的回调函数有两个参数
            success:function(data,textStatus){
                $.each(data, function(index,item){
                    var uuid = data[index].uuid,
                        name = data[index].uuidName;
                    $('#search_uuid').append("<option value='"+uuid+"'>"+uuid+" ("+name+")</option>");
                });
            }
        });
    }
</script>
</body>
</html>
