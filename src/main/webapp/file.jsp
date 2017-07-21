<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>导出文件</title>
    <link rel="stylesheet" type="text/css"
          href="<%=path%>/statics/css/restcss.css">
    <link rel="stylesheet" type="text/css"
          href="<%=path%>/statics/plugins/huatiao/css/style.css" />
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

        #map_search div input[type="text"] {
            border: 1px solid #d4d4d4;
            padding: 6px 0 6px 4px;
            box-sizing: border-box;
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

        #label img {
            display: block;
        }

        .label_msg_wrapper .label_p {
            padding: 0px 3px;
            background: white;
            border: 1px solid grey;
            border-radius: 4px;
        }

        .label_msg_wrapper p {
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

        .title {
            margin-left: 20px;
        }

        #search_trillocate {
            height: 50px;
            line-height: 50px;
            background: rgb(191, 142, 233);
        }

        #search_trillocate div input[type="text"] {
            border: 1px solid #d4d4d4;
            padding: 6px 0 6px 4px;
            box-sizing: border-box;
        }

        #search_trillocate div {
            float: left;
            margin-left: 6px;
        }

        #search_trillocate div input[type="button"] {
            padding: 4px 16px;
            border-radius: 4px;
            background: #379b8c;
            color: #fff;
            cursor: pointer;
        }

        #search_trillocate div input[type="button"]:hover {
            background: #268878;
        }
    </style>
</head>
<body width="150%">
<div id="header">
    <div class='title'>慧联人员定位平台</div>
</div>
<div id="map_search">
    <div style="margin-left:10px;">
        uuid: <select id="search_uuid"></select>
    </div>
    <div style="margin-left:10px;">
        始时间: <input type="text" id="start"
                    onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',lang:'zh-cn'})" />
    </div>
    <div>
        末时间: <input type="text" id="end"
                    onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',lang:'zh-cn'})" />
    </div>
    <div>
        <input type="button" id="run" class="play" value="导出文件" />
    </div>

    <div>
        <input type="button" id="to_index" value="显示界面" />
    </div>
</div>
<div id="search_trillocate">
    <div style="margin-left:10px;">导出一段时间的位置数据:</div>
    <div style="margin-left:10px;">
        始时间: <input type="text" id="loc_start"
                    onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',lang:'zh-cn'})" />
    </div>
    <div>
        末时间: <input type="text" id="loc_end"
                    onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',lang:'zh-cn'})" />
    </div>
    <div>
        <input type="button" id="loc_run" class="play" value="导出文件" />
    </div>
</div>
<script src="<%=path%>/statics/js/jquery-3.1.1.min.js" charset="utf-8"></script>
<script type="text/javascript"
        src="<%=path%>/statics/plugins/date/WdatePicker.js"></script>
<script src="<%=path%>/statics/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
        src="<%=path%>/statics/plugins/huatiao/js/jquery-ui-1.8.4.custom.min.js"></script>
<script type="text/javascript"
        src="<%=path%>/statics/plugins/huatiao/js/jQuery.peSlider.js"></script>
<script type="text/javascript"
        src="<%=path%>/statics/js/jquery.fileDownload.js"></script>
<script type="text/javascript">

    $(function() {
        getAllLabel();
        $("#to_index").click(function(){
            window.location.href = "nodeindex.jsp";
        });
        $("#run").click(function(){


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
            // 向后台请求位置信息
            $.ajax({
                type : "post",// 请求方式
                url :  "<%=path%>/file/add",   // 发送请求地址
                dataType : "json",
                data : {// 发送给数据库的数据
                    uuid : uuid,
                    start: start,
                    end: end
                },
                // 请求成功后的回调函数有两个参数
                success : function(data, textStatus) {
                    if(data.code==0){
                        window.location.href="download/file?filename="+uuid+".txt";
                    }
                }
            });

        });

        $("#loc_run").click(function(){


            var start = $("#loc_start").val();
            var end = $("#loc_end").val();
            if (start == null || start == '' || end == null || end == '') {
                alert("请选择时间段");
                return;
            }
            // 向后台请求位置信息
            $.ajax({
                type : "post",// 请求方式
                url :  "<%=path%>/file/gettrillocate",   // 发送请求地址
                dataType : "json",
                data : {// 发送给数据库的数据
                    start: start,
                    end: end
                },
                // 请求成功后的回调函数有两个参数
                success : function(data, textStatus) {
                    if(data.code==0){
                        window.location.href="download/file?filename=trillocate.txt";
                    }
                }
            });

        });
    });

    // 点击跳转到显示页面


    /**
     * 从服务器得到所有的标签信息
     */
    function getAllLabel(){
        $.ajax({
            type:"post",//请求方式
            url:"<%=path%>/label/find_all",//发送请求地址
            dataType : "json",
            data : {//发送给数据库的数据
            },
            //请求成功后的回调函数有两个参数
            success : function(data, textStatus) {
                $
                    .each(
                        data,
                        function(index, item) {
                            var uuid = data[index].uuid, name = data[index].uuidName;
                            $("#search_uuid").append(
                                "<option value='"+uuid+"'>"
                                + uuid
                                + "</option>");
                        });
            }
        });
    }
</script>
</body>
</html>
