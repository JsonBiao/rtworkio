<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"  %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<base href="<%=basePath%>">
    <meta charset="utf-8">
    <title>${sessionScope.sysName}</title>
    <!-- HTML5 Shim and Respond.js IE10 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 10]>
		<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
		<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
    <!-- Meta -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="author" content="FH Admin QQ313596790" />

    <link rel="icon" href="assets/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="assets/fonts/material/css/materialdesignicons.min.css">
    <link rel="stylesheet" href="assets/fonts/fontawesome/css/fontawesome-all.min.css">
    <link rel="stylesheet" href="assets/plugins/animation/css/animate.min.css">
    <link rel="stylesheet" href="assets/css/style.css">

</head>

<body>
    
    <!-- [加载状态 ] start -->
    <div class="loader-bg">
        <div class="loader-track">
            <div class="loader-fill"></div>
        </div>
    </div>
    <!-- [ 加载状态  ] End -->

	    <!-- [ Hover-table ] start -->
        <div class="col-xl-12" style="padding-top: 15px;">
            <div class="card">
	
					<form action="studyplanmx/list" method="post" name="Form" id="Form">
						<input type="hidden" name="STUDYPLAN_ID" id="STUDYPLAN_ID" value="${pd.STUDYPLAN_ID}"/>
						<!-- 检索  -->
						<div style="padding-left: 20px;padding-top: 15px;">
						<table>
							<tr>
								<td>
			                        <div class="input-group input-group-sm mb-3">
                                    	<input class="form-control" id="KEYWORDS" type="text" name="KEYWORDS" value="${pd.KEYWORDS }" placeholder="这里输入关键词" />
                                	</div>
								</td>
								<td style="vertical-align:top;padding-left:5px;">
									<a class="btn btn-light btn-sm" onclick="searchs();" style="width: 23px;height:30px;margin-top:1px;" title="检索"><i style="margin-top:-3px;margin-left: -6px;"  class="feather icon-search"></i></a>
									<shiro:hasPermission name="toExcel">
									<a class="btn btn-light btn-sm" onclick="toExcel();" style="width: 23px;height:30px;margin-top:1px;margin-left: -9px;" title="导出到excel表格">
										<i style="margin-top:-3px;margin-left: -6px;" class="mdi mdi-cloud-download"></i>
									</a>
									</shiro:hasPermission>
								</td>
							</tr>
						</table>
						</div>
						<!-- 检索  -->
						<div class="card-block table-border-style" style="margin-top: -15px">
            				<div class="table-responsive">
								<table class="table table-hover mb-0">	
									<thead>
										<tr>
											<th style="width: 50px;" id="cts">
					                            <div class="checkbox d-inline">
                                                    <input type="checkbox" name="fhcheckbox" id="zcheckbox">
                                                    <label  style="max-height: 12px;" for="zcheckbox" class="cr"></label>
                                                </div>
											</th>
											<th style="width:50px;">NO</th>
											<th>学习书目</th>
											<th>书本信息</th>
											<th>学习情况</th>
											<th>开始学习时间</th>
											<th>完成学习时间</th>
											<th>上级评分</th>
											<th>操作</th>
										</tr>
									</thead>
															
									<tbody>
									<!-- 开始循环 -->	
									<c:choose>
										<c:when test="${not empty varList}">
											<c:forEach items="${varList}" var="var" varStatus="vs">
												<c:if test="${'no' == isEnd }">
											    <tr ondblclick="edit('${var.STUDYPLANMX_ID}');">
				                 				</c:if>
													<td>
													  	<div class="checkbox d-inline">
                                                        	<input type="checkbox" id="zcheckbox${vs.index }" name='ids' value="${var.STUDYPLANMX_ID}">
                                                        	<label  style="max-height: 12px;" for="zcheckbox${vs.index }" class="cr"></label>
                                                    	</div>
													</td>
													<td scope="row">${page.showCount*(page.currentPage-1)+vs.index+1}</td>
													<td>${var.BOOKNAME}</td>
													<td>${var.BOOKCONTET}</td>
													<td>${var.COUNT}</td>
													<td>${var.STUDYSTARTTIME}</td>
													<td>${var.STUDYENDTIME}</td>
													<td>${var.SCORE}</td>
													<td>
														<c:if test="${'no' == isEnd }">
														   <a title="修改" onclick="edit('${var.STUDYPLANMX_ID}');" style="cursor:pointer;"><i class="feather icon-edit-2"></i></a>
					                 					</c:if>
					                 					<a title="删除" onclick="del('${var.STUDYPLANMX_ID}');" style="cursor:pointer;"><i class="feather icon-x"></i></a>
													</td>
												</tr>
											
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr>
												<td colspan="100">没有相关数据</td>
											</tr>
										</c:otherwise>
									</c:choose>
									</tbody>
								</table>
								
								<table style="width:100%;margin-top:15px;">
									<tr>
										<td style="vertical-align:top;">
											<a class="btn btn-light btn-sm" onclick="add();">新增</a>
											<a class="btn btn-light btn-sm" onclick="makeAll('确定要删除选中的数据吗?');">删除</a>
										</td>
										<td style="vertical-align:top;"><div style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
									</tr>
								</table>
							</div>
						</div>	
					</form>

            </div>
        </div>
        <!-- [ Hover-table ] end -->
    
<script type="text/javascript" src="assets/js/jquery-1.7.2.js"></script>
<script type="text/javascript" src="assets/js/pre-loader.js"></script>
<script src="assets/plugins/sweetalert/js/sweetalert.min.js"></script>
<!-- 表单验证提示 -->
<script src="assets/js/jquery.tips.js"></script>
<script type="text/javascript">

		//检索
		function searchs(){
			$("#Form").submit();
		}
		
		//新增
		function add(){
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>studyplanmx/goAdd?STUDYPLAN_ID=${pd.STUDYPLAN_ID}';
			 diag.Width = 800;
			 diag.Height = 600;
			 diag.Modal = true;				//有无遮罩窗口
			 diag. ShowMaxButton = true;	//最大化按钮
		     diag.ShowMinButton = true;		//最小化按钮
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('showform').style.display == 'none'){
					searchs();
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//删除
		function del(Id){
			swal({
                title: '',
	            text: "确定要删除 吗?",
	            icon: "warning",
	            buttons: true,
	            dangerMode: true,
            }).then((willDelete) => {
            	if (willDelete) {
	            	$.ajax({
	        			type: "POST",
	        			url: '<%=basePath%>studyplanmx/delete',
	        	    	data: {STUDYPLANMX_ID:Id,tm:new Date().getTime()},
	        			dataType:'json',
	        			cache: false,
	        			success: function(data){
	        				 if("success" == data.result){
	        					 swal("删除成功", "已经从列表中删除!", "success");
	        				 }
	        				 setTimeout(searchs, 1500);
	        			}
	        		});
            	}
	        });
		}
		
		//修改
		function edit(Id){
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '<%=basePath%>studyplanmx/goEdit?STUDYPLANMX_ID='+Id;
			 diag.Width = 800;
			 diag.Height = 600;
			 diag.Modal = true;				//有无遮罩窗口
			 diag. ShowMaxButton = true;	//最大化按钮
		     diag.ShowMinButton = true;		//最小化按钮
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('showform').style.display == 'none'){
					 searchs();
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//批量操作
		function makeAll(msg){
			swal({
                title: '',
	            text: msg,
	            icon: "warning",
	            buttons: true,
	            dangerMode: true,
            }).then((willDelete) => {
            	if (willDelete) {
	            	var str = '';
	    			for(var i=0;i < document.getElementsByName('ids').length;i++){
	    				  if(document.getElementsByName('ids')[i].checked){
	    				  	if(str=='') str += document.getElementsByName('ids')[i].value;
	    				  	else str += ',' + document.getElementsByName('ids')[i].value;
	    				  }
	    			}
	    			if(str==''){
	    				$("#cts").tips({
	    					side:2,
	    		            msg:'点这里全选',
	    		            bg:'#AE81FF',
	    		            time:3
	    		        });
	    				swal("", "您没有选择任何内容!", "warning");
	    				return;
	    			}else{
	    				if(msg == '确定要删除选中的数据吗?'){
	    					$.ajax({
	    						type: "POST",
	    						url: '<%=basePath%>studyplanmx/deleteAll?tm='+new Date().getTime(),
	    				    	data: {DATA_IDS:str},
	    						dataType:'json',
	    						cache: false,
	    						success: function(data){
	    							if("success" == data.result){
	    	        					 swal("删除成功", "已经从列表中删除!", "success");
	    	        				 }
	    	        				 setTimeout(searchs, 1500);
	    						}
	    					});
	    				}
	    			}
            	}
	        });
		}
		
		//导出excel
		function toExcel(){
			window.location.href='<%=basePath%>studyplanmx/excel';
		}
		
		$(function() {
			//复选框控制全选,全不选 
			$('#zcheckbox').click(function(){
			 if($(this).is(':checked')){
				 $("input[name='ids']").click();
			 }else{
				 $("input[name='ids']").attr("checked", false);
			 }
			});
		})
		
	</script>


</body>
</html>