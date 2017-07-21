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


$(function() {
//	console.log(socket_context + '/websocket');
	$("body").css("width", $(window).width());
	
	
	// 从服务器获取所有节点并显示

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








socket.onopen = function(event) {
	socket.send("I am a client and I am listening!");
};

socket.onmessage = function(event) {
	var result = event.data;
	console.log("socket message:" + result);
	var group = result.split(",");
	// "location"来的数据类型,信息格式("command","uuid","rssi","type","time","mac")
	if(group[0] == "flow"){
		setFlowMsg(group[1],group[2],group[3],group[4],group[5]);
	}
};



function setFlowMsg(uuid,rssi,type,time,mac) {
//	var random_num = Math.ceil(Math.random() * 10000);
	var search_uuid = $("#search_id").val();
	
	if(search_uuid != null&&search_uuid!=""){
	if(search_uuid == uuid){
		$(".course_tab .tab4 .first")
		.after("<tr><td>"
		+ uuid + "</td><td>" + rssi + "</td><td>"+ type + "</td><td>" + time +  "</td><td>" + mac+"</td></tr>");
	}else if(count == '2'){
		//do nothing
	}
	}else{
		$(".course_tab .tab4 .first")
		.after("<tr><td>"
		+ uuid + "</td><td>" + rssi + "</td><td>"+ type + "</td><td>" + time +  "</td><td>" + mac+"</td></tr>");
	}
//	window.setTimeout(function() {
//		$(".flowmsg ." + random_num + " .new_message_hint").remove();
//		$(".flowmsg ." + random_num).removeAttr("class");
//	}, 8000);
	var trs_length = $(".course_tab tr").length;
	if (trs_length > 31) {
		$(".course_tab tr:last").remove();
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
