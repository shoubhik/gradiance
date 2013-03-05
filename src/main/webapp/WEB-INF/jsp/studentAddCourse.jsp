<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Course</title>
</head>
<body>
Only the courses that have not expired and not previously added can be added.<br/>
<form:form method="post" modelAttribute="addCourse">
    <form:errors path="" cssClass="error"/>
    <table>
        <tr>
            <td>SelectCourse <form:select path="course" items="${course}" multiple="false"/></td>
            <td><form:errors path="course" cssClass="error"/></td>
        </tr>
        <tr>
            <td>SelectCourse <form:input path="token" /></td>
            <td><form:errors path="course" cssClass="error"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="Add"/></td>
        </tr>
    </table>
</form:form>
<br/>
<a href="home">back</a>
</body>
</html>