<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

<script type="text/javascript">

	$(function(){

		//创建市场活动，向后端发送请求
		$("#createClueBtn").click(function (){
			//初始化
			$("#createClueForm")[0].reset();
			//弹出模态窗口
			$("#createClueModal").modal("show");
		});
		$("#saveClueBtn").click(function(){
	        var fullname        = $.trim($("#create-fullname").val());
			var appellation     = $("#create-appellation").val();
			var owner           = $("#create-owner").val();
			var company         = $.trim($("#create-company").val());
			var job             = $.trim($("#create-job").val());
			var email           = $.trim($("#create-email").val());
			var phone           = $.trim($("#create-phone").val());
			var website         = $.trim($("#create-website").val());
			var mphone           = $.trim($("#create-mphone").val());
			var state           = $("#create-state").val();
			var source          = $("#create-source").val();
			var description     = $.trim($("#create-description").val());
			var contactSummary  = $.trim($("#create-contactSummary").val());
			var nextContactTime = $.trim($("#create-nextContactTime").val());
			var address         = $.trim($("#create-address").val());

			//表单验证
			//正则表达式验证
			//带*非空

			//发送请求
			$.ajax({
				url:'workbench/clue/saveCreateClue.do',
				data:{
					 fullname 		:fullname,
					 appellation 	:appellation,
					 owner			:owner,
					 company		:company,
					 job			:job,
					 email			:email,
					 phone			:phone,
					 website		:website,
					 mphone			:mphone,
					 state          :state,
					 source			:source,
					 description	:description,
					 contactSummary :contactSummary,
					 nextContactTime:nextContactTime,
					 address        :address
				},
				type:'post',
				dataType:'json',
				success:function(data){
					if(data.code=="1"){
						//关闭模态窗口
						$("#createClueModal").modal("hide");
						//刷新线索列表，显示第一页数据，保持每页显示条数不变(作业)

					}else{
						//提示信息
						alert(data.message);
						//模态窗口不关闭
						$("#createClueModal").modal("show");
					}
					queryClueByConditionForPage(1,10);
				}
			})
		});
		queryClueByConditionForPage(1,10);
		$("#queryClueBtn").click(function (event){
			event.preventDefault();
			queryClueByConditionForPage(1,$("#demo_page_1").bs_pagination('getOption', 'rowsPerPage'));
		});
		$("#checkAll").click(function (){
			$("#tBody input[type='checkbox']").prop("checked",this.checked);
		});
		$("#tBody").on("click","input[type='checkbox']",function () {
			//如果列表中的所有checkbox都选中，则"全选"按钮也选中
			if($("#tBody input[type='checkbox']").size()==$("#tBody input[type='checkbox']:checked").size()){
				$("#checkAll").prop("checked",true);
			}else{//如果列表中的所有checkbox至少有一个没选中，则"全选"按钮也取消
				$("#checkAll").prop("checked",false);
			}
		});
		//删除市场活动
		$("#deleteClueBtn").click(function (){
			//获得选中的id
			var checkedIds = $("#tBody input[type='checkbox']:checked");
			if(checkedIds.size()==0){
				alert("请选择要删除的市场活动");
				return;
			}
			if(window.confirm("确认删除么？")){
				var ids="";
				$.each(checkedIds,function (){
					ids+="id="+this.value+"&";
				});
				ids=ids.substr(0,ids.length-1);
				$.ajax({
					url:'workbench/clue/deleteClueByIds.do',
					data:ids,
					type:'post',
					dataType:'json',
					success:function (data) {
						if(data.code=="1"){
							//刷新市场活动列表,显示第一页数据,保持每页显示条数不变
							queryClueByConditionForPage(1,$("#demo_page_1").bs_pagination('getOption', 'rowsPerPage'));
						}else{
							//提示信息
							alert(data.message);
						}
					}
				});
			}
		});

		$("#editClueBtn").click(function (){
			var checkedId = $("#tBody input[type='checkbox']:checked");
			if(checkedId.size()==0){
				alert("请选择需要修改的线索");
				return false;
			}else if(checkedId.size()>1){
				alert("最多每次只能修改一个线索");
				return false;
			}
			var id = checkedId[0].value;
			//将从数据库中查询出来的信息显示在模态窗口上面
			$.ajax({
				url:'workbench/clue/queryClueById.do',
				data:{
					id:id
				},
				type:'post',
				dataType:'json',
				success:function(data){
					//把查出来的线索显示在模态窗口上
					$("#edit-id").val(data.id);
					$("#edit-owner").val(data.owner);
					$("#edit-company").val(data.company);
					$("#edit-appellation").val(data.appellation);
					$("#edit-fullname").val(data.fullname);
					$("#edit-job").val(data.job);
					$("#edit-email").val(data.email);
					$("#edit-phone").val(data.phone);
					$("#edit-website").val(data.website);
					$("#edit-mphone").val(data.mphone);
					$("#edit-state").val(data.state);
					$("#edit-source").val(data.source);
					$("#edit-description").val(data.description);
					$("#edit-contactSummary").val(data.contactSummary);
					$("#edit-nextContactTime").val(data.nextContactTime);
					$("#edit-address").val(data.address);
				}
			});
		});

		$("#saveEditClue").click(function (){
			var id = $("#edit-id").val();
			var owner = $("#edit-owner").val();
			var company = $("#edit-company").val();
			var appellation = $("#edit-appellation").val();
			var fullname = $("#edit-fullname").val();
			var job  = $("#edit-job").val();
			var email = $("#edit-email").val();
			var phone = $("#edit-phone").val();
			var website = $("#edit-website").val();
			var mphone = $("#edit-mphone").val();
			var state = $("#edit-state").val();
			var source = $("#edit-source").val();
			var description = $("#edit-description").val();
			var contactSummary = $("#edit-contactSummary").val();
			var nextContactTime = $("#edit-nextContactTime").val();
			var address = $("#edit-address").val();
			$.ajax({
				url:'workbench/clue/saveEditClue.do',
				data:{
					id:id,
					owner : owner,
					company : company,
					appellation : appellation,
					fullname : fullname,
					job  : job,
					email : email,
					phone : phone,
					website : website,
					mphone : mphone,
					state : state,
					source : source,
					description : description,
					contactSummary : contactSummary,
					nextContactTime : nextContactTime,
					address : address
				},			
				success:function (data) {
					if(data.code=="1"){
						//刷新市场活动列表,保持页号和每页显示条数都不变
						queryClueByConditionForPage($("#demo_page_1").bs_pagination('getOption', 'currentPage'),$("#demo_page_1").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						//提示信息
						alert(data.message);
						//模态窗口不关闭
						$("#editClueModal").modal("show");
					}
				}
			})
		});

	});
	function queryClueByConditionForPage(pageNo,pageSize){

		var name = $("#query-name").val();
		var company = $("#query-company").val();
		var mphone = $("#query-mphone").val();
		var phone = $("#query-phone").val();
		var owner = $("#query-owner").val();
		var source = $("#query-source").val();
		var state = $("#query-state").val();

		$.ajax({
			url: 'workbench/clue/queryClueByConditionForPage.do',
			data:{
				name :name,
				company:company,
				mphone:mphone,
				phone:phone,
				owner:owner,
				source:source,
				state:state,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:'post',
			dataType: 'json',
			success:function (data){
				var htmlStr="";
				$.each(data.clueList,function(index,obj){
					htmlStr+="<tr class=\"active\">";
					htmlStr+="<td><input type=\"checkbox\" value=\""+obj.id+"\"/></td>";
					htmlStr+="<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/clue/detailClue.do?id="+obj.id+"'\">"+obj.fullname+"</a></td>";
					htmlStr+="<td>"+obj.company+"</td>";
					htmlStr+="<td>"+obj.mphone+"</td>";
					htmlStr+="<td>"+obj.phone+"</td>";
					htmlStr+="<td>"+obj.source+"</td>";
					htmlStr+="<td>"+obj.owner+"</td>";
					htmlStr+="<td>"+obj.state+"</td>";
					htmlStr+="</tr>";
				});

				$("#tBody").html(htmlStr);
				var totalPages=1;
				if(data.totalRows%pageSize==0){
					totalPages=data.totalRows/pageSize;
				}else{
					totalPages=parseInt(data.totalRows/pageSize)+1;
				}
				$("#demo_page_1").bs_pagination({
					currentPage:pageNo,//当前页号,相当于pageNo
					rowsPerPage:pageSize,//每页显示条数,相当于pageSize
					totalRows:data.totalRows,//总条数
					totalPages: totalPages,  //总页数,必填参数.
					visiblePageLinks:5,//最多可以显示的卡片数
					showGoToPage:true,//是否显示"跳转到"部分,默认true--显示
					showRowsPerPage:true,//是否显示"每页显示条数"部分。默认true--显示
					showRowsInfo:true,//是否显示记录的信息，默认true--显示
					//用户每次切换页号，都自动触发本函数;
					//每次返回切换页号之后的pageNo和pageSize
					onChangePage: function(event,pageObj) { // returns page_num and rows_per_page after a link has clicked

						queryClueByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage);
					}
				});
			}
		});
	}
</script>
</head>
<body>

	<!-- 创建线索的模态窗口 -->
	<div class="modal fade" id="createClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">创建线索</h4>
				</div>
				<div class="modal-body">
					<form id="createClueForm" class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
									<c:forEach items="${userList}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
							<label for="create-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-company">
							</div>
						</div>

						<div class="form-group">
							<label for="create-appellation" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-appellation">
									<c:forEach items="${appellationList}" var="appell">
										<option value="${appell.id}">
												${appell.value}
										</option>
									</c:forEach>
								</select>
							</div>
							<label for="create-fullname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-fullname">
							</div>
						</div>

						<div class="form-group">
							<label for="create-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-job">
							</div>
							<label for="create-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-email">
							</div>
						</div>

						<div class="form-group">
							<label for="create-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
							<label for="create-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-website">
							</div>
						</div>

						<div class="form-group">
							<label for="create-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-mphone">
							</div>
							<label for="create-state" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-state">
									<option></option>
									<c:forEach items="${clueStateList}" var="cs">
										<option value="${cs.id}">${cs.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="create-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-source">
									<option></option>
									<c:forEach items="${sourceList}" var="sl">
										<option value="${sl.id}">${sl.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>


						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">线索描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>

						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control" id="create-nextContactTime">
								</div>
							</div>
						</div>

						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

						<div style="position: relative;top: 20px;">
							<div class="form-group">
                                <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address"></textarea>
                                </div>
							</div>
						</div>
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="saveClueBtn" type="button" class="btn btn-primary" data-dismiss="modal">保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改线索的模态窗口 -->
	<div class="modal fade" id="editClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">修改线索</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">

						<div class="form-group">
							<label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
									<select class="form-control" id="edit-owner">
										<c:forEach items="${userList}" var="u">
											<option value="${u.id}">${u.name}</option>
										</c:forEach>
									</select>
							</div>
							<label for="edit-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-company" value="动力节点">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-appellation" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-appellation">
									<c:forEach items="${appellationList}" var="appell">
										<option value="${appell.id}">
												${appell.value}
										</option>
									</c:forEach>
								</select>
							</div>
							<label for="edit-fullname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-fullname" value="李四">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-job" value="CTO">
							</div>
							<label for="edit-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-email" value="lisi@bjpowernode.com">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone" value="010-84846003">
							</div>
							<label for="edit-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-website" value="http://www.bjpowernode.com">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-mphone" value="12345678901">
							</div>
							<label for="edit-state" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-state">
									<option></option>
									<c:forEach items="${clueStateList}" var="cs">
										<option value="${cs.id}">${cs.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="edit-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-source">
									<option></option>
									<c:forEach items="${sourceList}" var="sl">
										<option value="${sl.id}">${sl.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">这是一条线索的描述信息</textarea>
							</div>
						</div>

						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="edit-contactSummary">这个线索即将被转换</textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control" id="edit-nextContactTime" value="2017-05-01">
								</div>
							</div>
						</div>

						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address">北京大兴区大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="saveEditClue" type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
				</div>
			</div>
		</div>
	</div>




	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>线索列表</h3>
			</div>
		</div>
	</div>

	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">

			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司</div>
				      <input class="form-control" type="text" id="query-company">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司座机</div>
				      <input class="form-control" type="text" id="query-mphone">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索来源</div>
						<select class="form-control" id="query-source">
							<option></option>
							<c:forEach items="${sourceList}" var="sl">
								<option value="${sl.id}">${sl.value}</option>
							</c:forEach>
						</select>
				    </div>
				  </div>
				  <br>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">手机</div>
				      <input class="form-control" type="text" id="query-phone">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索状态</div>
						<select class="form-control" id="query-state">
							<option></option>
							<c:forEach items="${clueStateList}" var="cs">
								<option value="${cs.id}">${cs.value}</option>
							</c:forEach>
						</select>
				    </div>
				  </div>

				  <button id="queryClueBtn" type="submit" class="btn btn-default">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 40px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button id="createClueBtn" type="button" class="btn btn-primary" ><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button id="editClueBtn" type="button" class="btn btn-default" data-toggle="modal" data-target="#editClueModal"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button id="deleteClueBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>


			</div>
			<div style="position: relative;top: 50px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
							<td>公司</td>
							<td>公司座机</td>
							<td>手机</td>
							<td>线索来源</td>
							<td>所有者</td>
							<td>线索状态</td>
						</tr>
					</thead>
					<tbody id="tBody">

					</tbody>

				</table>
				<div id="demo_page_1">

				</div>
			</div>



		</div>

	</div>
</body>
</html>