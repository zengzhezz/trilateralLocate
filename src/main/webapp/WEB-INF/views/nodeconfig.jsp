<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配置页面</title>
	<link rel="stylesheet" type="text/css" href="<%=path %>/statics/css/config.css">
    <link rel="stylesheet" type="text/css" href="<%=path %>/statics/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
    <link rel="stylesheet" type="text/css" href="<%=path %>/statics/css/component.css">
    <link rel="stylesheet" type="text/css" href="<%=path %>/statics/css/restcss.css">
</head>

<body width="150%">
	<div id="header">
  		<div style="margin-left:10px;">
			配置页面
		</div>
		<div>
			<input type="button" id="to_index" value="显示界面" />
		</div>
		
  	</div>
	<div id="scene" class="scene">
      <img src="<%=path %>/statics/imgs/background.jpg" alt="">
    </div>
    <div class="config_msg">
      <span class="input input--kaede">
        <input class="input__field input__field--kaede" type="text" id="input-35" name="mac"/>
        <label class="input__label input__label--kaede" for="input-35">
          <span class="input__label-content input__label-content--kaede">mac</span>
        </label>
      </span>
      <span class="input input--kaede">
        <input class="input__field input__field--kaede" type="text" id="input-36" name="name"/>
        <label class="input__label input__label--kaede" for="input-36">
          <span class="input__label-content input__label-content--kaede">名称</span>
        </label>
      </span>
      <button type="button" name="button" class="add_node_btn">添加</button>
      <button type="button" name="button" class="save_node_btn">保存</button>
    </div>
   <!--   <div class="config_person content">
     
      <table>
        <tr class="first">
          <td>uuid</td>
          <td>姓名</td>
          <td>类型</td>
          <td>描述</td>
          <td width='15%'>操作</td>
        </tr>
      </table>
    </div>-->
    <script src="<%=path %>/statics/js/jquery-3.1.1.min.js" charset="utf-8"></script>
    <script src="<%=path %>/statics/js/classie.js"></script>
    <script src="<%=path %>/statics/js/config.js" charset="utf-8"></script>
    <script>
		(function() {
			// trim polyfill : https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/Trim
			if (!String.prototype.trim) {
				(function() {
					// Make sure we trim BOM and NBSP
					var rtrim = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g;
					String.prototype.trim = function() {
						return this.replace(rtrim, '');
					};
				})();
			}

			[].slice.call( document.querySelectorAll( 'input.input__field' ) ).forEach( function( inputEl ) {
				// in case the input is already filled..
				if( inputEl.value.trim() !== '' ) {
					classie.add( inputEl.parentNode, 'input--filled' );
				}

				// events:
				inputEl.addEventListener( 'focus', onInputFocus );
				inputEl.addEventListener( 'blur', onInputBlur );
			} );

			function onInputFocus( ev ) {
				classie.add( ev.target.parentNode, 'input--filled' );
			}

			function onInputBlur( ev ) {
				if( ev.target.value.trim() === '' ) {
					classie.remove( ev.target.parentNode, 'input--filled' );
				}
			}
		})();
	</script>

</body>
</html>