function jumpPage(pageNo) {
	$("#pageNo").val(pageNo);
	$("#mainForm").submit();
}

function sort(orderBy) {
	var orderByArray=$("#orderBy").val().split(",");
	var index=$.inArray(orderBy,orderByArray); 
	var defaultOrderArray=$("#order").val().split(",");
	
	if(defaultOrderArray[index]=="desc"){
		defaultOrderArray[index]="asc";
	}else{
		defaultOrderArray[index]="desc";
	}
	
	$("#order").val(defaultOrderArray.join());
	$("#mainForm").submit();
}

function search() {
	$("#order").val("");
	$("#orderBy").val("");
	$("#pageNo").val("1");

	$("#mainForm").submit();
}

function aSearch() {
	$("#order").val("");
	$("#orderBy").val("");
	$("#pageNo").val("1");

	$("#mainForm").submit();
}

function pageSizeChange(){
	$("#order").val("");
	$("#orderBy").val("");
	$("#pageNo").val("1");

	$("#mainForm").submit();
}