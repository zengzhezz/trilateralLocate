//获取项目的url根地址
var localObj = window.location;
var contextPath = localObj.pathname.split("/")[1];
var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
var server_context = basePath;
var socket_context = "ws://"+localObj.host + "/" + contextPath;

var image_width;
var image_height;

var image_ratio = 0.367475;

var image_type = [ "man", "dog", "car", "thief", "bike"];
// 定义canvas对象
var canvas = document.getElementById("canvas");
// 定义context对象
var ctx;

var socket = new WebSocket(socket_context + '/websocket');

$(function() {
//	console.log(socket_context + '/websocket');
	$("body").css("width", $(window).width());
	// 得到图片的宽高
	image_width = document.body.clientWidth;
	image_height = image_width * image_ratio;
	//定义canvas的宽和高
	canvas.width = image_width;
	canvas.height = image_height;
	// 根据canvas得到ctx对象
	ctx = canvas.getContext("2d");
	// set some style
	ctx.scale(1, 1);
	ctx.lineCap = "round";
	ctx.lineWidth = 2;
	ctx.strokeStyle = "blue";
	// 从服务器获取所有节点并显示
	getAllNode();
	// // 从服务器得到所有节点下的人员信息
	// getAllPerson();
	// // 从服务器得到最新的5条流水信息显示
	// getAllFlowMsg();
	// 在关闭网页时，把此时页面的node相关的信息保存进数据库
	window.onbeforeunload = function() {
		// 关闭socket
		socket.close();
	};
	$('#to_config').click(function() {
		window.location.href = "config";
	});
	$('#to_file').click(function() {
		window.location.href = "file.jsp";
	});
	$('#to_history').click(function() {
		window.location.href = "history.jsp";
	});
	$('#show_node').click(function(){
		if($('.node').is(':hidden')){
			$('.node').show();
			$('#show_node').val('隐藏节点');
		}else{
			$('.node').hide();
			$('#show_node').val('显示节点');
		}
	});
	$("#show_log").click(function(){
		if($('.flowmsg').is(':hidden')){
			$('.flowmsg').show();
			$('#show_log').val('隐藏日志');
		}else{
			$('.flowmsg').hide();
			$('#show_log').val('显示日志');
		}
	});
	$('#show_man').click(function(){
		if($(this).is(':checked')){
			$('.label_man').show();
		}else{
			$('.label_man').hide();
		}
	});
	$('#show_car').click(function(){
		if($(this).is(':checked')){
			$('.label_car').show();
		}else{
			$('.label_car').hide();
		}
	});
	$('#show_dog').click(function(){
		if($(this).is(':checked')){
			$('.label_dog').show();
		}else{
			$('.label_dog').hide();
		}
	});
	$('#show_thief').click(function(){
		if($(this).is(':checked')){
			$('.label_thief').show();
		}else{
			$('.label_thief').hide();
		}
	});
	$('#show_bike').click(function(){
		if($(this).is(':checked')){
			$('.label_bike').show();
		}else{
			$('.label_bike').hide();
		}
	})
});

/**
 * 动态画电子围栏
 */

function drawAlarmFrame() {
	var alarm_wrap = document.createElement("div");
	var alarm_wrap2 = document.createElement("div");
	// alarm提示框
	var hint = document.createElement("div");
	alarm_wrap.setAttribute("id", "alarm_wrap");
	alarm_wrap2.setAttribute("id", "alarm_wrap2");
	document.body.appendChild(alarm_wrap);
	document.body.appendChild(alarm_wrap2);
	hint.setAttribute("class", "hint");
	alarm_wrap.appendChild(hint);
	$('#alarm_wrap').css("position", "absolute");
	$('#alarm_wrap').css("top", image_height * alarm_frame_location.top + 50);
	$('#alarm_wrap').css("left", image_width * alarm_frame_location.left);
	$('#alarm_wrap').css("width", image_width * alarm_frame_location.width);
	$('#alarm_wrap').css("height", image_height * alarm_frame_location.height);
	$('#alarm_wrap').css("border", "2px dotted rgba(100,100,100,0.3)");
	$('#alarm_wrap').css("border-radius", "8px");
	$('#alarm_wrap2').css("position", "absolute");
	$('#alarm_wrap2').css("top", image_height * alarm_frame_location2.top + 50);
	$('#alarm_wrap2').css("left", image_width * alarm_frame_location2.left);
	$('#alarm_wrap2').css("width", image_width * alarm_frame_location2.width);
	$('#alarm_wrap2').css("height", image_height * alarm_frame_location2.height);
	$('#alarm_wrap2').css("border", "1px dashed grey");
	$('#alarm_wrap2').css("border-radius", "8px");
	$('.hint').append("<div class='trai'></div><div class='message_wrap'><p class='first'>非法人员</p></div>");
	$('.hint').css("left",image_width * alarm_frame_location.width / 2 - 70);
	$('.hint').css("top",image_height * alarm_frame_location.height + 1);
	$('.hint').hide();
}

/**
 * 在页面上添加节点图标
 * 
 * @param mac
 * @param name
 * @param nodeTop
 * @param nodeLeft
 */
function addNodeByMsg(mac, name, nodeTop, nodeLeft) {
	var node = document.createElement("div");
	node.setAttribute("id", mac);
	node.setAttribute("class", "node");
	node.setAttribute("name", name);
	// 设置出现的初始位置
	node.style.top = nodeTop + 'px';
	node.style.left = nodeLeft + 'px';
	document.body.appendChild(node);
}

/**
 * 得到所有的节点
 */
function getAllNode() {
	$.ajax({
			type : "post",// 请求方式
			url : server_context + "/node/getallnode.do",// 发送请求地址
			dataType : "json",
			data : {// 发送给数据库的数据
			},
			// 请求成功后的回调函数有两个参数
			success : function(data, textStatus) {
				$.each(data,function(index, item) {
					var mac = data[index].mac, name = data[index].name, nodeTop = data[index].nodeTop
							* image_height + 50, nodeLeft = data[index].nodeLeft
							* image_width;
					addNodeByMsg(mac, name, nodeTop,
							nodeLeft);
				});
			}
		});
}

// /**
// * 得到所有节点的人员信息
// */
// function getAllPerson(){
// $.ajax({
// type : "post",// 请求方式
// url : server_context + "/node/getallnodeperson.do",// 发送请求地址
// dataType : "json",
// data : {// 发送给数据库的数据
// },
// // 请求成功后的回调函数有两个参数
// success : function(data, textStatus) {
// $.each(data,function(index, item) {
// var mac = data[index].mac,
// number = data[index].number,
// uuidNameString = data[index].uuidNameString;
// var uuidNameGroup = uuidNameString.split(",");
// setNodePerson(mac,number,uuidNameGroup);
// });
// }
// });
// }

// /**
// * 得到最近的5条流水信息
// */
// function getAllFlowMsg(){
// $.ajax({
// type : "post",// 请求方式
// url : server_context + "/flowmsg/5msg.do",// 发送请求地址
// dataType : "json",
// data : {// 发送给数据库的数据
// },
// // 请求成功后的回调函数有两个参数
// success : function(data, textStatus) {
// if(data !== null && data !== undefined && data !== ''){
// $.each(data,function(index, item) {
// var time = data[index].lastUpdateTime,
// uuid = data[index].uuid,
// uuidName = data[index].uuidName,
// event = data[index].event,
// macName = data[index].macName;
// setFlowMsg(time,uuid,uuidName,event,macName,"",false);
// });
// }
// }
// });
// }

socket.onopen = function(event) {
	socket.send("I am a client and I am listening!");
};

socket.onmessage = function(event) {
	var result = event.data;
	console.log("socket message:" + result);
	var group = result.split(",");
	// "location"来的数据类型,(uuid, uuidName, labelType, description, left, top)
	if (group[0] == "location") {
		// 删除此人
		$('.label_' + group[1]).remove();
		addLabel(group[1], group[2], group[3], group[4], group[5], group[6]);
	}else if(group[0] == "alarm"){ // "alarm来的数据类型(is_alarmed[0表示无报警，1表示有报警],number[报警人数],uuid1,uuid1Name,uuid2....)"
		if(group[1]=='1'){
			$('#alarm_wrap').css("border-color", "rgba(255,0,0,0.3)");
			$('#alarm_wrap2').css("border-color", "red");
			$('.hint').show();
			$('.hint .message_wrap').children().not('.first').remove();
			for(var i=1; i<=group[2]; i++){
				var uuid = group[2*i+1], uuidName = group[2*i+2];
				$('.hint .message_wrap').append("<p>"+uuid+", "+uuidName+"</p>");
			}
			//如果是进入，则发出警报声
			if($("#alarmMusic")[0].paused){
				//播放警报音
				$("#alarmMusic")[0].play();
			}
		}else{
//			$('#alarm_wrap').css("border", "3px solid grey");
			$('#alarm_wrap').css("border-color", "rgba(100,100,100,0.3)");
			$('#alarm_wrap2').css("border-color", "grey");
			$('.hint').hide();
			//如果是播放中
			if(!$("#alarmMusic")[0].paused){
				//停止报警
				$("#alarmMusic")[0].pause();
			}
		}
	}else if(group[0] == "delete_all"){
		$('.label').remove();
	}else if(group[0] == "delete"){
		var uuid = group[1];
		$('.label_'+uuid).remove();
	}else if(group[0] == "flowmsg"){
		setFlowMsg(group[1],group[2],group[3],group[4],group[5],group[6],group[7]);
	}else if(group[0] == "clear_canvas"){
		// clear canvas
		ctx.clearRect(0,0,canvas.width,canvas.height);
	}else if(group[0] == "draw_circle"){
		// (circle_x, circle_y, radiux)
		drawThisCircle(group[1]*image_width+4, group[2]*image_height+4, group[3]*image_width);
	}
};

/**
 * 添加显示的人员
 * 
 * @param uuid
 * @param uuidName
 * @param left
 * @param top
 */
function addLabel(uuid, uuidName, labelType, description, left, top) {
	var image_string = image_type[parseInt(labelType)];
	$('body').append("<div class='label label_"+uuid+ " label_"+image_string+"'><img width='20px' src='"+ server_context
		+ "/statics/imgs/"+ image_string+ ".png'></img><div class='label_msg'><div class='tria'></div><div class='label_p'><p>姓名:"
		+ uuidName + "<br/>"+description+"</p></div></div></div>");
	$('.label_' + uuid).css("position", "absolute");
	$('.label_' + uuid).css("left", (image_width * left - 8) + "px");
	$('.label_' + uuid).css("top", (image_height * top + 50 - 10) + "px");
//	if(!$('#show_'+image_string).is(':checked')){
//		$('.label_' + uuid).hide();
//	}
//	$('body').append("<div class='circle'></div>");
//	$('.circle').css("position", "absolute");
//	$('.circle').css("left", (image_width * left) + "px");
//	$('.circle').css("top", (image_height * top + 50) + "px");
}

function setFlowMsg(time, uuid, count, mac1, distance1, mac2, distance2) {
//	var random_num = Math.ceil(Math.random() * 10000);
	if(count == '1'){
		$(".flowmsg .first")
		.after("<tr><td>"
		+ time + "</td><td>" + uuid + "</td><td>"+ count + "</td><td>" + mac1 + "</td><td>"
		+ parseFloat(distance1).toFixed(2)+"</td><td></td><td></td></tr>");
	}else if(count == '2'){
		$(".flowmsg .first")
		.after("<tr><td>"
		+ time + "</td><td>" + uuid + "</td><td>"+ count + "</td><td>" + mac1 + "</td><td>"
		+ parseFloat(distance1).toFixed(2) + "</td><td>" + mac2 + "</td><td>" + parseFloat(distance2).toFixed(2) + "</td></tr>");
	}
//	window.setTimeout(function() {
//		$(".flowmsg ." + random_num + " .new_message_hint").remove();
//		$(".flowmsg ." + random_num).removeAttr("class");
//	}, 8000);
	var trs_length = $(".flowmsg tr").length;
	if (trs_length > 11) {
		$(".flowmsg tr:last").remove();
	}
}

/**
 * 根据圆心和半径画图
 * @param circle_x
 * @param circle_y
 * @param radiux
 */
function drawThisCircle(circle_x, circle_y, radiux){
	ctx.beginPath();
	ctx.arc(circle_x, circle_y, radiux, 0, Math.PI * 2, false); 
	ctx.stroke();
}
//
// function setNodePerson(mac, number, uuidGroup){
// $("#" + mac + " .cornertext").html(number);
// // 更新popView的List中的内容,在ul中添加人员信息
// // 得到ul的jquery对象
// var $ul = $("#" + mac + " .listContent").children(".list-group");
// $ul.children("li").remove();
// // 如果人数非空，才添加图片和人数信息
// if (number != 0) {
// for (var i = 0; i < uuidGroup.length; i++) {
// $ul.append("<li><img src='" + server_context
// + "/statics/imgs/man.png' width='20px'></img>"
// + uuidGroup[i] + "</li>");
// }
// }
// }
