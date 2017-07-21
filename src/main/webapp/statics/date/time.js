// JavaScript Document
window.onload=function showLeftTime(){
//读取系统时间
var now=new Date();
		 var year=now.getYear()+1900;
         var month=now.getMonth()+1;
         var date=now.getDate();
		 var day=now.getDay();
		 var today = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
         var hours=now.getHours(); 
         var minutes=now.getMinutes();
         var seconds=now.getSeconds();
		 if(now.getMonth()+1<10){
			 month="0"+month
		 }
		 if(now.getDate()+1<10){
			 date="0"+date
		 }
		 if(now.getHours()<10){
			 hours="0"+hours
		 }
		 if(now.getMinutes()<10){
			 minutes="0"+minutes
		 }



	document.getElementById("sj_time").innerHTML=""+hours+":"+minutes+"";
	document.getElementById("sj_xq").innerHTML=""+year+"-"+month+"-"+date+" "+today[day];
	
//一秒刷新一次显示时间
    var timeID=setTimeout(showLeftTime,1000);
};