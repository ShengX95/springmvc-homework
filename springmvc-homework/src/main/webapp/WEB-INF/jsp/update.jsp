<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>更新</title>
</head>
<script src="${ctx}/webjars/jquery/3.3.1-2/jquery.min.js"></script><html>
<body>
update：
<form action="/resume/update" method="post">
    <input type="text" name="id" value="${resume.id}" readonly ="readonly"/>
    <input type="text" name="name" value="${resume.name}"/>
    <input type="text" name="address" value="${resume.address}"/>
    <input type="text" name="phone" value="${resume.phone}"/>
    <button type="submit">更新</button>
</form>
</body>
</html>
