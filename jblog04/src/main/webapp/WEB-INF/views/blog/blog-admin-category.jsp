<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>

<Link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/jblog.css">
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/jquery/jquery-3.4.1.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/ejs/ejs.js"></script>	
	
	
<script type="text/javascript">
var nlist;
var isEnd= false;
var id= ""


var listItemTemplate = new EJS({
		url: "${pageContext.request.contextPath }/assets/js/ejs/list-item-template.ejs"
	});


var listTemplate = new EJS({
	url: "${pageContext.request.contextPath }/assets/js/ejs/list-template.ejs"
});	


var fetchList = function(){
	if(isEnd){
		return;	
	}
	
	$.ajax({
		url: '${pageContext.request.contextPath }/api/category/list/' + "${authUser.id}",
		async: true,
		type: 'get',
		dataType: 'json',
		data: '',
		success: function(response){
			if(response.result != "success"){
				console.error(response.message);
				return;
			}
			
			var contextPath="${pageContext.request.contextPath }" ;
			response.data.contextPath= contextPath;
			console.log(response)
			var authUser =${authUser.id}
			response.data.authUserId=authUser;
			
			
			
			
			var html = listTemplate.render(response);
			$(".admin-cat").append(html);
			
		
		},
		error: function(xhr, status, e){
			console.error(status + ":" + e);
		}
	});	
}

$(function(){
	
	
	$(document).on('click', '.admin-cat a', function(event){
		event.preventDefault();
		var no = $(this).data('no');
		//$(this).parents('tr').remove();
		
		
			
			$.ajax({
				url: '${pageContext.request.contextPath }/api/category/delete/' + no,
				async: true,
				type: 'delete',
				dataType: 'json',
				data: '' ,
				success: function(response){
					if(response.result != "success"){
						console.error(response.message);
						return;
					}	
					
					
					//$(".admin-cat  [data-no=" + no + "]").remove();
					 
					  $(".admin-cat  #deleteTr").remove();
						
					  
					  // 리스트 추가 
					  var contextPath="${pageContext.request.contextPath }" ;
					  response.data.contextPath= contextPath;
					  console.log(response)
					  var authUser =${authUser.id}
					  response.data.authUserId=authUser;
					  
					  
					  var html = listTemplate.render(response);
						console.log(html);
					  
					  $(".admin-cat  #deleteTr").remove();
					  $(".admin-cat").append(html);
			
			
			
					// var html = listTemplate.render(response);
					//$(".admin-cat").append(html);
					  
					return;
					
					
				},
				error: function(xhr, status, e){
					console.error(status + ":" + e);
				}
			});
			
			
		
	
	
	});

	//////////////////////////////////////////
	
	$('#admin-cat-add').submit(function(event){
		event.preventDefault();
		
		var vo = {};
		vo.postCount= 0;
	
		
	
		vo.name = $("#input-text").val();
		if(vo.name == ''){
			
			$("#input-text").focus();
			return;	
		}
		
		vo.description = $("#input-description").val();
		if(vo.description == ''){
			$("#input-description").focus();
			return;	
		}
	
	
		$.ajax({
			url: '${pageContext.request.contextPath }/api/category/add',
			async: true,
			type: 'post',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(vo),
			success: function(response){
				if(response.result != "success"){
					console.error(response.message);
					return;
				}
				var k = $('.admin-cat tr').last().index();
				var num=k+1;
				response.data.num=num;
				
				
				var contextPath="${pageContext.request.contextPath }" ;
				response.data.contextPath= contextPath;
		
				var html = listItemTemplate.render(response.data);
				
			
				
				$(".admin-cat").append(html);
				
				// form reset
				$("#admin-cat-add")[0].reset();
			},
			error: function(xhr, status, e){
				console.error(status + ":" + e);
			}
		});
		
		
	});
	
	
	
	

	
// 리스트 시작하기 
fetchList();
	
});
	/////////////////////////////////////////////////////////////////////////////////






</script>	
	
	
</head>


<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="wrapper">
			<div id="content" class="full-screen">
				<ul class="admin-menu">
					<li><a
						href="${pageContext.request.contextPath}/${authUser.id}/admin-basic">기본설정</a></li>
					<li class="selected">카테고리</li>
					<li><a
						href="${pageContext.request.contextPath}/${authUser.id}/admin-write">글작성</a></li>
				</ul>
				<table class="admin-cat">
					<tr>
						<th>번호</th>
						<th>카테고리명</th>
						<th>포스트 수</th>
						<th>설명</th>
						<th>삭제</th>
					</tr>
				<!--  
					<c:forEach items='${list}' var='vo' varStatus='status'>     
						<tr>
							<td>${status.index+1 }</td>
							<td>${vo.name}</td>
							<td>${vo.postCount }</td>
							<td>${vo.description}</td>
							
						
							<td>
							<c:if test="${(vo.postCount == 0)  && !(vo.no ==min) }">
							<a href="${pageContext.request.contextPath}/${authUser.id}/admin-category/delete/${vo.no}"><img
								src="${pageContext.request.contextPath}/assets/images/delete.jpg"></a>
								</c:if>
							</td>
								
						</tr>

					</c:forEach>
				-->

				</table>

				<h4 class="n-c">새로운 카테고리 추가</h4>
				<form action="" method="Post" id="admin-cat-add">
				<table >
					<tr>
						<td class="t">카테고리명</td>
						<td><input type="text" name="name" id="input-text"></td>
					</tr>
					<tr>
						<td class="t">설명</td>
						<td><input type="text" name="description" id ="input-description"></td>
					</tr>
					<tr>
						<td class="s">&nbsp;</td>
						<td><input type="submit" value="카테고리 추가"></td>
					</tr>
				</table>
				</form>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>