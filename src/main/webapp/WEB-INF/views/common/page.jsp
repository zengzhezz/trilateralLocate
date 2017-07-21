<%@ page pageEncoding="UTF-8"%>
<div>
	<button class="btn btn-primary btn-sm pagebtn">第${page.pageNo}页,
		共${page.totalPages}页,共${page.totalCount}条</button>
	<a class="btn btn-primary btn-sm" href="javascript:jumpPage(1)">首页</a>
	<c:if test="${page.hasPre}">
		<a class="btn btn-primary btn-sm"
			href="javascript:jumpPage(${page.prePage})">上一页</a>
	</c:if>
	<c:if test="${page.hasNext}">
		<a class="btn btn-primary btn-sm"
			href="javascript:jumpPage(${page.nextPage})">下一页</a>
	</c:if>
	<a class="btn btn-primary btn-sm"
		href="javascript:jumpPage(${page.totalPages})">末页</a> 
		
		<select id="pageSize" name="pageSize" onchange="pageSizeChange();" >
		<option value="10">10</option>
		<option value="20">20</option>
		<option value="50">50</option>
		<option value="100">100</option>
		<option value="200">200</option>
		<option value="500">500</option>
		<option value="1000">1000</option>
	</select> <select id="refreshSelect" name="refreshTime"
		value="${param['refreshTime']}" onchange="changeRefresh(this.value);" >
		<option value="">不刷新</option>
		<option value="5">5秒</option>
		<option value="10">10秒</option>
		<option value="20">20秒</option>
		<option value="30">30秒</option>
		<option value="60">60秒</option>
	</select> <input type="hidden" name="pageNo" id="pageNo" value="${page.pageNo}" />
	<input type="hidden" name="orderBy" id="orderBy"
		value="${page.orderBy}" /> <input type="hidden" name="order"
		id="order" value="${page.order}" />
	<script type="text/javascript">
		$(function() {
			$("#pageSize").val("${page.pageSize}");
			//batch delete
			// 全选
			$("#allCheck").click(function() {
				$("input[name='subCheck']").prop("checked", this.checked);
			});
			// 单选
			var subCheck = $("input[name='subCheck']");
			subCheck.click(function() {
						$("#allCheck").attr("checked",subCheck.length == subCheck
						.filter(":checked").length ? true: false);
					});
			/* 批量删除 */
			$("#deleteBatchButton").click(
							function() {
								// 判断是否至少选择一项
								var checkedNum = $("input[name='subCheck']:checked").length;
								if (checkedNum == 0) {
									alert("请选择至少一项！");
									return;
								}
								// 批量选择
								if (confirm("确定要删除所选项目？")) {
									var checkedList = [];
									$("input[name='subCheck']:checked").each(function() {
								       checkedList.push($(this).val());
									                        });

									$.ajax({
												type : "POST",
												dataType : "html",
												url : batchUrl,
												data : {'ids' : checkedList.toString()},
												success : function(result) {
												if (result == "0") {
													$("[name ='subCheck']:checkbox").attr("checked",false);
													window.location.reload();
														alert("删除成功");
													} 
													else  {
														alert("无法删除,请确认对象没有关联其他");
														window.location.reload();
													}
												}
											});
								}
							});
			//batch delete
			//init Refresh
			var refreshTime = "${param['refreshTime']}";
			if (refreshTime == null || refreshTime == "") {
				$("#refreshSelect").val("");
			} else {
				$("#refreshSelect").val("${param['refreshTime']}");
				changeRefresh("${param['refreshTime']}");
			}
		});
		function changeRefresh(val) {
			window.setTimeout(function timeout() {
				$("#mainForm").submit();
			}, val * 1000);
		}
	</script>
</div>