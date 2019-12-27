<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html>
<head>
    <meta charset="UTF-8">
</head>
<body>
<h2>Hello World!</h2>
<form>
    管理员: <input id="aName"  type="text"><br>
    密码:<input id="aPassword"  type="text"><br>
    <input type="button"  value="登录" onclick="login()">
</form>
<script src="static/js/jquery.min.js"></script>
<script>
    function login() {
        $.ajax({
            url: "toLogin",
            type: "post",
            data: {
                username: $("#aName").val(),
                password: $("#aPassword").val()
            },
            success: function(res){
                alert(res);
            }
        });
    }
</script>
</body>
</html>
