<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/3/11
  Time: 14:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加</title>
</head>
<body>
add：
<form action="/resume/add" method="post">
    <input type="text" name="name"/>
    <input type="text" name="address"/>
    <input type="text" name="phone"/>
    <button type="submit">添加</button>
</form>
</body>
</html>
