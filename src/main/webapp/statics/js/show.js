//获取项目的url根地址
var localObj = window.location;
var contextPath = localObj.pathname.split("/")[1];
var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
var server_context = basePath;
var socket_context = "ws://"+localObj.host + "/" + contextPath;


//本地调试地址
//var socket = new WebSocket('ws://localhost:8080/demonstration/websocket');
// var socket = new WebSocket('ws://localhost:8080/locationwp/websocket');
var socket = new WebSocket(socket_context + '/websocket');
// 服务器地址
// var socket = new WebSocket('/websocket');

// 定义节点对象数组
var myNodeArray = [];

$(function() {
//	console.log(socket_context + '/websocket');
	$("body").css("width", $(window).width());
	
	
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
	}
	
});



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
					var myNode = new MyNode(mac, name,
							nodeLeft, nodeTop);
					myNodeArray.push(myNode);
				});
			}
		});
}



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
	if(!$('#show_'+image_string).is(':checked')){
		$('.label_' + uuid).hide();
	}
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
