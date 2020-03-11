<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/3/10
  Time: 17:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login</title>
</head>
<body>
<form action="/login" method="post">
    username：<input type="text" name="username" placeholder="username"/>
    password：<input type="password" name="password" placeholder="username"/>
    <button type="submit">login</button>
</form>
<h4>${error}</h4>
</body>
</html>
