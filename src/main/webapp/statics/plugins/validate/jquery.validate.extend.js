$.extend($.validator.messages, {
    required: "请填写该字段",
    remote: "请修正该字段",
    email: "请输入正确格式的电子邮件",
    url: "请输入合法的网址",
    date: "请输入合法的日期",
    dateISO: "请输入合法的日期 (ISO).",
    number: "请输入合法的数字",
    digits: "只能输入整数",
    creditcard: "请输入合法的信用卡号",
    equalTo: "请再次输入相同的值",
    accept: "请输入拥有合法后缀名的字符串",
    maxlength: $.validator.format("请输入一个长度最多是 {0} 的字符串"),
    minlength: $.validator.format("请输入一个长度最少是 {0} 的字符串"),
    rangelength: $.validator.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
    range: $.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
    max: $.validator.format("请输入一个最大为 {0} 的值"),
    min: $.validator.format("请输入一个最小为 {0} 的值")
});



$.validator.addMethod("xxx",function(value,element,params){
	if(value.length>1){
	return false;
	}
	if(value>=params[0]&&value<=params[1]){
	return true;
	}else{
	return false;
	}
},"必须是一个字母,且a-f");


/*正整数*/
jQuery.validator.addMethod("posInt", function(value, element) {
	return this.optional(element) || /^[1-9]\d*$/.test(value);
	}, "请填写正整数");

/*
经纬度*/
jQuery.validator.addMethod("itude", function(value, element) {
	return this.optional(element) || /^(-[1-9]\d*\.\d{1,6}|-0\.\d{1,6})|([1-9]\d*\.\d{1,6}|0\.\d{1,6})$|^\d*$/.test(value);
	}, "请填写最多含6位小数的数字");

/*信道*/
jQuery.validator.addMethod("upCh", function(value, element) {
	return this.optional(element) || /^([1-9]\d*\.\d{1,2}|0\.\d{1,2}|[1-9]\d*)$|^\d*$/.test(value);
	}, "请填写最多含2位小数的数字");