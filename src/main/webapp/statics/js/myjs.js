// JavaScript Document
/*全屏展示*/
$(function(){
	  $('.sideNav').click(function(){
			 if($('#sidebar').css('display')=='none'){
			   		$(this).css({'left':'200px','display':'block'});
			   		$('#sidebar').fadeIn(0);
			   		$('#main').css('margin','60px auto 0 220px')
			   		
			 	
			 }else{
			   		$(this).css({'left':'0','display':'block'});
			   		$('#sidebar').fadeOut(0);
			   		$('#main').css('margin','60px 0')
			 	
			 }
	  })

});

/*首页时间展示
$(document).ready(function(){	
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
						hourString + ":" + minutesString );

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
*/













