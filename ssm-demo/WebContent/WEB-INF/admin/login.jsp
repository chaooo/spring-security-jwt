<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<base href="/ssm-AdminLogin/">
<meta charset="utf-8">
<title>Insert title here</title>
</head>
<body>
    <form>
	    name: <input id="userName"  type="text"  name="name"> <br>
	    password:<input id="userPassword"  type="text"  name="password"><br>
	    <input id="loginBtn"  type="button"  value="登录">
    </form>
    
<script src="js/jquery.min.js"></script>
<script>
$("#loginBtn").on("click", function(){
    $.ajax({
        url: "admin/login",
        type: "post",
        data: {
        	//id: 0, 
        	name: $("#userName").val(),
        	password: $("#userPassword").val()
        },
        success: function(res){
            alert(res);
        }
    });
});



</script>