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
    <meta name="author" content="FH Admin" />

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

    <!-- [ 主内容区 ] start -->
        <div class="pcoded-wrapper">
            <div class="pcoded-content">
                <div class="pcoded-inner-content">
                    <div class="main-body">
                        <div class="page-wrapper">
                            <!-- [ Main Content ] start -->
                            <div class="row">

							    <!-- [ Hover-table ] start -->
                                <div class="col-xl-12">
                                    <div class="card">
							
										<form action="workplan/report" method="post" name="Form" id="Form">
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
                                        			<table class="table table-hover">	
														<thead>
															<tr>
																<th>部门</th>
																<th>提交人</th>
																<!--<th>接收人</th>-->
																<!--<th>抄送人</th>-->
																<!--<th>工作内容</th>-->
																<th>开始时间</th>
																<th>是否提交</th>
																<th>平均分</th>
															</tr>
														</thead>
														<tbody>
														<!-- 开始循环 -->	
														<c:choose>
															<c:when test="${not empty varList}">
																<c:forEach items="${varList}" var="var" varStatus="vs">
																	<tr>
																		<td>${var.DEPARTMENT}</td>
																		<td>${var.zname}</td>
																		<!--<td>${var.COPYNAME}</td>-->
																		<!--<td>${var.CONTENET}</td>-->
																		<td>${var.STARTTIME}</td>
																		<td>${var.submit==0?'否':'是'}</td>
																		<td>${var.SCORE==null?'未评分':var.SCORE}</td>
																		<!--<td>${var.ENDTIME}</td>-->
																		<!--<td>${var.PROC_INST_ID_}</td>-->
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
												</div>
                                    		</div>
										</form>
			
                                    </div>
                                </div>
                                <!-- [ Hover-table ] end -->

                            </div>
                            <!-- [ Main Content ] end -->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    <!-- [ 主内容区 ] end -->
    
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
			 diag.URL = '<%=basePath%>workplan/goAdd';
			 diag.Width = 1000;
			 diag.Height = 800;
			 diag.Modal = true;				//有无遮罩窗口
			 diag. ShowMaxButton = true;	//最大化按钮
		     diag.ShowMinButton = true;		//最小化按钮
			 diag.CancelEvent = function(){ //关闭事件
				searchs();
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
	        			url: '<%=basePath%>workplan/delete',
	        	    	data: {WORKPLAN_ID:Id,tm:new Date().getTime()},
	        			dataType:'json',
	        			cache: false,
	        			success: function(data){
	        				 if("success" == data.result){
        		                swal("删除成功", "已经从列表中删除!", "success");
        		                setTimeout(searchs, 1500);
	        				 }else{
	     		                 swal("删除失败!", "请先删除明细数据!", "warning");
	        				 }
	        				 
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
			 diag.URL = '<%=basePath%>workplan/goEdit?WORKPLAN_ID='+Id;
			 diag.Width = 1000;
			 diag.Height = 800;
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
		
		//导出excel
		function toExcel(){
			window.location.href='<%=basePath%>workplan/reportExcel';
		}

</script>

</body>
</html>