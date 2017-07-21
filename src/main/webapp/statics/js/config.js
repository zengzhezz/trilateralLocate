//获取项目的url根地址
var localObj = window.location;
var contextPath = localObj.pathname.split("/")[1];
var basePath = localObj.protocol+"//"+localObj.host+"/"+contextPath;
var server_context=basePath;

var image_width;
var image_height;
var image_ratio = 0.367475;
var real_width ;
var real_height;
// 定义标签类型数组，之后可以直接通过index找到
var labelArray = ["人","狗","车","非法人","电动车"];

$(function(){
	  //清除scenebox默认的右击事件
	  document.getElementById("scene").oncontextmenu = function(event){
		  event.returnValue = false;
	  };
	  $("body").css("width", $(window).width());
	  //得到图片的宽高
	  image_width = document.body.clientWidth;
	  image_height = image_width * image_ratio;
	  $('.add_node_btn').click(function(){
	    var mac = $(".config_msg input[name='mac']").val().toLowerCase(),
	        name = $(".config_msg input[name='name']").val();
	    if(checkInputMsg(mac, name)){
	      addNodeByMsg(mac,name,100,100);
	    }
	  });
	  $('.add_person_btn').click(function(){
	    var uuid = $(".config_person input[name='uuid']").val().toLowerCase(),
	        name = $(".config_person input[name='name']").val(),
	        type = $(".label_type").val(),
	        description = $("#label_description").val();
	    if(checkAddPersonMsg(uuid, name, type)){
	      addPerson(uuid, name, type, description);
	    }
	  });
	  $('.save_node_btn').click(function(){
		  saveNode();
	  });
	  $('#addlocation').click(function(){
			var width=$("#width").val();
			
			var height=$("#height").val();
			$.ajax({
		        type:"post",//请求方式
		        url:server_context + "/location/save.do",//发送请求地址
		        dataType:"json",
		        data:{
		        	width: width,
		        	height: height
		        },
		        //请求成功后的回调函数有两个参数
		        success:function(data,textStatus){
		          //如果添加成功
		          if(data.code==0){
		        	  alert("sucess");
		          }
		        }
		   });
			   
	  });
	/*  $('#to_index').click(function(){
		  window.location.href = "index";
	  });
	  $("#to_config_alarm").click(function(){
		  window.location.href = "config_alarm";
	  })*/
	  findlocation();
	  getAllLabel();
	  getAllNode();
	  addLocationNode("mylocationnode","mylocationnode","150","100");
	});
	/*添加一个定位的点*/
function addLocationNode(id,name,nodeTop,nodeLeft){
    console.log(id + ", " + name);
    var node = document.createElement("div");
    node.setAttribute("id", id);
    node.setAttribute("class","mylocationnode");
    node.setAttribute("name",name);
   
    node.onmousedown = fnDown1;
    //设置出现的初始位置
    node.style.top = nodeTop + 'px';
    node.style.left = nodeLeft + 'px';
    document.body.appendChild(node);
}
/**
 * 在页面上添加一个节点
 * @param mac
 * @param name
 * @param x
 * @param y
 */
function addNodeByMsg(mac,name,nodeTop,nodeLeft){
    console.log(mac + ", " + name);
    var node = document.createElement("div");
    node.setAttribute("id", mac);
    node.setAttribute("class","node");
    node.setAttribute("name",name);
    node.appendChild(createpopwindow(name));
    node.onmousedown = fnDown;
    //设置出现的初始位置
    node.style.top = nodeTop + 'px';
    node.style.left = nodeLeft + 'px';
    //鼠标悬浮提示实际距离
    //新建一个提示框
    var hint = document.createElement('p');
    hint.setAttribute("id", "hint");
    node.onmousemove = function(){
    	var height = ((node.offsetTop - 50) / image_height * real_height).toFixed(1);
    	var width = (node.offsetLeft / image_width * real_height).toFixed(1);
    	hint.innerHTML = width+","+height;
    	hint.style.display = "block";
    };
    node.onmouseout = function(){
    	hint.style.display = "none";
    };
    node.appendChild(hint);
    document.body.appendChild(node);
}

function createpopwindow(name){
	  var popwindow = document.createElement("div"),
	      triangle = document.createElement("div"),
	      popbox = document.createElement("div"),
	      p = document.createElement("p");
	  p.textContent = name;
	  triangle.setAttribute("class", "triangle");
	  popbox.setAttribute("class", "popbox");
	  popwindow.setAttribute("class", "popwindow");
	  popwindow.appendChild(triangle);
	  popbox.appendChild(p);
	  popwindow.appendChild(popbox);
	  return popwindow;
}

function saveNode(){
	var jsonstr="[]";
    var jsonarray = eval('('+jsonstr+')');
	var $nodes = $(".node");
	for (var node of $nodes){
		var arr = {
			mac: $(node).attr("id"),
			name: $(node).attr("name"),
			nodeTop: (((node.style.top).replace("px","") - 50)/image_height).toFixed(4),
			nodeLeft: (((node.style.left).replace("px",""))/image_width).toFixed(4)
		};
		jsonarray.push(arr);
	}
	$.ajax({
        type:"post",//请求方式
        url:server_context + "/node/savenode.do",//发送请求地址
        dataType:"json",
        data: JSON.stringify(jsonarray),
        //请求成功后的回调函数有两个参数
        success:function(data,textStatus){
          //如果添加成功
          if(data.code==0){
        	 
          }
        }
   });
}
function findlocation(width, height){
	  // 发送异步请求给后台
	$.ajax({
      type:"post",//请求方式
      url:server_context + "/location/find.do",//发送请求地址
      dataType:"json",
      data:{
      
      },
      //请求成功后的回调函数有两个参数
      success:function(data,textStatus){
        //如果添加成功
      real_width = data.width;
      real_height =  data.height;
   
        
      }
 });
}
function savelocation(){
	var width=$("#width").val();
	$("#width").after(width);
	var height=$("#height").val();
	$("#width").after(height);
	 if(checkInput(width, height)){
		 addlocation(width,height);
	    }
}
	function checkInput(width,height){
		
		  if(width==""){
		    alert("width不能为空");
		    return false;
		  }else if(height==""){
		    alert("height不能为空");
		    return false;
		  }
		}
	function addlocation(width, height){
		  // 发送异步请求给后台
		$.ajax({
	        type:"post",//请求方式
	        url:server_context + "/location/save.do",//发送请求地址
	        dataType:"json",
	        data:{
	        	width: width,
	        	height: height
	        },
	        //请求成功后的回调函数有两个参数
	        success:function(data,textStatus){
	          //如果添加成功
	          if(data.code==0){
	        	 
	          }
	        }
	   });
	}
/**
 * 检测添加节点输入框是否合法
 * @param mac
 * @param name
 * @returns {Boolean}
 */
function checkInputMsg(mac,name){
  var flag = 0;
  if(mac==""){
    alert("mac不能为空");
    return false;
  }else if(name==""){
    alert("name不能为空");
    return false;
  }else{
    var $nodes = $(".node");
    $.each($nodes,function(index, item){
        if($(this).attr('id')==mac){
          alert("此mac已注册!");
          flag = 1;
          return false;
        }
    });
    if(flag==0)
      return true;
    else
      return false;
  }
}

/**
 * 增加人员信息，后台和页面都要进行添加
 * @param uuid
 * @param name
 */
function addPerson(uuid, name, type, description){
  // 发送异步请求给后台
  $.ajax({
        type:"post",//请求方式
        url:server_context + "/label/add_label",//发送请求地址
        dataType:"json",
        data:{//发送给数据库的数据
          uuid: uuid,
          name: name,
          type: type,
          description: description
        },
        //请求成功后的回调函数有两个参数
        success:function(data,textStatus){
          //如果添加成功
          if(data.code==0){
        	 addPersonTr(uuid,name, type, description);
          }
        }
   });
}

/**
 * 在table中添加一行人员的信息
 * @param uuid
 * @param name
 */
function addPersonTr(uuid,name,type,description){
	var typeName = labelArray[parseInt(type)];
	$(".config_person table tr:last").after("<tr><td class='person_uuid'>"+uuid+"</td><td>"+name+"</td><td>"+typeName+"</td><td>"+description+"</td><td width='15%'><button class='person_delete_btn_"+uuid+"'>删除</button></td></tr>");
    // 如果删除按钮按下
    $(".config_person .person_delete_btn_"+uuid).click(function(){
      var person_uuid = $(this).parent().parent().children(":first").text(),
          $person_tr = $(this).parent().parent();
      //发送删除person请求给后台
      $.ajax({
            type:"post",//请求方式
            url:server_context + "/label/delete_label",//发送请求地址
            dataType:"json",
            data:{//发送给数据库的数据
              uuid: person_uuid
            },
            //请求成功后的回调函数有两个参数
            success:function(returndata,textStatus){
              if(returndata.code==0){
                $person_tr.remove();
              }else{
                //否则弹出错误信息
                alert(returndata.message);
              }
            }
       });
    });
}

/**
 * 从服务器得到所有的标签信息
 */
function getAllLabel(){
	$.ajax({
        type:"post",//请求方式
        url:server_context + "/label/find_all",//发送请求地址
        dataType:"json",
        data:{//发送给数据库的数据
        },
        //请求成功后的回调函数有两个参数
        success:function(data,textStatus){
        	$.each(data, function(index,item){
        		var uuid = data[index].uuid,
        			name = data[index].uuidName,
        			type = data[index].labelType,
        			description = data[index].description;
        		addPersonTr(uuid, name, type, description);
        	});
        }
   });
}

/**
 * 得到所有的节点
 */
function getAllNode(){
	$.ajax({
        type:"post",//请求方式
        url:server_context + "/node/getallnode.do",//发送请求地址
        dataType:"json",
        data:{//发送给数据库的数据
        },
        //请求成功后的回调函数有两个参数
        success:function(data,textStatus){
        	$.each(data, function(index,item){
        		var mac = data[index].mac,
        			name = data[index].name,
        			nodeTop = data[index].nodeTop * image_height + 50,
        			nodeLeft = data[index].nodeLeft * image_width;
        		addNodeByMsg(mac, name, nodeTop, nodeLeft);
        	});
        }
   });
}

/**
 * 检测添加人员输入框是否合法
 * @param uuid
 * @param name
 * @returns {Boolean}
 */
function checkAddPersonMsg(uuid, name, type){
  var $uuids = $(".config_person .person_uuid");
  var flag = 0;
  if(uuid==""){
    alert("uuid不能为空");
    return false;
  }else if(name==""){
    alert("name不能为空");
    return false;
  }else if(type==""){
	alert("类型不能为空");
	return false;
  }else{
    $.each($uuids,function(index, item){
        if($(this).html()==uuid){
          alert("此uuid("+uuid+")已注册!");
          flag = 1;
          return false;
        }
    });
    if(flag==0)
      return true;
    else {
      return false;
    }
  }
}

var key = false;
var firstTime = 0;
var lastTime = 0;
//鼠标按下时的操作
function fnDown(event){
	var node = this;
	//如果鼠标左键按下
	if(event.button == 0){
		firstTime = new Date().getTime();
	    var id = this.id,
	        // 光标按下时光标和面板之间的距离
	        disX=event.clientX-this.offsetLeft,
	        disY=event.clientY-this.offsetTop;
	    document.onmousemove = function(event){
	      fnMove(event,disX,disY,id);
	    };
	    document.onmouseup = function(){
	    	document.onmousemove = null;
	    	document.onmouseup = null;
	    };
	    node.onmouseup = function(){
	      lastTime = new Date().getTime();
	      node.onmouseup = null;
	      if(lastTime - firstTime < 200){
	        //如果时间差小于200ms，执行点击事件
	        key = true;
	      }
	    }
	}
	//如果鼠标右键按下，删除此node
	else if(event.button == 2){
		if(confirm("是否删除此节点？")){
			//在页面上删除此节点
			node.parentNode.removeChild(node);
			$.ajax({
		        type:"post",//请求方式
		        url:server_context + "/node/deletenode.do",//发送请求地址
		        dataType:"json",
		        data:{//发送给数据库的数据
		        	mac:$(node).attr("id")
		        },
		        //请求成功后的回调函数有两个参数
		        success:function(data,textStatus){
		        	if(data.code==1){
		    			console.log(data.msg);
		        	}
		        }
		   });
		}
		else{
			this.oncontextmenu = function(evt){
				//清除默认的右键点击菜单
				evt.returnValue = false;
			}
		}
	}
}

function fnDown1(event){
	var node = this;
	//如果鼠标左键按下
	if(event.button == 0){
		firstTime = new Date().getTime();
	    var id = this.id,
	        // 光标按下时光标和面板之间的距离
	        disX=event.clientX-this.offsetLeft,
	        disY=event.clientY-this.offsetTop;
	    document.onmousemove = function(event){
	      fnMove1(event,disX,disY,id);
	    };
	    document.onmouseup = function(){
	    	document.onmousemove = null;
	    	document.onmouseup = null;
	    };
	    node.onmouseup = function(){
	      lastTime = new Date().getTime();
	      node.onmouseup = null;
	      if(lastTime - firstTime < 200){
	        //如果时间差小于200ms，执行点击事件
	        key = true;
	      }
	    }
	}

		else{
			this.oncontextmenu = function(evt){
				//清除默认的右键点击菜单
				evt.returnValue = false;
			}
		}
	}

function fnMove1(event,posX,posY,id){
	  var oDrag = document.getElementById(id),
	      left = event.clientX - posX,
	      top = event.clientY - posY;
	  oDrag.style.left = left + 'px';
	  oDrag.style.top = top + 'px';
	  $("#juli").remove();
	  var this_left = (left / image_width * real_width).toFixed(2);
	  var this_top = ((top-50) / image_height * real_height).toFixed(2);
	  $("#addlocation").parent().after("<div style='float:left' id='juli'>"+this_left+', &nbsp;&nbsp;'+this_top+' '+ "</div>");
}

function fnMove(event,posX,posY,id){
  var oDrag = document.getElementById(id),
      left = event.clientX - posX,
      top = event.clientY - posY;
  oDrag.style.left = left + 'px';
  oDrag.style.top = top + 'px';
 
}
