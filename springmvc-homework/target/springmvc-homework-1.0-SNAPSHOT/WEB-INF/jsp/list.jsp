<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="${ctx}/webjars/jquery/3.3.1-2/jquery.min.js"></script><html>
<head>
    <title>list</title>
    <script>
        function add() {
            window.location.href="/resume/add"
        }
        function update(id) {
            window.location.href="/resume/update?id=" + id
        }

        function remove(id) {
            $.ajax({
                type : "GET",
                contentType: "application/json;charset=UTF-8",
                url : "/resume/delete",
                data : "id=" + id,
                success : function (result) {
                    window.location.href="/resume/list"
                },
                error : function (e) {
                    window.location.href="/resume/list"
                }
            })
        }
    </script>
</head>
<body>
resume infos:
<table>
    <c:forEach items="${resumes}" var="resume">
        <tr>
            <td>${resume.id} </td>
            <td>${resume.address}</td>
            <td>${resume.name}</td>
            <td>${resume.phone} <button onclick="update(${resume.id})">编辑</button><button onclick="remove(${resume.id})">删除</button></td>
        </tr>
    </c:forEach>
</table>
<button onclick="add()">新增</button>
</body>
</html>
