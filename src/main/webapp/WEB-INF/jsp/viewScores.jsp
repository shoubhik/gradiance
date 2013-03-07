<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Scores</title>
</head>
<body>
<table border="1">
    <tr>
        <td>Homework Name</td>
        <td>Attempt #</td>
        <td>Score</td>
    </tr>
    <c:forEach var="homework" items="${homeworks}">
        <tr>
            <td><c:out value="${homework.name}"></c:out></td>
            <td>Attempt <c:out value="${homework.attemptCount}"></c:out></td>
            <td><c:out value="${homework.score}"/></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>